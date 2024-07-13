package school.kku.repellingserver.repellent.repellentDevice.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.ResultActions;
import school.kku.repellingserver.common.BaseControllerTest;
import school.kku.repellingserver.repellent.repellentDevice.service.RepellentDeviceService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RepellentDeviceControllerTest extends BaseControllerTest {

    @MockBean
    private RepellentDeviceService repellentDeviceService;

    @Test
    void 퇴치기기가_존재하고_등록이안되었으면_등록하고_true를_리턴한다() throws Exception {
        //given
        when(repellentDeviceService.isSerialIdExistsActivated(any(), any()))
                .thenReturn(true);

        //when
        ResultActions resultActions = mockMvc.perform(get(API + "/repellent-device/valid/serial-id")
                        .param("serialId", "256")
                        .param("farmId", "1"))
                .andDo(print());

        //then
        resultActions.andExpect(status().isOk())
                .andDo(document("repellent-device/valid/serial-id/success",
                        queryParameters(
                                parameterWithName("serialId").description("퇴치기기의 시리얼 아이디"),
                                parameterWithName("farmId").description("농장 아이디")
                        ),
                        responseFields(
                                fieldWithPath("isSerialIdExists").description("퇴치기기가 존재하고 등록이 안되었으면 true, 아니면 false")
                        )
                ));

    }

}