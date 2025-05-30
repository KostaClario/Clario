package com.oopsw.clario.config.auth;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {


    private final CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(
                        (csrfConfig) -> csrfConfig.disable()
                )
                .headers(
                        (headerConfig) ->headerConfig.frameOptions(
                                frameOptionsConfig -> frameOptionsConfig.disable()
                        )
                )
                .authorizeHttpRequests(
                        (auth) -> auth
                                .requestMatchers("/", "/css/**", "/js/**", "/img/**", "/account-css/**").permitAll()

                                // 로그인 및 회원가입 관련
                                .requestMatchers("/loginView", "/loginSuccess").permitAll()
                                .requestMatchers("/privacy", "/agree", "/join").permitAll()
                                .requestMatchers(HttpMethod.POST, "/join").permitAll()

                                // OAuth2 관련
                                .requestMatchers("/oauth2/**", "/login/oauth2/**").permitAll()

                                // 계좌 카드 연동
                                .requestMatchers("/myData/**").authenticated()

                                // 통계
                                .requestMatchers("/view/statistics", "/api/**").authenticated()

                                // 인증 필요한 기능
                                .requestMatchers("/modal", "/account/edit", "/account/remove",
                                        "/account/reset-password", "/account/verify-password").authenticated()

                                .anyRequest().authenticated()
                )
                .logout(
                        (logoutConfig) -> logoutConfig
                                .logoutUrl("/logout")
                                .logoutSuccessUrl("/loginView")
                                .invalidateHttpSession(true)
                                .deleteCookies("JSESSIONID")
                )
                .oauth2Login(
                        (oauth2) -> oauth2
                                .loginPage("/loginView")
                                .userInfoEndpoint(
                                        (userInfo) -> userInfo
                                                .userService(customOAuth2UserService)
                                )
                                .defaultSuccessUrl("/loginSuccess", true) // "/"에서 리다이렉트 처리
                );
        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}



