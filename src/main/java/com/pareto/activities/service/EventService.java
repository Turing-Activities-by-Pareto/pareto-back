package com.pareto.activities.service;

import com.pareto.activities.DTO.EventGetResponse;
import com.pareto.activities.DTO.EventRequest;
import com.pareto.activities.DTO.EventCreateResponse;
import com.pareto.activities.config.MinioConfig;
import com.pareto.activities.entity.EventEntity;
import com.pareto.activities.entity.FileEntity;
import com.pareto.activities.mapper.EventMapper;
import com.pareto.activities.repository.EventCategoryRepository;
import com.pareto.activities.repository.EventRepository;
import com.pareto.activities.repository.SubEventCategoryRepository;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.http.Method;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

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

        String presignedUrl = storageService.getObjectUrl(
                "event-background",
                String.valueOf(fileEntityDB.getId()) + "." + event.getFileExtension()
        );

        fileEntityDB.setBucket("event-background");
        fileEntityDB.setObject(String.valueOf(fileEntityDB.getId()));

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
}
