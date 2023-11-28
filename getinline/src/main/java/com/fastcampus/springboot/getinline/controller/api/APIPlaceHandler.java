package com.fastcampus.springboot.getinline.controller.api;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.function.HandlerFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import java.net.URI;
import java.util.List;

import static org.springframework.web.servlet.function.ServerResponse.created;

// funtional interface HandlerFunction을 implements 해서 메소드 골격을 만들어줌 
// (골격 만드는용으로 쓰고 지움)
@Component
public class APIPlaceHandler{

    public ServerResponse getPlaces(ServerRequest request){
        return ServerResponse.ok().body(List.of("place1", "place2"));
    }


    public ServerResponse createPlace(ServerRequest request){
        return created(URI.create("/api/places/1")).body(true);
        // TODO : 1은 구현을 할 때 제대로 넣어주자
    }

    public ServerResponse getPlace(ServerRequest request){
        return ServerResponse.ok().body("place" + request.pathVariable("placeId"));
    }

    public ServerResponse modifyPlace(ServerRequest request){
        return ServerResponse.ok().body(true);
    }

    public ServerResponse removePlace(ServerRequest request){
        return ServerResponse.ok().body(true);
    }
}
