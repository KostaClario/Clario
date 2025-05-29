package com.oopsw.clario.config.auth;

import com.oopsw.clario.config.auth.authdto.OAuthAttributes;
import com.oopsw.clario.config.auth.authdto.SessionUser;
import com.oopsw.clario.domain.member.Member;
import com.oopsw.clario.domain.member.MemberRepository;
import com.oopsw.clario.domain.member.Role;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberRepository memberRepository;
    private final HttpSession httpSession;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 기본 OAuth2 사용자 서비스로부터 사용자 정보 로드
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate  = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);




        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();

        // 구글 응답을 추출 name email
        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        // 회원가입된 사용자인 경우만 업데이트
        Member member = memberRepository.findByEmail(attributes.getEmail())
                .map(entity -> entity.update(attributes.getName(), attributes.getEmail()))
                .orElse(null);

        // 회원이면 메인으로 아니면 privacy로
        String redirectUrl;
        if (member == null) {
            log.info("세션 ID (OAuth2UserService): " + httpSession.getId());
            httpSession.setAttribute("oauthAttributes", attributes);
            redirectUrl = "/privacy";
        }else{
            log.info("회원이므로 modal로 리디렉트");
            redirectUrl = "/modal";
        }

        log.info("CustomOAuth2UserService 실행됨");
        log.info("이메일: " + attributes.getEmail());
        log.info("멤버 여부: " + (member != null));
        log.info("세션 ID: " + httpSession.getId());

        httpSession.setAttribute("redirectUrl", redirectUrl);

        // 사용자 권한
        String role = (member != null) ? member.getRoleKey() : Role.USER.getKey();

        // CustomOAuth2User 리턴
        return new CustomOAuth2User(
                attributes.getEmail(),
                attributes.getEmail(),
                attributes.getAttributes(),
                Collections.singleton(new SimpleGrantedAuthority(role))
        );
    }
}
