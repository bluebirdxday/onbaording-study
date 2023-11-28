package com.fastcampus.springboot.getinline.controller.api;

import com.fastcampus.springboot.getinline.constant.ErrorCode;
import com.fastcampus.springboot.getinline.dto.APIErrorResponse;
import com.fastcampus.springboot.getinline.exception.GeneralException;
import com.fastcampus.springboot.getinline.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class APIEventController {

    private final EventService eventService;

    @GetMapping("/events")
    public List<String> getEvents(){
//        throw new GeneralException("테스트 메시지");  // -> APIExceptionHandler
        return List.of("event1", "event2");
    }

    @PostMapping("/events")
    public Boolean createEvent(){
        return true;
    }

    @GetMapping("/events/{eventId}")
    public String getEvent(@PathVariable Integer eventId){
//        throw new RuntimeException("runtime 테스트 메시지");  // -> APIExceptionHandler
        return "event" + eventId;
    }

    @PutMapping("/events/{eventId}")
    public Boolean modifyEvent(@PathVariable Integer eventId){
        return true;
    }

    @DeleteMapping("/events/{eventId}")
    public Boolean removeEvent(@PathVariable Integer eventId){
        return true;
    }


}
