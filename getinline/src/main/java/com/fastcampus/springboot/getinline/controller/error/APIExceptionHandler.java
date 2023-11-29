package com.fastcampus.springboot.getinline.controller.error;

import com.fastcampus.springboot.getinline.constant.ErrorCode;
import com.fastcampus.springboot.getinline.dto.APIErrorResponse;
import com.fastcampus.springboot.getinline.exception.GeneralException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Map;

@RestControllerAdvice(annotations = RestController.class) // -> 이를 잡는 대상은 전체 범위가 아니라 rest controller 애노테이션을 쓰는 것만을 대상으로 함
                                                            // api만 대상이 됨 view들은 영향을 받지 않게 됨
// => 여기에 있는 모든 응답은 response body 애노테이션이 추가로 붙는 게 됨
// 꼭 필요 없긴 함
//  response body는 response entity를 응답으로 쓸 건데 status를 직접 제어하고 싶어서..ㅇㅇ
// entity를 응답으로 주게되면 response body는 생략해도 됨 
// 그냥 의미적인 부분.. @RestControllerAdvice 이게 아니면 안되는 게 아님 @ControllerAdvice랑 똑같이 동작

// API에 대한 컨트롤 어드바이스
public class APIExceptionHandler extends ResponseEntityExceptionHandler {   
                    // spring web에서 발생하는 에러들을 처리하기 위해 상속받음 -> 그쪽에서 발생하는 에러를 알아서 처리함

    @ExceptionHandler
    // ConstraintViolationException => 스프링이 알아서 처리해주지 않기 때문에 직접 exception handler 만들어줌
    public ResponseEntity<Object> validation(ConstraintViolationException e, WebRequest request){

        ErrorCode errorCode = ErrorCode.VALIDATION_ERROR;
        HttpStatus status = HttpStatus.BAD_REQUEST;

        // 부모 클래스에 넘겨줌
        return super.handleExceptionInternal(
                e,
                APIErrorResponse.of(false, errorCode.getCode(), errorCode.getMessage(e)),
                HttpHeaders.EMPTY,
                status,
                request
        );

    }



    // general exception이 터졌을 경우 (프로젝트에서 발생시키는 일반 오류)
//    @ExceptionHandler
//    public ResponseEntity<APIErrorResponse> general(GeneralException e){
//
//        ErrorCode errorCode = e.getErrorCode();
//        HttpStatus status = errorCode.isClientSideError()?
//                HttpStatus.BAD_REQUEST :
//                HttpStatus.INTERNAL_SERVER_ERROR;
//
//        return ResponseEntity
//                .status(status)
//                .body(APIErrorResponse.of(
//                        false, errorCode, errorCode.getMessage(e)
//                ));
//    }
    @ExceptionHandler
    public ResponseEntity<Object> general(GeneralException e, WebRequest request){

        ErrorCode errorCode = e.getErrorCode();
        HttpStatus status = errorCode.isClientSideError()?
                HttpStatus.BAD_REQUEST :
                HttpStatus.INTERNAL_SERVER_ERROR;

        return super.handleExceptionInternal(
                e,
                APIErrorResponse.of(false, errorCode.getCode(), errorCode.getMessage(e)),
                HttpHeaders.EMPTY,
                status,
                request
        );
    }


    // 전체적으로 에러가 터졌을 경우
    // 우리가 예상하지 못한 일반적인 에러를 잡는 부분이라 전부 인터널 에러라고 확신하고 세팅해준 것
    @ExceptionHandler
    public ResponseEntity<Object> exception(Exception e, WebRequest request){

        ErrorCode errorCode = ErrorCode.INTERNAL_ERROR;
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        return super.handleExceptionInternal(
                e,
                APIErrorResponse.of(false, errorCode.getCode(), errorCode.getMessage(e)),
                HttpHeaders.EMPTY,
                status,
                request
        );
    }

    // 상속 받은 것만으로 알아서 오류를 잘 처리하지만 다른 핸들러처럼 json 형태로 반환시키기 위해 재정의함
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
        ErrorCode errorCode = statusCode.is4xxClientError()?
                ErrorCode.SPRING_BAD_REQUEST :
                ErrorCode.SPRING_INTERNAL_ERROR;
        return super.handleExceptionInternal(
                ex,
                APIErrorResponse.of(false, errorCode.getCode(), errorCode.getMessage(ex)),
                headers,
                statusCode,
                request
        );
    }



    private ResponseEntity<Object> handleExceptionInternal(Exception e, ErrorCode errorCode, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return super.handleExceptionInternal(
                e,
                APIErrorResponse.of(false, errorCode.getCode(), errorCode.getMessage(e)),
                headers,
                status,
                request
        );
    }
}
