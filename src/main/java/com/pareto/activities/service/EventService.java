package com.pareto.activities.service;

import com.google.common.base.CaseFormat;
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

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.pareto.activities.config.Constants.ALLOWED_IMAGE_EXTENSIONS;

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
        return eventMapper.toEventGetResponse(eventRepository
                                                      .findById(eventId)
                                                      .orElseThrow(() -> new BusinessException(
                                                              BusinessStatus.EVENT_NOT_FOUND,
                                                              HttpStatus.NOT_FOUND
                                                      )));
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
            MultiValueMap<String, String> filters
    ) {
        int size = 10;
        int page = 1;

        // @formatting off
        if (filters != null && !filters.isEmpty() && filters.containsKey("size") && !filters
                .get("size")
                .isEmpty()
        ) {
            size = Integer.parseInt(
                    filters
                            .get("size")
                            .getFirst()
            );
        }

        if (filters != null && !filters.isEmpty() && filters.containsKey("page") && !filters
                .get("page")
                .isEmpty()) {
            page = Integer.parseInt(
                    filters
                            .get("page")
                            .getFirst()
            );
        }
        // @formatting on

        log.info(
                "pageable size: {}, page: {}",
                size,
                page
        );

        filters.remove("size");
        filters.remove("page");

        Set<String> localDateTimeKeys = Arrays
                .stream(EventEntity.class.getDeclaredFields())
                .filter(field -> field.getType() == LocalDateTime.class)
                .map(Field::getName)
                .map(camel -> CaseFormat.LOWER_CAMEL.to(
                        CaseFormat.LOWER_HYPHEN,
                        camel
                ))
                .collect(Collectors.toSet())
                ;

        Set<String> listKeys = Arrays
                .stream(EventEntity.class.getDeclaredFields())
                .filter(field -> Collection.class.isAssignableFrom(field.getType()))
                .map(Field::getName)
                .map(camel -> CaseFormat.LOWER_CAMEL.to(
                        CaseFormat.LOWER_HYPHEN,
                        camel
                ))
                .filter(name -> !localDateTimeKeys.contains(name))
                .collect(Collectors.toSet())
                ;

        Set<String> equalityChekcKeys = Arrays
                .stream(EventEntity.class.getDeclaredFields())
                .filter(field -> !Collection.class.isAssignableFrom(field.getType()))
                .map(Field::getName)
                .map(camel -> CaseFormat.LOWER_CAMEL.to(
                        CaseFormat.LOWER_HYPHEN,
                        camel
                ))
                .filter(name -> !localDateTimeKeys.contains(name))
                .collect(Collectors.toSet())
                ;

        log.info(
                "LocalDateTime fields {}",
                localDateTimeKeys
        );
        log.info(
                "other Exact match fields {}",
                equalityChekcKeys
        );
        log.info(
                "List keys fields: {}",
                listKeys
        );

        Criteria[] criteriaList = filters
                .entrySet()
                .stream()
                .filter(entry -> equalityChekcKeys.contains(entry.getKey()))
                .map(entry -> new Criteria()
                        .orOperator(
                                entry
                                        .getValue()
                                        .stream()
                                        .map(value -> Criteria
                                                .where(CaseFormat.LOWER_HYPHEN.to(
                                                        CaseFormat.LOWER_CAMEL,
                                                        entry.getKey()
                                                ))
                                                .is(value)
                                        )
                                        .toList()
                        )
                )
                .toArray(Criteria[]::new)
                ;

        Criteria[] listCheckCriteriaList = filters
                .entrySet()
                .stream()
                .filter(entry -> listKeys.contains(entry.getKey()))
                .map(value -> Criteria
                        .where(CaseFormat.LOWER_HYPHEN.to(
                                CaseFormat.LOWER_CAMEL,
                                value.getKey()
                        ))
                        .in(value.getValue())
                )
                .toArray(Criteria[]::new)
                ;
        ;

        Criteria criteria = null;

        if (criteriaList.length == 0 && listCheckCriteriaList.length == 0) {
            criteria = new Criteria();
        }

        if (criteriaList.length != 0 && listCheckCriteriaList.length == 0) {
            criteria = new Criteria().orOperator(
                    new Criteria().orOperator(criteriaList)
            );
        }

        if (criteriaList.length == 0 && listCheckCriteriaList.length != 0) {
            criteria = new Criteria().orOperator(
                    listCheckCriteriaList
            );
        }

        if (criteriaList.length != 0 && listCheckCriteriaList.length != 0) {
            criteria = new Criteria().orOperator(
                    new Criteria().orOperator(criteriaList),
                    new Criteria().andOperator(listCheckCriteriaList)
            );
        }

        log.info(
                "this is criteria: {}",
                criteria
                        .getCriteriaObject()
                        .toJson(JsonWriterSettings
                                        .builder()
                                        .indent(true)
                                        .build())
        );

        PageRequest pageRequest = PageRequest.of(
                page,
                size
        );
        Query query = new Query(criteria).with(pageRequest);

        List<EventEntity> eventEntities = mongoTemplate.find(
                query,
                EventEntity.class
        );
        long total = mongoTemplate.count(
                query,
                EventEntity.class
        );

        Page<EventEntity> eventEntityPage = new PageImpl<>(
                eventEntities,
                pageRequest,
                total
        );
        return eventEntityPage.map(eventMapper::toEventsGetResponse);
    }
}
