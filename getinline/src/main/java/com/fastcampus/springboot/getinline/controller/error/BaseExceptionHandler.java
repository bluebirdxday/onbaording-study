package com.fastcampus.springboot.getinline.controller.error;

import com.fastcampus.springboot.getinline.constant.ErrorCode;
import com.fastcampus.springboot.getinline.exception.GeneralException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@ControllerAdvice
// => 전체 컨트롤러의 등작을 감시
// View에 대한 컨트롤 어드바이스
public class BaseExceptionHandler {

    // general exception이 터졌을 경우
    @ExceptionHandler
    public ModelAndView general(GeneralException e){
        ErrorCode errorCode = e.getErrorCode();
        HttpStatus status = errorCode.isClientSideError() ?
                HttpStatus.BAD_REQUEST :
                HttpStatus.INTERNAL_SERVER_ERROR;

        return new ModelAndView(
                "error",
                Map.of(
                    "statusCode", status.value(),
                    "errorCode", errorCode,
                    "message" , errorCode.getMessage(e)
                ), status
        );
    }

    // 전체적으로 에러가 터졌을 경우
    @ExceptionHandler
    public ModelAndView exception(Exception e){
        ErrorCode errorCode = ErrorCode.INTERNAL_ERROR;
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        return new ModelAndView(
                "error",
                Map.of(
                    "statusCode", status.value(),
                    "errorCode", errorCode,
                    "message" , errorCode.getMessage(e)
                ), status
        );
    }
}
