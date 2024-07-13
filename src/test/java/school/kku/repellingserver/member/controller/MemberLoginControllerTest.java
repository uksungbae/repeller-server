package school.kku.repellingserver.member.controller;


import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.ResultActions;
import school.kku.repellingserver.common.BaseControllerTest;
import school.kku.repellingserver.jwt.service.TokenService;
import school.kku.repellingserver.mail.service.MailService;
import school.kku.repellingserver.member.domain.Member;
import school.kku.repellingserver.member.dto.FindIdResponse;
import school.kku.repellingserver.member.dto.LoginRequest;
import school.kku.repellingserver.member.dto.RegisterRequest;
import school.kku.repellingserver.member.service.MemberService;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.cookies.CookieDocumentation.cookieWithName;
import static org.springframework.restdocs.cookies.CookieDocumentation.responseCookies;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MemberLoginControllerTest extends BaseControllerTest {

    @MockBean
    MemberService memberService;

    @MockBean
    TokenService tokenService;

    @MockBean
    MailService mailService;

    @Test
    void 로그인을_성공한다() throws Exception {
        //given

        String loginId = "loginId";
        LoginRequest loginRequest = LoginRequest.of(loginId, "password");


        when(memberService.isLoginSuccess(loginRequest))
                .thenReturn(true);
        when(tokenService.generateAccessToken(loginId))
                .thenReturn("accessToken");
        when(tokenService.generateRefreshToken(loginId))
                .thenReturn("refreshToken");
        when(memberService.loadUserByUsername(loginId))
                .thenReturn(Member.builder()
                        .name("name")
                        .build());

        //when
        ResultActions resultActions = mockMvc.perform(post(API + "/login")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andDo(print());

        //then
        resultActions.andExpect(status().isOk())
                .andDo(document("member/login/success",
                        requestFields(
                                fieldWithPath("loginId").description("이메일"),
                                fieldWithPath("password").description("비밀번호")
                        ),
                        responseHeaders(
                                headerWithName("Authorization").description("accessToken")
                        ),
                        responseCookies(
                                cookieWithName("refreshToken").description("refreshToken")
                        ),
                        responseFields(
                                fieldWithPath("name").description("유저의 이름")
                        )
                ));

    }

    @Test
    void 로그인시_아이디가_틀리면_에러를_던진다() throws Exception {
        //given

        String loginId = "loginId";
        LoginRequest loginRequest = LoginRequest.of(loginId, "password");


        when(memberService.isLoginSuccess(loginRequest))
                .thenThrow(new IllegalArgumentException("존재하지 않는 유저입니다."));

        //when
        ResultActions resultActions = mockMvc.perform(post(API + "/login")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andDo(print());

        //then
        resultActions.andExpect(status().isBadRequest())
                .andDo(document("member/login/fail/loginId",
                        requestFields(
                                fieldWithPath("loginId").description("이메일"),
                                fieldWithPath("password").description("비밀번호")
                        ),
                        responseFields(
                                fieldWithPath("message").description("에러 메시지")
                        )
                ));
    }

    @Test
    void 로그인시_아이디와_비밀번호가_일치하지않으면_에러를_던진다() throws Exception {
        //given

        String loginId = "loginId";
        LoginRequest loginRequest = LoginRequest.of(loginId, "password");


        when(memberService.isLoginSuccess(loginRequest))
                .thenThrow(new IllegalArgumentException("잘못된 비밀번호입니다!"));

        //when
        ResultActions resultActions = mockMvc.perform(post(API + "/login")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andDo(print());

        //then
        resultActions.andExpect(status().isBadRequest())
                .andDo(document("member/login/fail/password",
                        requestFields(
                                fieldWithPath("loginId").description("이메일"),
                                fieldWithPath("password").description("비밀번호")
                        ),
                        responseFields(
                                fieldWithPath("message").description("에러 메시지")
                        )
                ));
    }

    @Test
    void 회원가입을_성공한다() throws Exception {
        //given
        RegisterRequest registerRequest = RegisterRequest.of("loginId", "password", "name", "email");

        //when
        ResultActions resultActions = mockMvc.perform(post(API + "/register")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andDo(print());

        //then
        resultActions.andExpect(status().isOk())
                .andDo(document("member/register/success",
                        requestFields(
                                fieldWithPath("loginId").description("이메일"),
                                fieldWithPath("password").description("비밀번호"),
                                fieldWithPath("name").description("이름"),
                                fieldWithPath("email").description("이메일")
                        )
                ));

    }

    @Test
    void 이메일을_받으면_인증번호를_리턴한다() throws Exception {
        //given
        String certificationNumber = "123456";

        when(mailService.sendCertificationNumber(anyString()))
                .thenReturn(certificationNumber);

        //when
        ResultActions resultActions = mockMvc.perform(get(API + "/certification")
                        .param("email", "test@email.com"))
                .andDo(print());

        //then
        resultActions.andExpect(status().isOk())
                .andDo(document("member/register/certification/success",
                                queryParameters(
                                        parameterWithName("email").description("이메일")
                                ),
                                responseFields(
                                        fieldWithPath("certificationNumber").description("인증번호")
                                )
                        )
                );
    }

    @Test
    void 이름과_이메일로_아이디를_찾아서_리턴한다() throws Exception {
        //given
        when(memberService.findByNameAndEmail(anyString(), anyString()))
                .thenReturn(FindIdResponse.of("loginId"));
        //when
        ResultActions resultActions = mockMvc.perform(get(API + "/find/id")
                        .param("name", "name")
                        .param("email", "email")
                )
                .andDo(print());

        //then
        resultActions.andExpect(status().isOk())
                .andDo(document("member/find/id/success",
                                queryParameters(
                                        parameterWithName("name").description("이름"),
                                        parameterWithName("email").description("이메일")
                                ),
                                responseFields(
                                        fieldWithPath("loginId").description("로그인할 때 쓰는 아이디")
                                )
                        )
                );
    }


}