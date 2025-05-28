package com.oopsw.clario.config.auth.authdto;


import com.oopsw.clario.domain.member.Member;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class SessionUser implements Serializable {

    private String name;
    private String email;

    public SessionUser(Member member) {
        this.name = member.getName();
        this.email = member.getEmail();
    }

    // 새로운 오버로딩: name, email 직접 전달
    public SessionUser(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
