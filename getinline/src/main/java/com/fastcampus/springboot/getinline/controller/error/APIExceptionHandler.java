package com.fastcampus.springboot.getinline.controller.error;

import com.fastcampus.springboot.getinline.constant.ErrorCode;
import com.fastcampus.springboot.getinline.dto.APIErrorResponse;
import com.fastcampus.springboot.getinline.exception.GeneralException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@RestControllerAdvice(annotations = RestController.class) // -> 이를 잡는 대상은 전체 범위가 아니라 rest controller 애노테이션을 쓰는 것만을 대상으로 함
                                                            // api만 대상이 됨 view들은 영향을 받지 않게 됨
// => 여기에 있는 모든 응답은 response body 애노테이션이 추가로 붙는 게 됨
// 꼭 필요 없긴 함
//  response body는 response entity를 응답으로 쓸 건데 status를 직접 제어하고 싶어서..ㅇㅇ
// entity를 응답으로 주게되면 response body는 생략해도 됨 
// 그냥 의미적인 부분.. @RestControllerAdvice 이게 아니면 안되는 게 아님 @ControllerAdvice랑 똑같이 동작

// API에 대한 컨트롤 어드바이스
public class APIExceptionHandler {

    // general exception이 터졌을 경우
    @ExceptionHandler
    public ResponseEntity<APIErrorResponse> general(GeneralException e){

        ErrorCode errorCode = e.getErrorCode();
        HttpStatus status = errorCode.isClientSideError()?
                HttpStatus.BAD_REQUEST :
                HttpStatus.INTERNAL_SERVER_ERROR;

        return ResponseEntity
                .status(status)
                .body(APIErrorResponse.of(
                        false, errorCode, errorCode.getMessage(e)
                ));
    }

    // 전체적으로 에러가 터졌을 경우
    @ExceptionHandler
    public ResponseEntity<APIErrorResponse> exception(Exception e){

        ErrorCode errorCode = ErrorCode.INTERNAL_ERROR;
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        return ResponseEntity
                .status(status)
                .body(APIErrorResponse.of(
                        false, errorCode, errorCode.getMessage(e)
                ));
    }
}
