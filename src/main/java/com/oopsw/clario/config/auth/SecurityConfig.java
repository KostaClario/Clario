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
                                .requestMatchers(HttpMethod.GET, "/loginView").permitAll()
                                .requestMatchers("/","/loginSuccess", "/css/**","/account-css/**", "/img/**", "/js/**").permitAll()
                                .requestMatchers("/account/modal","/privacy","/agree","/join").authenticated()
                                .requestMatchers("/template-test").permitAll()
                                .anyRequest().authenticated()
                )
                .logout(
                        (logoutConfig) -> logoutConfig.logoutSuccessUrl("/")
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



