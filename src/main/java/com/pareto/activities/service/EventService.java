package com.pareto.activities.service;

import com.pareto.activities.DTO.EventGetResponse;
import com.pareto.activities.DTO.EventRequest;
import com.pareto.activities.DTO.EventCreateResponse;
import com.pareto.activities.DTO.EventsGetResponse;
import com.pareto.activities.entity.EventEntity;
import com.pareto.activities.entity.FileEntity;
import com.pareto.activities.mapper.EventMapper;
import com.pareto.activities.repository.EventCategoryRepository;
import com.pareto.activities.repository.EventRepository;
import com.pareto.activities.repository.SubEventCategoryRepository;
import io.minio.http.Method;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventService {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final FileRepository fileRepository;
    private final EventCategoryRepository eventCategoryRepository;
    private final SubEventCategoryRepository subEventCategoryRepository;
    private final StorageService storageService;

    public EventCreateResponse createEvent(EventRequest event) {

        EventEntity eventEntity = eventMapper.toEventEntity(event);

        eventEntity.setCategory(
                eventCategoryRepository.findByName(event.getCategory())
        );
        eventEntity.setSubCategory(
                subEventCategoryRepository.findByName(event.getSubCategory())
        );

        FileEntity fileEntity = new FileEntity();
        FileEntity fileEntityDB = fileRepository.save(fileEntity);

        eventEntity.setFile(fileEntityDB);
        EventEntity eventEntityDB = eventRepository.save(eventEntity);
        String objectName = fileEntityDB.getId() + "." + event.getFileExtension();

        String presignedUrl = storageService.getObjectUrl(
                "event-background",
                objectName,
                Method.PUT
        );

        fileEntityDB.setBucket("event-background");
        fileEntityDB.setObject(objectName);

        fileRepository.save(fileEntityDB);

        EventCreateResponse response = eventMapper.toEventCreateResponse(eventEntityDB);

        response.setCategory(
                eventEntityDB.getCategory().getName()
        );
        response.setSubCategory(
                eventEntityDB.getSubCategory().getName()
        );

        response.setImageUploadUrl(presignedUrl);

        return response;
    }

    @Transactional
    public EventGetResponse getEventById(Long eventId) {
        return eventMapper.toEventGetResponse(
         eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("not found"))
         );
    }

    public List<EventsGetResponse> getEvents() {
        return eventRepository.findAll().stream()
                .map(eventMapper::toEventsGetResponse).toList();
    }

    public String getObjectGetUrl(Long eventId) {
        return getObjectUrlbyMethod(eventId, Method.GET);
    }

    public String getObjectPutUrl(Long eventId) {
        return getObjectUrlbyMethod(eventId, Method.PUT);
    }

    private String getObjectUrlbyMethod(
            Long eventId,
            Method method
    ) {
        FileEntity fileEntity = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("not found")).getFile();

        String objectName = fileEntity.getObject();
        String bucket = fileEntity.getBucket();

        return storageService.getObjectUrl(bucket, objectName, method);
    }
}
