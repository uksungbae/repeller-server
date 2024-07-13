package school.kku.repellingserver.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import school.kku.repellingserver.member.domain.Member;
import school.kku.repellingserver.member.dto.FindIdResponse;
import school.kku.repellingserver.member.dto.LoginRequest;
import school.kku.repellingserver.member.dto.RegisterRequest;
import school.kku.repellingserver.member.repository.MemberRepository;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Member loadUserByUsername(String loginId) {
        return memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

    }

    public boolean isLoginSuccess(LoginRequest loginRequest) {
        Member member = memberRepository.findByLoginId(loginRequest.loginId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

        if (!passwordEncoder.matches(loginRequest.password(), member.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다!");
        }

        return true;

    }

    @Transactional
    public Member register(RegisterRequest request) {
        Member member = Member.builder()
                .loginId(request.loginId())
                .password(passwordEncoder.encode(request.password()))
                .name(request.name())
                .email(request.email())
                .build();
        return memberRepository.save(member);
    }

    public FindIdResponse findByNameAndEmail(String name, String email) {

        Member member = memberRepository.findByNameAndEmail(name, email)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

        return FindIdResponse.of(member.getLoginId());
    }
}
