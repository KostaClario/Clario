package com.oopsw.clario.config.auth;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

@Getter
public class CustomOAuth2User implements OAuth2User {

    private final String name;
    private final String email;
    private final Map<String, Object> attributes;
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomOAuth2User(String name, String email, Map<String, Object> attributes,
                            Collection<? extends GrantedAuthority> authorities) {
        this.name = name;
        this.email = email;
        this.attributes = attributes;
        this.authorities = authorities;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getName() {
        return name;
    }
}
