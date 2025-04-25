package com.pareto.activities.mapper.manual;

import com.pareto.activities.dto.EventCreateResponse;
import com.pareto.activities.dto.EventGetResponse;
import com.pareto.activities.dto.EventRequest;
import com.pareto.activities.entity.EventEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.stream.Collectors;

@Component
public class EventMapper {

    public EventEntity toEventEntity(EventRequest eventRequest) {
        return EventEntity.builder()
                .title(eventRequest.getTitle())
                .description(eventRequest.getDescription())
                .isLocal(eventRequest.getIsLocal())
                .deadline(eventRequest.getDeadline())
                .startDate(eventRequest.getStartDate())
                .endDate(eventRequest.getEndDate())
                .participantCategories(new HashSet<>())
                .requests(new ArrayList<>())
                .unlimitedSeats(eventRequest.getUnlimitedSeats())
                .remainingSeats(eventRequest.getTotalSeats())
                .build();
    }

    public EventCreateResponse toEventCreateResponse(EventEntity savedEvent, String presignedUrl) {
        return EventCreateResponse.builder()
                .id(savedEvent.getId())
                .title(savedEvent.getTitle())
                .description(savedEvent.getDescription())
                .isLocal(savedEvent.getIsLocal())
                .location(savedEvent.getLocation().getName())
                .participantCategories(savedEvent.getParticipantCategories().stream().map( pCat -> pCat.getName()).collect(Collectors.toSet()))
                .category(savedEvent.getCategory().getName())
                .subCategory(savedEvent.getSubCategory().getName())
                .unlimitedSeats(savedEvent.getUnlimitedSeats())
                .remainingSeats(savedEvent.getRemainingSeats())
                .imageUploadUrl(presignedUrl)
                .build();
    }

    public EventGetResponse toEventGetResponse(EventEntity eventEntity, String objectGetUrl) {
        return EventGetResponse.builder()
                .id(eventEntity.getId())
                .title(eventEntity.getTitle())
                .description(eventEntity.getDescription())
                .isLocal(eventEntity.getIsLocal())
                .location(eventEntity.getLocation().getName())
                .participantCategories(eventEntity.getParticipantCategories().stream().map( pCat -> pCat.getName()).collect(Collectors.toSet()))
                .category(eventEntity.getCategory().getName())
                .subCategory(eventEntity.getSubCategory().getName())
                .unlimitedSeats(eventEntity.getUnlimitedSeats())
                .remainingSeats(eventEntity.getRemainingSeats())
                .imageGetUrl(objectGetUrl)
                .build();
    }
}
