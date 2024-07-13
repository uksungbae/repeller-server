package school.kku.repellingserver.common.config;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import school.kku.repellingserver.common.config.filter.JwtTokenFilter;
import school.kku.repellingserver.common.exception.CustomAuthenticationEntryPoint;
import school.kku.repellingserver.jwt.repository.RefreshTokenRepository;
import school.kku.repellingserver.member.service.MemberService;

//FIXME : authorizeHttpRequest 추후 수정 필요
@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SpringSecurityConfiguration {

    private final MemberService memberService;
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.secret-key}")
    private String secretKey;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((req) -> req
                        .requestMatchers(new MvcRequestMatcher(null, "/api/v1/login")).permitAll()
                        .requestMatchers(new MvcRequestMatcher(null, "/api/v1/register")).permitAll()
                        .requestMatchers(new MvcRequestMatcher(null, "/api/v1/certification")).permitAll()
                        .requestMatchers(new MvcRequestMatcher(null, "/api/v1/find/id")).permitAll()
                        .requestMatchers(new MvcRequestMatcher(null, "/api/v1/repellent-data")).permitAll()
                        .requestMatchers(new MvcRequestMatcher(null, "/docs/index.html")).permitAll()
                        .anyRequest().authenticated()
//                                .anyRequest().permitAll()
                        )
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(new JwtTokenFilter(memberService, refreshTokenRepository, secretKey), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling((exception) -> exception
                        .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                )
                .build();
    }


}
