package com.fastcampus.springboot.getinline.service;

import com.fastcampus.springboot.getinline.constant.ErrorCode;
import com.fastcampus.springboot.getinline.constant.EventStatus;
import com.fastcampus.springboot.getinline.dto.EventDTO;
import com.fastcampus.springboot.getinline.exception.GeneralException;
import com.fastcampus.springboot.getinline.repository.EventRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {


    @InjectMocks   // -> 목으로 주입
    private EventService sut;

    @Mock 
    private EventRepository eventRepository;
    // -> 우리가 테스트하고자하는 서비스(sut)에다가 주입해줌 
    // -> injectMock이 붙은 대상에다 주입해주는 것


    @DisplayName("검색 조건 없이 이벤트 검색하면, 전체 결과를 출력하여 보여준다.")
    @Test
    void givenNothing_whenSearchingEvents_thenReturnsEntireEventList(){
        // Given
        given(eventRepository.findEvents(null, null, null, null, null))
                .willReturn(List.of(
                        createEventDTO(1L, "오전 운동", true),
                        createEventDTO(1L, "오후 운동", false)
                ));
        // 모킹을 하려고 하는 것 eventRepository.findEvents (이걸 사용해야 한다)
        // 검색 조건은 없으니까 전부 null로 넣어줌 이때 데이터 2개 보내주기
        // -> 이로써 우리가 이벤트 리포지토리에 실제 데이터가 나오는 것처럼 조작을 할 필요가 없음
        // 아직 jpa 같은 걸 만들거나 외부 저장소에 연결하는 구현을 하지 못한 상태인데
        // 모킹을 통해서 테스트가 가능하게 되는 것


        // When
        List<EventDTO> list = sut.getEvents(null, null, null, null, null);

        // Then
        assertThat(list).hasSize(2);
        then(eventRepository).should().findEvents(null, null, null, null, null);
        // verify(eventRepository).findEvents(null, null, null, null, null); -> 위와 동일한 동작을 함
    }

    @DisplayName("이벤트를 검색하는데 에러가 발생한 경우, 줄서기 프로젝트 기본 에러로 전환하여 예외 던진다.")
    @Test
    void givenDataRelatedException_whenSearchingEvents_thenThrowsGeneralException(){
        // Given
        RuntimeException e = new RuntimeException("This is test");
        given(eventRepository.findEvents(any(), any(), any(), any(), any())).willThrow(e);
        // 이벤트 리포지토리에서 던지는 예외 불분명 -> 최상의 Exception 던져서 시뮬레이션

        // When
        Throwable thrown = catchThrowable(()->{
                sut.getEvents(null, null, null, null, null);
        });


        // Then
        assertThat(thrown)
                .isInstanceOf(GeneralException.class)
                        .hasMessageContaining(ErrorCode.DATA_ACCESS_ERROR.getMessage());

        then(eventRepository).should().findEvents(any(), any(), any(), any(), any());

    }
    

    @DisplayName("검색 조건과 함께 이벤트 검색하면, 검색된 결과를 출력하여 보여준다.")
    @Test
    void givenSearchParams_whenSearchingEvents_thenReturnsEventList(){
        // Given
        Long placeId = 1L;
        String eventName = "오전 운동";
        EventStatus eventStatus = EventStatus.OPENED;
        LocalDateTime eventStartDatetime = LocalDateTime.of(2021, 1, 1, 0, 0, 0);
        LocalDateTime eventEndDatetime = LocalDateTime.of(2021, 1, 2, 0 ,0 ,0);

        given(eventRepository.findEvents(placeId, eventName, eventStatus, eventStartDatetime, eventEndDatetime))
                .willReturn(List.of(
                        createEventDTO(1L, "오전 운동", eventStatus, eventStartDatetime, eventEndDatetime)
                ));
        //  -> 검색은 하나만 걸리는 것처럼 연출해서 넣어줌

        // When
        List<EventDTO> list = sut.getEvents(placeId, eventName, eventStatus, eventStartDatetime, eventEndDatetime);

        // Then
        assertThat(list)
                .hasSize(1)     // 결과가 하나 나올 거라고 예상하고 있지만
                .allSatisfy(event->{
                    assertThat(event) // 결과가 n개 나올 거라고 생각하고 코드를 보기, assertion을 다 수행한다 이해하기
                            .hasFieldOrPropertyWithValue("placeId", placeId)
                            .hasFieldOrPropertyWithValue("eventName", eventName)
                            .hasFieldOrPropertyWithValue("eventStatus", eventStatus);
                    assertThat(event.eventStartDatetime()).isAfterOrEqualTo(eventStartDatetime);
                            // 내가 검색한 내용의 시작 시간이 더 뒤에 있거나 혹은 같은지 검색하는 것
                    assertThat(event.eventStartDatetime
                                ()).isBeforeOrEqualTo(eventStartDatetime);

                });
        
        then(eventRepository).should().findEvents(placeId, eventName, eventStatus, eventStartDatetime, eventEndDatetime);
        // -> 이 부분이 일치하지 않으면 테스트 실패 -> 그걸 검증하기 위해서 이렇게 만듦
    }


    @DisplayName("이벤트 ID로 존재하는 이벤트를 조회하면, 해당 이벤트 정보를 출력하여 보여준다.")
    @Test
    void givenEventId_whenSearchingExistingEvent_thenReturnsEvent() {
        // Given
        long eventId = 1L;
        EventDTO eventDTO = createEventDTO(1L, "오전 운동", true);

        given(eventRepository.findEvent(eventId)).willReturn(Optional.of(eventDTO));


        // When
        Optional<EventDTO> result = sut.getEvent(eventId);

        // Then
        assertThat(result).hasValue(eventDTO);
        then(eventRepository).should().findEvent(eventId);
    }


    @DisplayName("이벤트 ID로 이벤트를 조회하면, 빈 정보를 출력하여 보여준다.")
    @Test
    void givenEventId_whenSearchingNonexistentEvent_thenReturnsEmptyOptional() {
        // Given
        long eventId = 2L;

        given(eventRepository.findEvent(eventId)).willReturn(Optional.empty());

        // When
        Optional<EventDTO> result = sut.getEvent(eventId);

        // Then
        assertThat(result).isEmpty();
        then(eventRepository).should().findEvent(eventId);
    }

    @DisplayName("이벤트 정보를 주면, 이벤트를 생성하고 결과를 true 로 보여준다.")
    @Test
    void givenEvent_whenCreating_thenCreatesEventAndReturnsTrue() {
        // Given
        EventDTO dto = createEventDTO(1L, "오후 운동", false);
        // -> 모킹을 이용해서 dto가 있는 경우(만들려는 데이터가 있는 경우) test double 사용
        given(eventRepository.insertEvent(dto)).willReturn(true);
        // 테스트 스토리를 만듦

        // When
        boolean result = sut.createEvent(dto);

        // Then
        assertThat(result).isTrue();
        then(eventRepository).should().insertEvent(dto);
    }

    @DisplayName("이벤트 정보를 주지 않으면, 생성 중단하고 결과를 false 로 보여준다.")
    @Test
    void givenNothing_whenCreating_thenAbortCreatingAndReturnsFalse() {
        // Given
        given(eventRepository.insertEvent(null)).willReturn(false);

        // When
        boolean result = sut.createEvent(null);

        // Then
        assertThat(result).isFalse();
        then(eventRepository).should().insertEvent(null);
    }


    @DisplayName("이벤트 ID와 정보를 주면, 이벤트 정보를 변경하고 결과를 true 로 보여준다.")
    @Test
    void givenEventIdAndItsInfo_whenModifying_thenModifiesEventAndReturnsTrue() {
        // Given
        long eventId = 1L;
        EventDTO dto = createEventDTO(1L, "오후 운동", false);
        given(eventRepository.updateEvent(eventId, dto)).willReturn(true);


        // When
        boolean result = sut.modifyEvent(eventId, dto);

        // Then
        assertThat(result).isTrue();
        then(eventRepository).should().updateEvent(eventId, dto);
    }

    @DisplayName("이벤트 ID를 주지 않으면, 이벤트 정보 변경 중단하고 결과를 false 로 보여준다.")
    @Test
    void givenNoEventId_whenModifying_thenAbortModifyingAndReturnsFalse() {
        // Given
        EventDTO dto = createEventDTO(1L, "오후 운동", false);
        given(eventRepository.updateEvent(null, dto)).willReturn(false);

        // When
        boolean result = sut.modifyEvent(null, dto);

        // Then
        assertThat(result).isFalse();
        then(eventRepository).should().updateEvent(null, dto);
    }

    // 여기서는 정책이 적용되는데
    // 1. 변경은 시도하고 아무것도 안 바뀌었는데 일은 잘 했으니까 true 내보내기
    // 2. 입력 자체가 잘못됐다고 보고 변경 시도 자체를 하지 않고 못했다 실패다 라고 false 내보내기 => 채택
    @DisplayName("이벤트 ID만 주고 변경할 정보를 주지 않으면, 이벤트 정보 변경 중단하고 결과를 false 로 보여준다.")
    @Test
    void givenEventIdOnly_whenModifying_thenAbortModifyingAndReturnsFalse() {
        // Given
        long eventId = 1L;
        given(eventRepository.updateEvent(eventId, null)).willReturn(false);

        // When
        boolean result = sut.modifyEvent(eventId, null);

        // Then
        assertThat(result).isFalse();
        then(eventRepository).should().updateEvent(eventId, null);
    }


    @DisplayName("이벤트 ID를 주면, 이벤트 정보를 삭제하고 결과를 true 로 보여준다.")
    @Test
    void givenEventId_whenDeleting_thenDeletesEventAndReturnsTrue() {
        // Given
        long eventId = 1L;
        given(eventRepository.deleteEvent(eventId)).willReturn(true);

        // When
        boolean result = sut.removeEvent(eventId);

        // Then
        assertThat(result).isTrue();
        then(eventRepository).should().deleteEvent(eventId);
    }

    @DisplayName("이벤트 ID를 주지 않으면, 삭제 중단하고 결과를 false 로 보여준다.")
    @Test
    void givenNothing_whenDeleting_thenAbortsDeletingAndReturnsFalse() {
        // Given
        given(eventRepository.deleteEvent(null)).willReturn(false);

        // When
        boolean result = sut.removeEvent(null);

        // Then
        assertThat(result).isFalse();
        then(eventRepository).should().deleteEvent(null);
    }



    // 데이터를 계속해서 세팅하게 될 건데 세팅해야되는 DTO 사이즈가 큼 
    // -> 꼭 내가 비교할 때 사용하고 싶은 부분만 파라미터로 뽑아내서 집어넣음
    // -> 반복적인 DTO 만드는 로직의 코드 양을 줄일 수 있게 해줌
    private EventDTO createEventDTO(long placeId, String eventName, boolean isMorning) {
        String hourStart = isMorning ? "09" : "13";
        String hourEnd = isMorning ? "12" : "16";

        return createEventDTO(
                placeId,
                eventName,
                EventStatus.OPENED,
                LocalDateTime.parse("2021-01-01T%s:00:00".formatted(hourStart)),
                LocalDateTime.parse("2021-01-01T%s:00:00".formatted(hourEnd))
        );
    }


    private EventDTO createEventDTO(
            long placeId,
            String eventName,
            EventStatus eventStatus,
            LocalDateTime eventStartDateTime,
            LocalDateTime eventEndDateTime
    ) {
        return EventDTO.of(
                placeId,
                eventName,
                eventStatus,
                eventStartDateTime,
                eventEndDateTime,
                0,
                24,
                "마스크 꼭 착용하세요",
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

}