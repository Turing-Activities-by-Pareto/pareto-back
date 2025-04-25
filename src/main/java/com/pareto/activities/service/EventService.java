package com.pareto.activities.service;

import com.pareto.activities.dto.EventCreateResponse;
import com.pareto.activities.dto.EventFilter;
import com.pareto.activities.dto.EventGetResponse;
import com.pareto.activities.dto.EventRequest;
import com.pareto.activities.entity.EventCategoryEntity;
import com.pareto.activities.entity.EventEntity;
import com.pareto.activities.entity.EventSubCategoryEntity;
import com.pareto.activities.entity.FileEntity;
import com.pareto.activities.entity.Location;
import com.pareto.activities.entity.ParticipantCategory;
import com.pareto.activities.entity.UserEntity;
import com.pareto.activities.enums.BusinessStatus;
import com.pareto.activities.exception.BusinessException;
import com.pareto.activities.mapper.manual.EventMapper;
import com.pareto.activities.repository.EventCategoryRepository;
import com.pareto.activities.repository.EventRepository;
import com.pareto.activities.repository.EventSubCategoryRepository;
import com.pareto.activities.repository.LocationRepository;
import com.pareto.activities.repository.ParticipantCategoryRepository;
import com.pareto.activities.repository.UserRepository;
import com.pareto.activities.specification.EventSpecification;
import io.minio.http.Method;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.pareto.activities.config.Constants.ALLOWED_IMAGE_EXTENSIONS;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventService {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final UserRepository userRepository;
    private final EventCategoryRepository eventCategoryRepository;
    private final EventSubCategoryRepository eventSubCategoryRepository;
    private final LocationRepository locationRepository;
    private final ParticipantCategoryRepository participantCategoryRepository;
    private final EventRequestService eventRequestService;
    private final IStorageService minioStorageService;

    @Transactional
    public EventCreateResponse createEvent(
            EventRequest eventRequest,
            Long userId
    ) {
        //todo: alert EventRequest about this new created event (event is pending.)
        EventEntity eventEntity = eventMapper.toEventEntity(eventRequest);

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(BusinessStatus.USER_NOT_FOUND));
        EventCategoryEntity category = eventCategoryRepository.findByName(eventRequest.getCategory())
                .orElseThrow(() -> new BusinessException(BusinessStatus.EVENT_CATEGORY_NOT_FOUND));
        EventSubCategoryEntity subCategory = eventSubCategoryRepository.findByName(eventRequest.getSubCategory())
                .orElseThrow(() -> new BusinessException(BusinessStatus.EVENT_SUB_CATEGORY_NOT_FOUND));


        Location location = eventRequest.getIsLocal() ?
                locationRepository.findByName(eventRequest.getLocation())
                        .orElseThrow(() -> new BusinessException(BusinessStatus.LOCATION_NOT_FOUND))
                :
                locationRepository.findByName(eventRequest.getLocation())
                        .orElse(
                                locationRepository.save(
                                        Location.builder()
                                                .name(eventRequest.getLocation())
                                                .isLocal(eventRequest.getIsLocal())
                                                .build()
                                )
                        );

        if (eventRequest.getUnlimitedSeats() && eventRequest.getTotalSeats() != 0) {
            throw new BusinessException(BusinessStatus.INVALID_ARGUMENTS);
        }

        Set<ParticipantCategory> participantCategories = new HashSet<>();
        for (String participantCategory : eventRequest.getParticipantCategories()) {
            participantCategories.add(
                    participantCategoryRepository.findByName(participantCategory)
                            .orElse(participantCategoryRepository.save(ParticipantCategory.builder()
                                    .name(participantCategory)
                                    .events(new HashSet<>())
                                    .build())
                            )
            );
        }


        String fileExtension = eventRequest.getFileExtension();
        if (!ALLOWED_IMAGE_EXTENSIONS.contains(fileExtension)) {
            throw new BusinessException(BusinessStatus.INVALID_FILE_EXTENSION);
        }
        String objectName = minioStorageService.generateUniqueFileName() + "." + fileExtension;
        FileEntity fileEntity = FileEntity.builder()
                .bucket("event-background")
                .object(objectName)
                .build();

        String presignedUrl = minioStorageService.getObjectUrl(
                "event-background",
                objectName,
                Method.PUT
        );

        eventEntity.addUser(user);
        eventEntity.addCategory(category);
        eventEntity.addSubCategory(subCategory);
        eventEntity.setFile(fileEntity);
        eventEntity.setLocation(location);
        eventEntity.addAllParticipantCategories(participantCategories);

        EventEntity savedEvent = eventRepository.save(eventEntity);
        eventRequestService.createOrUpdatePendingRequest(savedEvent, user, LocalDateTime.now());

        return eventMapper.toEventCreateResponse(savedEvent, presignedUrl);
    }

    public EventGetResponse getEventById(
            Long eventId
    ) {
        return eventMapper.toEventGetResponse(
                eventRepository
                        .findById(eventId)
                        .orElseThrow(() -> new BusinessException(
                                BusinessStatus.EVENT_NOT_FOUND,
                                HttpStatus.NOT_FOUND
                        )),
                getObjectGetUrl(eventId)
        );
    }

    public List<EventGetResponse> getEvents() {
        return eventRepository
                .findAll()
                .stream()
                .map(event ->
                        eventMapper.toEventGetResponse(event, getObjectGetUrl(event.getId()))
                )
                .toList();
    }

    public Page<EventGetResponse> getEventsPage(
            int page,
            int size,
            EventFilter eventFilter
    ) {
        return eventRepository
                .findAll(
                        EventSpecification.filterEvents(eventFilter),
                        PageRequest.of(page, size)
                )
                .map(event ->
                        eventMapper.toEventGetResponse(event, getObjectGetUrl(event.getId()))
                );
    }

    public String getObjectGetUrl(
            Long eventId
    ) {
        return getObjectUrlbyMethod(
                eventId,
                Method.GET
        );
    }

    public String getObjectPutUrl(
            Long eventId
    ) {
        return getObjectUrlbyMethod(
                eventId,
                Method.PUT
        );
    }

    private String getObjectUrlbyMethod(
            Long eventId,
            Method method
    ) {
        FileEntity fileEntity = eventRepository
                .findById(eventId)
                .orElseThrow(() -> new BusinessException(
                        BusinessStatus.EVENT_NOT_FOUND,
                        HttpStatus.NOT_FOUND
                ))
                .getFile();

        String objectName = fileEntity.getObject();
        String bucket = fileEntity.getBucket();

        return minioStorageService.getObjectUrl(
                bucket,
                objectName,
                method
        );
    }
}
