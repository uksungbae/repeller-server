package school.kku.repellingserver.repellent.repellentData.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.test.web.servlet.ResultActions;
import school.kku.repellingserver.common.BaseControllerTest;
import school.kku.repellingserver.gateway.dto.RepellentDataRequest;
import school.kku.repellingserver.repellent.repellentData.domain.DetectionType;
import school.kku.repellingserver.repellent.repellentData.dto.DailyDetectionListResponse;
import school.kku.repellingserver.repellent.repellentData.dto.DayByDetectionListResponse;
import school.kku.repellingserver.repellent.repellentData.dto.HourByDetectionListResponse;
import school.kku.repellingserver.repellent.repellentData.dto.MainPageDataResponse;
import school.kku.repellingserver.repellent.repellentData.dto.ReDetectionMinutesAndRepellentSoundResponse;
import school.kku.repellingserver.repellent.repellentData.service.RepellentDataService;

class RepellentDataControllerTest extends BaseControllerTest {

  @MockBean
  private RepellentDataService repellentDataService;

  @Test
  void 메인_페이지에_필요한_퇴치_데이터를_리턴한다() throws Exception {
    //given
    DayByDetectionListResponse response1 = DayByDetectionListResponse.of(
        LocalDate.of(2023, 8, 20),
        DetectionType.PIR,
        10
    );
    DayByDetectionListResponse response2 = DayByDetectionListResponse.of(
        LocalDate.of(2023, 8, 21),
        DetectionType.PIR,
        5
    );
    DayByDetectionListResponse response3 = DayByDetectionListResponse.of(
        LocalDate.of(2023, 8, 22),
        DetectionType.PIR,
        5
    );

    when(repellentDataService.getRepellentDataList(any()))
        .thenReturn(MainPageDataResponse.of(
            List.of(response1, response2, response3),
            13L,
            "소리명"
        ));

    //when
    ResultActions resultActions = mockMvc.perform(get(API + "/repellent-data/main")
        .param("farmId", "1")
    ).andDo(print());

    //then
    resultActions.andExpect(status().isOk())
        .andDo(document("repellent-data/main/success",
            responseFields(
                fieldWithPath("dayByDetectionList[].detectedAt").description("퇴치 데이터가 측정된 날짜"),
                fieldWithPath("dayByDetectionList[].detectionType").description("측정된 타입"),
                fieldWithPath("dayByDetectionList[].count").description("측정된 횟수"),
                fieldWithPath("reDetectionTimeAvg").description("재탐지 시간 평균"),
                fieldWithPath("repellentSoundName").description("퇴치기 소리명")
            )));

  }


  @Test
  void 게이트웨이에서_퇴치_데이터를_전달받는다() throws Exception {
    //given
    final RepellentDataRequest request = RepellentDataRequest.of(
        "gatewayId",
        "nodeId",
        "message",
        "soundType",
        3,
        LocalDateTime.now(),
        DetectionType.PIR,
        1
    );

    System.out.println(objectMapper.writeValueAsString(request));

    //when
    ResultActions resultActions = mockMvc.perform(post(API + "/repellent-data")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andDo(print());

    //then
    resultActions.andExpect(status().isOk())
        .andDo(document("repellent-data/success",
            requestFields(
                fieldWithPath("gatewayId").description("게이트웨이의 ID"),
                fieldWithPath("nodeId").description("노드의 ID"),
                fieldWithPath("message").description("메시지"),
                fieldWithPath("soundType").description("소리의 종류"),
                fieldWithPath("soundLevel").description("소리의 크기"),
                fieldWithPath("timestamp").description("데이터가 전송된 시간"),
                fieldWithPath("detectionType").description("감지된 타입"),
                fieldWithPath("detectedCount").description("감지된 횟수")
            )));
  }


  @Test
  void 상세페이지_농장퇴치현황_api() throws Exception {
    //given
    Long farmId = 1L;

    DayByDetectionListResponse res1 = DayByDetectionListResponse.of(
        LocalDate.of(2023, 8, 20),
        DetectionType.PIR,
        10
    );

    DayByDetectionListResponse res2 = DayByDetectionListResponse.of(
        LocalDate.of(2023, 8, 21),
        DetectionType.PIR,
        5
    );

    DayByDetectionListResponse res3 = DayByDetectionListResponse.of(
        LocalDate.of(2023, 8, 22),
        DetectionType.PIR,
        5
    );

    when(repellentDataService.getDayByDetectionList(farmId))
        .thenReturn(List.of(res1, res2, res3));
    //when
    ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.get(
            API + "/repellent-data/detail/group-farm/farm/{farmId}", farmId))
        .andDo(print());

    //then
    resultActions.andExpect(status().isOk())
        .andDo(
            document(
                "repellent-data/detail/group-farm/success",
                RequestDocumentation.pathParameters(
                    RequestDocumentation.parameterWithName("farmId").description("농장 ID")
                ),
                responseFields(
                    fieldWithPath("[].detectedAt").description("퇴치 데이터가 측정된 날짜"),
                    fieldWithPath("[].detectionType").description("측정된 타입"),
                    fieldWithPath("[].count").description("측정된 횟수")
                )
            )
        );
  }

