package com.fastcampus.springboot.getinline.controller.api;

import com.fastcampus.springboot.getinline.constant.ErrorCode;
import com.fastcampus.springboot.getinline.constant.PlaceType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(APIPlaceController.class)
class APIPlaceControllerTest {

    private final MockMvc mvc;

    public APIPlaceControllerTest(@Autowired MockMvc mvc){
        this.mvc = mvc;
    }

    @DisplayName("[API][GET] 장소 리스트 조회")
    @Test
    void givenNothing_whenRequestingPlaces_thenReturnsListOfPlacesInStandardResponse() throws Exception{
        // Given

        // When
        // Then
        mvc.perform(MockMvcRequestBuilders.get("/api/places"))
                .andExpect(status().isOk()) // 200 떨어졌는지
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)) // MediaType에 application.JSON이 들어갔는지
                .andExpect(jsonPath("$.data").isArray())    // json path로 데이터가 array 형태로 사용 되었는지
                .andExpect(jsonPath("$.data[0].placeType").value(PlaceType.COMMON.name()))  // 첫번째 원소가 필요한 필드들을 충족시키고 있는지
                .andExpect(jsonPath("$.data[0].placeName").value("필라배드민턴장"))
                .andExpect(jsonPath("$.data[0].address").value("강남구"))
                .andExpect(jsonPath("$.data[0].phoneNumber").value("010-1234-5677"))
                .andExpect(jsonPath("$.data[0].capacity").value(30))
                .andExpect(jsonPath("$.data[0].memo").value("신장개업"))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.errorCode").value(ErrorCode.OK.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.OK.getMessage()));
        // -> 정상 케이스에 대한 테스트

    }

    @DisplayName("[API][GET] 단일 장소 조회 - 장소 있는 경우")
    @Test
    void givenPlaceAndItsId_whenRequestingPlace_thenReturnsPlaceInStandardResponse() throws Exception{

        // Given
        int placeId = 1;

        // When


        // Then
        // 데이터가 조회를 해봤는데 있는 경우 없는 경우를 가짜로 만들어봄
        mvc.perform(MockMvcRequestBuilders.get("/api/places/" + placeId))
                .andExpect(status().isOk()) // 200 떨어졌는지
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)) // MediaType에 application.JSON이 들어갔는지
                .andExpect(jsonPath("$.data").isMap())    // json path로 데이터가 array 형태로 사용 되었는지
                .andExpect(jsonPath("$.data.placeType").value(PlaceType.COMMON.name()))  // 첫번째 원소가 필요한 필드들을 충족시키고 있는지
                .andExpect(jsonPath("$.data.placeName").value("필라배드민턴장"))
                .andExpect(jsonPath("$.data.address").value("강남구"))
                .andExpect(jsonPath("$.data.phoneNumber").value("010-1234-5677"))
                .andExpect(jsonPath("$.data.capacity").value(30))
                .andExpect(jsonPath("$.data.memo").value("신장개업"))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.errorCode").value(ErrorCode.OK.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.OK.getMessage()));
    }

    @DisplayName("[API][GET] 단일 장소 조회 - 장소 없는 경우")
    @Test
    void givenPlaceId_whenRequestingPlace_thenReturnsEmptyStandardResponse() throws Exception{

        // Given
        int placeId = 2;

        // When


        // Then
        // 데이터가 조회를 해봤는데 있는 경우 없는 경우를 가짜로 만들어봄
        mvc.perform(MockMvcRequestBuilders.get("/api/places/" + placeId))
                .andExpect(status().isOk()) // 200 떨어졌는지
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.errorCode").value(ErrorCode.OK.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.OK.getMessage()));
    }
}