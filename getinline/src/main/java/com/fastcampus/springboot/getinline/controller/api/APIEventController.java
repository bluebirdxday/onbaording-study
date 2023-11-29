package com.fastcampus.springboot.getinline.controller.api;

import com.fastcampus.springboot.getinline.constant.EventStatus;
import com.fastcampus.springboot.getinline.dto.APIDataResponse;
import com.fastcampus.springboot.getinline.dto.EventRequest;
import com.fastcampus.springboot.getinline.dto.EventResponse;
import com.fastcampus.springboot.getinline.service.EventService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Validated
@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class APIEventController {

    private final EventService eventService;

    // @Validated 방식 검증
    @GetMapping("/events")
    public APIDataResponse<List<EventResponse>> getEvents(
            @Positive Long placeId,
            @Size(min=2) String eventName,
            EventStatus eventStatus,
            @DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME) LocalDateTime eventStartDatetime,
            @DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME) LocalDateTime eventEndDatetime
    ){
//        throw new GeneralException("테스트 메시지");  // -> APIExceptionHandler

        // EventService에서 getEvents를 해서 검색을 함 -> 결과물로 event dto의 list가 나옴
        // -> response가 필요하기 때문에 dto를 stream 적용해서 map을 시켜줌
        // 기존 dto를 다 response 형태로 맵핑해서 바꿔줌 -> 그걸 다시 list로 모아서 출력
        List<EventResponse> responses = eventService.getEvents(
                placeId, eventName, eventStatus, eventStartDatetime, eventEndDatetime)
                .stream().map(EventResponse::from).toList();
        
        return APIDataResponse.of(responses);
    }

    // @Valid 방식 검증
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/events")
    public APIDataResponse<String> createEvent(@Valid @RequestBody EventRequest eventRequest){
        boolean result = eventService.createEvent(eventRequest.toDTO());
        return APIDataResponse.of(Boolean.toString(result));
    }

    @GetMapping("/events/{eventId}")
    public APIDataResponse<EventResponse> getEvent(@Positive @PathVariable Long eventId) {
        EventResponse eventResponse = EventResponse.from(eventService.getEvent(eventId).orElse(null));

        return APIDataResponse.of(eventResponse);
    }

    @PutMapping("/events/{eventId}")
    public APIDataResponse<Void> modifyEvent(
            @PathVariable Long eventId,
            @RequestBody EventRequest eventRequest
    ){
        return APIDataResponse.empty();
    }

    @DeleteMapping("/events/{eventId}")
    public APIDataResponse<Void> removeEvent(@PathVariable Long eventId){
        return APIDataResponse.empty();
    }


}
