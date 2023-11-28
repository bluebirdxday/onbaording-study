package com.fastcampus.springboot.getinline.service;

import com.fastcampus.springboot.getinline.constant.EventStatus;
import com.fastcampus.springboot.getinline.dto.EventDTO;
import com.fastcampus.springboot.getinline.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class EventService{

    private final EventRepository eventRepository;

    public List<EventDTO> getEvents(
            Long placeId,
            String eventName,
            EventStatus eventStatus,
            LocalDateTime eventStartDatetime,
            LocalDateTime eventEndDatetime)
    {
        return eventRepository.findEvents(placeId, eventName, eventStatus, eventStartDatetime, eventEndDatetime); // => 책임을 리포지토리에 위임해서 결과만 바로 돌려주게끔
    }

    public Optional<EventDTO> getEvent(Long eventId){
        return eventRepository.findEvent(eventId);
    }

    public boolean createEvent(EventDTO eventDTO){
        return eventRepository.insertEvent(eventDTO);
    }

    public boolean modifyEvent(Long eventId, EventDTO eventDTO){
        return eventRepository.updateEvent(eventId, eventDTO);
    }

    public boolean removeEvent(Long eventId){
        return eventRepository.deleteEvent(eventId);
    }
}
