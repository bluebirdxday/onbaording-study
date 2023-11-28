package com.fastcampus.springboot.getinline.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// Mock MVC에 대한 설정 인스턴스를 만들고 설정을 자동으로 해주는 것
//@AutoConfigureMockMvc
//@SpringBootTest
@WebMvcTest(BaseController.class)
// -> 이를 사용하면 MockMvc를 사용하기 위해서 @AutoConfigureMockMvc 이걸 쓰지 않아도 됨
class BaseControllerTest {

    private final MockMvc mvc;

    public BaseControllerTest(@Autowired MockMvc mvc){
        this.mvc = mvc;
    }

    @DisplayName("[view][GET] 기본 페이지 요청")
    @Test
    void testRoot() throws Exception{
        // Given

        // When
        // Then
        mvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().string(containsString("기본 페이지")))
                .andExpect(view().name("index"))    // index 페이지를 맵핑하고 싶은 것
                .andDo(print());    // 에러 여부에 상관 없이 무조건 볼 수 있음 => 초기에 내가 테스트를 잘 하고 있나 볼 때는 유용

    }
}