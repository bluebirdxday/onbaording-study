package com.fastcampus.springboot.getinline.controller.error;

import com.fastcampus.springboot.getinline.constant.ErrorCode;
import com.fastcampus.springboot.getinline.dto.APIErrorResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

// 기본 에러 페이지 연동
@Controller

public class BaseErrorController implements ErrorController {

    // text-html을 accept 하도록 가지고 있는 것만 잡음 => view에 대응
    @RequestMapping(path = "/error", produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView errorHtml(HttpServletResponse response){

        HttpStatus status = HttpStatus.valueOf(response.getStatus());
        ErrorCode errorCode = status.is4xxClientError() ? ErrorCode.BAD_REQUEST : ErrorCode.INTERNAL_ERROR;

        return new ModelAndView(
                "error",
                Map.of(
                        "statusCode", status.value(),
                        "errorCode", errorCode,
                        "message", errorCode.getMessage(status.getReasonPhrase())
                                                // http status 코드는 각각 대응하는 메세지가 있음(일어난 이유) 
                                                // 이 내용이 없으면 기본 error 출력
                ),
                status);    // model and view가 갖는 status
    }

    // json body로 반환 , api 호출에 대응
    @RequestMapping("/error")
    public ResponseEntity<APIErrorResponse> error(HttpServletResponse response){

        HttpStatus status = HttpStatus.valueOf(response.getStatus());
        ErrorCode errorCode = status.is4xxClientError() ? ErrorCode.BAD_REQUEST : ErrorCode.INTERNAL_ERROR;

        return ResponseEntity
                .status(status)
                .body(APIErrorResponse.of(
                        false,
                        errorCode
                ));
    }
}
