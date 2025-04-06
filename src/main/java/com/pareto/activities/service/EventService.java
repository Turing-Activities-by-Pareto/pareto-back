package com.pareto.activities.service;

import com.pareto.activities.DTO.EventCreateResponse;
import com.pareto.activities.DTO.EventGetResponse;
import com.pareto.activities.DTO.EventRequest;
import com.pareto.activities.DTO.EventsGetResponse;
import com.pareto.activities.entity.EventEntity;
import com.pareto.activities.entity.FileEntity;
import com.pareto.activities.enums.BusinessStatus;
import com.pareto.activities.enums.EConfirmStatus;
import com.pareto.activities.exception.BusinessException;
import com.pareto.activities.mapper.EventMapper;
import com.pareto.activities.repository.EventCategoryRepository;
import com.pareto.activities.repository.EventRepository;
import com.pareto.activities.repository.EventSubCategoryRepository;
import com.pareto.activities.repository.UserRepository;
import com.pareto.activities.util.Utils;
import io.minio.http.Method;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.json.JsonWriterSettings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static com.pareto.activities.config.Constants.ALLOWED_IMAGE_EXTENSIONS;
import static com.pareto.activities.util.Utils.toCamelCase;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventService {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final EventCategoryRepository eventCategoryRepository;
    private final EventSubCategoryRepository eventSubCategoryRepository;
    private final UserRepository userRepository;
    private final FileRepository fileRepository;
    private final IStorageService minioStorageService;
    private final MongoTemplate mongoTemplate;

    public EventCreateResponse createEvent(
            EventRequest event
    ) {
        SecurityContext context = SecurityContextHolder.getContext();
        if (!userRepository.existsById(context
                                               .getAuthentication()
                                               .getPrincipal()
                                               .toString())
        ) {
            throw new BusinessException(
                    ". with user Id %s .".formatted(context
                                                            .getAuthentication()
                                                            .getPrincipal()
                                                            .toString()),
                    BusinessStatus.USER_NOT_FOUND,
                    HttpStatus.NOT_FOUND
            );
        }

        if (!eventCategoryRepository.existsByName(event.getCategory())) {
            throw new BusinessException(
                    BusinessStatus.EVENT_CATEGORY_NOT_FOUND,
                    HttpStatus.NOT_FOUND
            );
        }

        if (!eventSubCategoryRepository.existsByName(event.getSubCategory())) {
            throw new BusinessException(
                    BusinessStatus.EVENT_SUB_CATEGORY_NOT_FOUND,
                    HttpStatus.NOT_FOUND
            );
        }

        String fileExtension = event.getFileExtension();
        if (fileExtension == null || fileExtension.isBlank()) {
            throw new BusinessException(
                    BusinessStatus.FILE_EXTENSION_NOT_FOUND,
                    HttpStatus.NOT_FOUND
            );
        }
        if (!ALLOWED_IMAGE_EXTENSIONS.contains(fileExtension.toLowerCase())) {
            throw new BusinessException(
                    BusinessStatus.INVALID_FILE_EXTENSION,
                    HttpStatus.BAD_REQUEST
            );
        }

        EventEntity eventEntity = eventMapper.toEventEntity(event);

        String fileNameWithExtension = minioStorageService.generateUniqueFileName() + fileExtension;
        String presignedUrl = minioStorageService.getObjectUrl(
                "event-background",
                fileNameWithExtension,
                Method.PUT
        );

        FileEntity savedFile = fileRepository.save(FileEntity
                                                           .builder()
                                                           .bucket("event-background")
                                                           .object(fileNameWithExtension)
                                                           .build());

        eventEntity.setConfirmStatus(EConfirmStatus.PENDING);
        eventEntity.setFileId(savedFile.getId());

        EventEntity savedEvent = eventRepository.save(eventEntity);
        EventCreateResponse eventCreateResponse = eventMapper.toEventCreateResponse(savedEvent);
        eventCreateResponse.setImageUploadUrl(presignedUrl);
        return eventCreateResponse;

    }

    @Transactional
    public EventGetResponse getEventById(
            String eventId
    ) {
        return eventMapper.toEventGetResponse(
                eventRepository
                        .findById(eventId)
                        .orElseThrow(() -> new BusinessException(
                                BusinessStatus.EVENT_NOT_FOUND,
                                HttpStatus.NOT_FOUND
                        ))
        );
    }

    public List<EventsGetResponse> getEvents() {
        return eventRepository
                .findAll()
                .stream()
                .map(eventMapper::toEventsGetResponse)
                .toList();
    }

    public String getObjectGetUrl(
            String eventId
    ) {
        return getObjectUrlbyMethod(
                eventId,
                Method.GET
        );
    }

    public String getObjectPutUrl(
            String eventId
    ) {
        return getObjectUrlbyMethod(
                eventId,
                Method.PUT
        );
    }

    private String getObjectUrlbyMethod(
            String eventId,
            Method method
    ) {

        EventEntity event = eventRepository
                .findById(eventId)
                .orElseThrow(() -> new BusinessException(
                        BusinessStatus.EVENT_NOT_FOUND,
                        HttpStatus.NOT_FOUND
                ));

        FileEntity fileEntity = fileRepository
                .findById(event.getFileId())
                .orElseThrow(() -> new BusinessException(
                        BusinessStatus.FILE_NOT_FOUND,
                        HttpStatus.NOT_FOUND
                ));

        String objectName = fileEntity.getObject();
        String bucket = fileEntity.getBucket();

        return minioStorageService.getObjectUrl(
                bucket,
                objectName,
                method
        );
    }

    public Page<EventsGetResponse> getEventsPage(
            int page,
            int size,
            MultiValueMap<String, String> filters
    ) {
        log.info(
                "pageable size: {}, page: {}",
                size,
                page
        );

        filters.remove("size");
        filters.remove("page");

        Set<String> localDateTimeKeys = Utils.getClassFieldNames(
                EventEntity.class,
                LocalDateTime.class,
                Utils.NamingConventionFormattingStrategy.CAMEL_TO_KEBAB
        );

        Set<String> collectionKeys = Utils.getClassFieldNames(
                EventEntity.class,
                Collection.class,
                Utils.NamingConventionFormattingStrategy.CAMEL_TO_KEBAB
        );

        Set<String> stringKeys = Utils.getClassFieldNames(
                EventEntity.class,
                String.class,
                Utils.NamingConventionFormattingStrategy.CAMEL_TO_KEBAB
        );


        log.info(
                "LocalDateTime fields {}",
                localDateTimeKeys
        );
        log.info(
                "String match fields {}",
                stringKeys
        );
        log.info(
                "Collection keys fields: {}",
                collectionKeys
        );

        Criteria[] allCriterias = filters
                .entrySet()
                .stream()
                .map(entry -> {
                         if (stringKeys.contains(entry.getKey())) {
                             return Criteria
                                     .where(toCamelCase(entry.getKey()))
                                     .is(entry
                                                 .getValue()
                                                 .getFirst()
                                     );
                         }

                         if (collectionKeys.contains(entry.getKey())) {
                             return Criteria
                                     .where(toCamelCase(entry.getKey()))
                                     .all(entry.getValue());
                         }

                         return null;
                     }
                )
                .filter(Objects::nonNull)
                .toArray(Criteria[]::new)
                ;

        Criteria criteria;
        if (allCriterias.length == 0) {
            criteria = Criteria
                    .where("title")
                    .exists(true);
        }
        else {
            criteria = new Criteria().andOperator(allCriterias);
        }

        log.info(
                "all criterias should look like:\n{}",
                criteria
                        .getCriteriaObject()
                        .toJson(
                                JsonWriterSettings
                                        .builder()
                                        .indent(true)
                                        .build()

                        )
        );

        PageRequest pageRequest = PageRequest.of(
                page,
                size
        );

        Query queryWithPage = new Query(criteria).with(pageRequest);

        long total = mongoTemplate.count(
                new Query(criteria),
                EventEntity.class
        );

        List<EventEntity> eventEntities = mongoTemplate.find(
                queryWithPage,
                EventEntity.class
        );

        Page<EventEntity> pageResult = new PageImpl<>(
                eventEntities,
                pageRequest,
                total
        );

        return pageResult.map(eventMapper::toEventsGetResponse);
    }
}
