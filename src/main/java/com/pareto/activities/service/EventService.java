package com.pareto.activities.service;

import com.pareto.activities.DTO.EventCreateResponse;
import com.pareto.activities.DTO.EventGetResponse;
import com.pareto.activities.DTO.EventRequest;
import com.pareto.activities.DTO.EventsGetResponse;
import com.pareto.activities.entity.EventEntity;
import com.pareto.activities.entity.FileEntity;
import com.pareto.activities.enums.BusinessStatus;
import com.pareto.activities.exception.BusinessException;
import com.pareto.activities.mapper.EventMapper;
import com.pareto.activities.repository.EventRepository;
import io.minio.http.Method;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventService {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final FileRepository fileRepository;
    private final IStorageService minioStorageService;

    public EventCreateResponse createEvent(
            EventRequest event
    ) {

        EventEntity eventEntity = eventMapper.toEventEntity(event);

        FileEntity fileEntity = new FileEntity();
        FileEntity fileEntityDB = fileRepository.save(fileEntity);

        eventEntity.setFile(fileEntityDB);
        EventEntity eventEntityDB = eventRepository.save(eventEntity);
        String objectName = fileEntityDB.getId() + "." + event.getFileExtension();

        String presignedUrl = minioStorageService.getObjectUrl(
                "event-background",
                objectName,
                Method.PUT
        );

        fileEntityDB.setBucket("event-background");
        fileEntityDB.setObject(objectName);

        fileRepository.save(fileEntityDB);

        EventCreateResponse response = eventMapper.toEventCreateResponse(eventEntityDB);

        response.setImageUploadUrl(presignedUrl);

        return response;
    }

    @Transactional
    public EventGetResponse getEventById(
            Long eventId
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
                .getFile()
                ;

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
            int size
    ) {
        return eventRepository
                .findAll(
                        PageRequest.of(
                                page,
                                size
                        )
                )
                .map(eventMapper::toEventsGetResponse);
    }
}
