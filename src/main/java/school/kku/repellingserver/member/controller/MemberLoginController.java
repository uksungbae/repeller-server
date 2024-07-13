package school.kku.repellingserver.member.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import school.kku.repellingserver.jwt.service.TokenService;
import school.kku.repellingserver.mail.service.MailService;
import school.kku.repellingserver.member.domain.Member;
import school.kku.repellingserver.member.dto.*;
import school.kku.repellingserver.member.service.MemberService;

import static school.kku.repellingserver.jwt.constants.TokenDuration.REFRESH_TOKEN_DURATION;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class MemberLoginController {

    private final MemberService memberService;
    private final TokenService tokenService;
    private final MailService mailService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        memberService.isLoginSuccess(loginRequest);

        String accessToken = tokenService.generateAccessToken(loginRequest.loginId());
        String refreshToken = tokenService.generateRefreshToken(loginRequest.loginId());

        response.setHeader(HttpHeaders.AUTHORIZATION, accessToken);

        Cookie refreshTokenCookie = setRefreshTokenCookie(refreshToken);
        response.addCookie(refreshTokenCookie);

        String name = memberService.loadUserByUsername(loginRequest.loginId()).getName();
        return ResponseEntity.ok(LoginResponse.of(name));
    }


    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody RegisterRequest registerRequest) {
        Member register = memberService.register(registerRequest);
        log.info("member = {}", register);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/certification")
    public ResponseEntity<CertificationNumberResponse> getCertificationNumber(@RequestParam String email) {
        String certificationNumber = mailService.sendCertificationNumber(email);

        return ResponseEntity.ok().body(CertificationNumberResponse.of(certificationNumber));
    }

    @GetMapping("/find/id")
    public ResponseEntity<FindIdResponse> findById(@RequestParam String name, @RequestParam String email) {
        FindIdResponse response = memberService.findByNameAndEmail(name, email);

        return ResponseEntity.ok(response);
    }



    private static Cookie setRefreshTokenCookie(String refreshToken) {
        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setMaxAge((int) REFRESH_TOKEN_DURATION.getDuration() / 1000);
        return refreshTokenCookie;
    }


}
