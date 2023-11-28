package com.fastcampus.springboot.getinline.dto;

import com.fastcampus.springboot.getinline.constant.ErrorCode;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


// TDD를 위한 테스트
// APIDataResponse 클래스는 Spring이 사용된 부분이 없으므로 독립적으로 설계된 데이터 클래스
// -> 굳이 Spring을 로드할 필요가 X
class APIDataResponseTest {

    @DisplayName("문자열 데이터가 주어지면, 표준 성공 응답을 생성한다.")
    @Test
    void givenStringData_whenCreatingResponse_thenReturnsSuccessfulResponse(){
        // Given
        String data = "Test data";

        // When
        APIDataResponse<String> response = APIDataResponse.of(data);

        // Then
        Assertions.assertThat(response)
                .hasFieldOrPropertyWithValue("success", true)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.OK.getCode())
                .hasFieldOrPropertyWithValue("message", ErrorCode.OK.getMessage())
                .hasFieldOrPropertyWithValue("data", data);
        // 메소드 체이닝 방식으로 테스트할 수 있어서 assertThat 선호
        // response 안에 어떤 데이터가 어떤 필드명으로 들어와야 되는지를 표시
        // APIDataResponse 디자인이 테스크 코드에 잘 드러남

    }

    @DisplayName("데이터가 없을 떄, 비어있는 표준 성공 응답을 생성한다.")
    @Test
    void givenNothing_whenCreatingResponse_thenReturnsEmptySuccessfulResponse(){
        // Given

        // When
//        APIDataResponse<String> response = APIDataResponse.of(null);
        // 데이터가 비어있는 경우 위 말고 아래처럼 표현하고 싶어~
        APIDataResponse<String> response = APIDataResponse.empty();

        // Then
        Assertions.assertThat(response)
                .hasFieldOrPropertyWithValue("success", true)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.OK.getCode())
                .hasFieldOrPropertyWithValue("message", ErrorCode.OK.getMessage())
                .hasFieldOrPropertyWithValue("data", null);
        // 메소드 체이닝 방식으로 테스트할 수 있어서 assertThat 선호
        // response 안에 어떤 데이터가 어떤 필드명으로 들어와야 되는지를 표시
        // APIDataResponse 디자인이 테스크 코드에 잘 드러남

    }
}