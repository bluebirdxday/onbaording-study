package com.fastcampus.springboot.getinline.service;

import com.fastcampus.springboot.getinline.constant.ErrorCode;
import com.fastcampus.springboot.getinline.constant.EventStatus;
import com.fastcampus.springboot.getinline.dto.EventDTO;
import com.fastcampus.springboot.getinline.exception.GeneralException;
import com.fastcampus.springboot.getinline.repository.EventRepository;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
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
            LocalDateTime eventEndDatetime
            // 문자열로 들어간 query param을 local date time으로 변환할 수 있게 도와주는 애노테이션
    )
    {
        try {
            return eventRepository.findEvents(
                    placeId,
                    eventName,
                    eventStatus,
                    eventStartDatetime,
                    eventEndDatetime); // => 책임을 리포지토리에 위임해서 결과만 바로 돌려주게끔

        }catch (Exception e){
            throw new GeneralException(ErrorCode.DATA_ACCESS_ERROR, e);
            // -> event service list 조회 부분에서 에러 났을 때
            //      유의미한 에러를 보내주기 위함
        }
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
