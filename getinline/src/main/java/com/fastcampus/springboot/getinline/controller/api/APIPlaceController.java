package com.fastcampus.springboot.getinline.controller.api;

import com.fastcampus.springboot.getinline.constant.PlaceType;
import com.fastcampus.springboot.getinline.dto.APIDataResponse;
import com.fastcampus.springboot.getinline.dto.APIErrorResponse;
import com.fastcampus.springboot.getinline.dto.PlaceDTO;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.util.List;

@RequestMapping("/api")
@RestController
public class APIPlaceController {

    @GetMapping("/places")
    public APIDataResponse<List<PlaceDTO>> getPlaces(){
        return APIDataResponse.of(List.of(PlaceDTO.of(
                PlaceType.COMMON,
                "필라배드민턴장",
                "강남구",
                "010-1234-5677",
                30,
                "신장개업"
        )));
    }

    @PostMapping("/places")
    public Boolean createPlace(){
        return true;
    }

    @GetMapping("/places/{placeId}")
    public APIDataResponse<PlaceDTO> getPlace(@PathVariable Integer placeId){

        // 테스트를 위해 임시로 만듦
        if(placeId.equals(2)){
            return APIDataResponse.of(null);
        }

        return APIDataResponse.of(PlaceDTO.of(
                PlaceType.COMMON,
                "필라배드민턴장",
                "강남구",
                "010-1234-5677",
                30,
                "신장개업"
        ));
    }

    @PutMapping("/places/{placeId}")
    public Boolean modifyPlace(@PathVariable Integer placeId){
        return true;
    }

    @DeleteMapping("/places/{placeId}")
    public Boolean removePlace(@PathVariable Integer placeId){
        return true;
    }

}
