package com.oopsw.clario.config.auth;

import com.oopsw.clario.config.auth.authdto.OAuthAttributes;
import com.oopsw.clario.config.auth.authdto.SessionUser;
import com.oopsw.clario.domain.member.Member;
import com.oopsw.clario.domain.member.MemberRepository;
import com.oopsw.clario.domain.member.Role;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
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

        // 세션 저장
        if (member != null) {
            httpSession.setAttribute("user", new SessionUser(member));
            httpSession.setAttribute("redirectAfterLogin", "/modal");
        }else{
            // DB저장 하지 말고 session에만 저장
            httpSession.setAttribute("oauthAttributes", attributes);
            httpSession.setAttribute("redirectAfterLogin", "/privacy");
        }

        // 세션을 통해 구글 이름과 이메일을 받아오는 거지만 우리 프로젝트와 관련없고
        // 모든 컨트롤러에서 email을 어노테이션으로 받아오기에 일단 학습용으로 남김
        httpSession.setAttribute("user", new SessionUser(attributes.getName(), attributes.getEmail()));

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(
                        member != null ? member.getRoleKey() : Role.USER.getKey())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey());
    }
}
