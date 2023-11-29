package com.fastcampus.springboot.getinline.constant;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ErrorCodeTest {

    @ParameterizedTest(name="[{index} {0} => {1}]")
    @MethodSource
    @DisplayName("예외를 받으면, 예외 메세지가 포함된 메세지 출력")
    void givenExceptionWithMessage_whenGettingMessage_thenReturnsMessage(ErrorCode sut, String expected){
        // Given
        Exception e = new Exception("This is test message");

        // When
        String actual = sut.getMessage(e);

        // Then
        Assertions.assertThat(actual).isEqualTo(expected);
    }


    @ParameterizedTest(name="[{index} \"{0}\" => {1}]")
    @MethodSource
    @DisplayName("에러 메세지를 받으면, 해당 에러 메세지로 출력")
    void givenMessage_whenGettingMessage_thenReturnsMessage(String input, String expected){
        // Given

        // When
        String actual = ErrorCode.INTERNAL_ERROR.getMessage(input);

        // Then
        Assertions.assertThat(actual).isEqualTo(expected);
    }


    static Stream<Arguments> givenExceptionWithMessage_whenGettingMessage_thenReturnsMessage(){
        return Stream.of(
                    // arguments(input, output)
                Arguments.arguments(ErrorCode.OK, "Ok - This is test message"),
                Arguments.arguments(ErrorCode.BAD_REQUEST, "Bad request - This is test message"),
                Arguments.arguments(ErrorCode.SPRING_BAD_REQUEST, "Spring-detected bad request - This is test message"),
                Arguments.arguments(ErrorCode.VALIDATION_ERROR, "validation error - This is test message"),
                Arguments.arguments(ErrorCode.INTERNAL_ERROR, "Internal error - This is test message"),
                Arguments.arguments(ErrorCode.SPRING_INTERNAL_ERROR, "Spring-detected internal error - This is test message"),
                Arguments.arguments(ErrorCode.DATA_ACCESS_ERROR, "Data Access Error - This is test message")
        );
    }

    static Stream<Arguments> givenMessage_whenGettingMessage_thenReturnsMessage(){
        return Stream.of(
                Arguments.arguments(null, ErrorCode.INTERNAL_ERROR.getMessage()),
                Arguments.arguments("", ErrorCode.INTERNAL_ERROR.getMessage()),
                Arguments.arguments("   ", ErrorCode.INTERNAL_ERROR.getMessage()),
                Arguments.arguments("This is test message", ErrorCode.INTERNAL_ERROR.getMessage())
            );
    }

    @DisplayName("toString() 호출 포맷")
    @Test
    void givenNothing_whenToString_thenReturnsSimplifiedToString(){
        // Given

        // When
        String result = ErrorCode.INTERNAL_ERROR.toString();

        // Then
        Assertions.assertThat(result).isEqualTo("INTERNAL_ERROR (20000)");
    }



}