  @Test
  void 상세페이지_시간별_퇴치현황() throws Exception {
    //given
    Long farmId = 1L;

    HourByDetectionListResponse res1 = HourByDetectionListResponse.of(
        LocalTime.of(8, 20, 0),
        DetectionType.PIR,
        10
    );

    HourByDetectionListResponse res2 = HourByDetectionListResponse.of(
        LocalTime.of(6, 21, 0),
        DetectionType.PIR,
        5
    );

    HourByDetectionListResponse res3 = HourByDetectionListResponse.of(
        LocalTime.of(4, 22, 0),
        DetectionType.PIR,
        5
    );

    when(repellentDataService.getDayByTimeList(farmId))
        .thenReturn(List.of(res1, res2, res3));
    //when
    ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.get(
            API + "/repellent-data/detail/group-time/farm/{farmId}", farmId))
        .andDo(print());

    //then
    resultActions.andExpect(status().isOk())
        .andDo(
            document(
                "repellent-data/detail/group-time/success",
                RequestDocumentation.pathParameters(
                    RequestDocumentation.parameterWithName("farmId").description("농장 ID")
                ),
                responseFields(
                    fieldWithPath("[].detectedAt").description("퇴치 데이터가 측정된 시간"),
                    fieldWithPath("[].detectionType").description("측정된 타입"),
                    fieldWithPath("[].count").description("측정된 횟수")
                )
            )
        );
  }

  @Test
  void 상세페이지_퇴치기기별_퇴치현황() throws Exception {
    //given
    Long farmId = 1L;

    DailyDetectionListResponse res1 = DailyDetectionListResponse.of(
        "1번기기",
        DetectionType.PIR.name(),
        12L
    );

    DailyDetectionListResponse res2 = DailyDetectionListResponse.of(
        "2번기기",
        DetectionType.PIR.name(),
        10L
    );

    DailyDetectionListResponse res3 = DailyDetectionListResponse.of(
        "3번기기",
        DetectionType.PIR.name(),
        8L
    );

    when(repellentDataService.getDailyByDetectionList(farmId))
        .thenReturn(List.of(res1, res2, res3));
    //when
    ResultActions resultActions = mockMvc.perform(
            RestDocumentationRequestBuilders.get(
                API + "/repellent-data/detail/group-detection-device/farm/{farmId}", farmId))

        //then
        .andDo(print());
    resultActions.andExpect(status().isOk())
        .andDo(
            document(
                "repellent-data/detail/group-detection-device/success",
                RequestDocumentation.pathParameters(
                    RequestDocumentation.parameterWithName("farmId").description("농장 ID")
                ),
                responseFields(
                    fieldWithPath("[].repellentDeviceName").description("퇴치기기 이름"),
                    fieldWithPath("[].detectionType").description("측정된 타입"),
                    fieldWithPath("[].detectionNumSum").description("측정된 횟수")
                )
            )
        );
  }

  @Test
  void 상세페이지_퇴치기기의_퇴치현황() throws Exception {
    //given
    Long detectionDeviceId = 1L;

    DayByDetectionListResponse res1 = DayByDetectionListResponse.of(
        LocalDate.of(2023, 8, 20),
        DetectionType.PIR,
        10
    );

    DayByDetectionListResponse res2 = DayByDetectionListResponse.of(
        LocalDate.of(2023, 8, 21),
        DetectionType.PIR,
        5
    );

    DayByDetectionListResponse res3 = DayByDetectionListResponse.of(
        LocalDate.of(2023, 8, 22),
        DetectionType.PIR,
        5
    );

    when(repellentDataService.getEachDayDetectionListByDetectionDeviceId(detectionDeviceId))
        .thenReturn(List.of(res1, res2, res3));
    //when
    ResultActions resultActions = mockMvc.perform(
            RestDocumentationRequestBuilders.get(
                API + "/repellent-data/detail/detection-device-id/{detectionDeviceId}",
                detectionDeviceId))
        .andDo(print());

    //then
    resultActions.andExpect(status().isOk())
        .andDo(
            document(
                "repellent-data/detail/detection-device-id/success",
                RequestDocumentation.pathParameters(
                    RequestDocumentation.parameterWithName("detectionDeviceId")
                        .description("퇴치기기 ID")
                ),
                responseFields(
                    fieldWithPath("[].detectedAt").description("퇴치 데이터가 측정된 날짜"),
                    fieldWithPath("[].detectionType").description("측정된 타입"),
                    fieldWithPath("[].count").description("측정된 횟수")
                )
            )
        );


  }

  @Test
  void 상세페이지_퇴치기기의_최근_퇴치소리와_재탐지시간을_반환한다() throws Exception {
    //given
    Long detectionDeviceId = 1L;

    ReDetectionMinutesAndRepellentSoundResponse res1 = ReDetectionMinutesAndRepellentSoundResponse.of(
        LocalTime.of(8, 20, 0),
        10,
        "빵야"
    );

    ReDetectionMinutesAndRepellentSoundResponse res2 = ReDetectionMinutesAndRepellentSoundResponse.of(
        LocalTime.of(8, 21, 0),
        35,
        "펑펑"
    );

    ReDetectionMinutesAndRepellentSoundResponse res3 = ReDetectionMinutesAndRepellentSoundResponse.of(
        LocalTime.of(8, 22, 0),
        24,
        "빵빵"
    );

    ReDetectionMinutesAndRepellentSoundResponse res4 = ReDetectionMinutesAndRepellentSoundResponse.of(
        LocalTime.of(8, 23, 0),
        13,
        "멍멍"
    );



    when(repellentDataService.getDetectionListByDetectionDeviceId(detectionDeviceId))
        .thenReturn(List.of(res1, res2, res3, res4));

    //when
    ResultActions resultActions = mockMvc.perform(
            RestDocumentationRequestBuilders.get(
                API + "/repellent-data/detail/detection/detection-device-id/{detectionDeviceId}",
                detectionDeviceId))
        .andDo(print());

    //then

    resultActions.andExpect(status().isOk())
        .andDo(
            document(
                "repellent-data/detail/detection/detection-device-id/success",
                RequestDocumentation.pathParameters(
                    RequestDocumentation.parameterWithName("detectionDeviceId")
                        .description("퇴치기기 ID")
                ),
                responseFields(
                    fieldWithPath("[].detectionTime").description("퇴치 데이터가 측정된 시간"),
                    fieldWithPath("[].reDetectionMinutes").description("재탐지 시간"),
                    fieldWithPath("[].repellentSound").description("퇴치기 소리명")
                )
            )
        );

  }

}