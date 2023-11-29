package com.fastcampus.springboot.getinline.constant;


import com.fastcampus.springboot.getinline.exception.GeneralException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Predicate;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    OK(0, ErrorCategory.NORMAL, "Ok"),

    BAD_REQUEST(10000, ErrorCategory.CLIENT_SIDE, "Bad request"),
    SPRING_BAD_REQUEST(10001, ErrorCategory.CLIENT_SIDE, "Spring-detected bad request"),
    VALIDATION_ERROR(10002, ErrorCategory.CLIENT_SIDE, "validation error"),
    // bad request 중에서도 spring이 발생시킨 것만 따로

    INTERNAL_ERROR(20000, ErrorCategory.SERVER_SIDE, "Internal error"),
    SPRING_INTERNAL_ERROR(20001, ErrorCategory.SERVER_SIDE,"Spring-detected internal error"),
    DATA_ACCESS_ERROR(20002, ErrorCategory.SERVER_SIDE, "Data Access Error")
    ;

    private final Integer code;
    private final ErrorCategory errorCategory;
    private final String message;


    // 예외가 있을 경우 예외 메세지
    public String getMessage(Exception e) {
        return this.getMessage(this.getMessage() + " - " + e.getMessage());
    }

    // 메세지를 사용자가 직접 입력하는 경우
    public String getMessage(String message) {
        return Optional.ofNullable(message)
                .filter(Predicate.not(String::isBlank))
                .orElse(this.getMessage());
    }

    public boolean isClientSideError(){
        return this.getErrorCategory() == ErrorCategory.CLIENT_SIDE;
   }

    public boolean isServerSideError(){
        return this.getErrorCategory() == ErrorCategory.SERVER_SIDE;
    }

    @Override
    public String toString() {
        return String.format("%s (%d)", this.name(), this.getCode());
    }

    public enum ErrorCategory{
        NORMAL, CLIENT_SIDE, SERVER_SIDE
    }

}