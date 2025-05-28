package com.oopsw.clario.service;


import com.oopsw.clario.domain.member.Member;
import com.oopsw.clario.domain.member.MemberRepository;
import com.oopsw.clario.domain.member.Role;
import com.oopsw.clario.exception.EmailAlreadyExistsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;


    public MemberService(MemberRepository memberRepository, BCryptPasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean existsByEmail(String email) {
        return memberRepository.existsByEmail(email);
    }



    public void saveMember(String email, String name, String phonenum, String password){

        if (memberRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException("이미 가입된 이메일입니다: " + email);
        }

        Member member = Member.builder()
                .email(email)
                .oauth("google")
                .name(name)
                .phonenum(phonenum)
                .password(passwordEncoder.encode(password))
                .totalAssets(0L)
                .targetAssets(0L)
                .activation(true)
                .lastSyncedAt(LocalDateTime.now())
                .role(Role.USER)
                .build();
        memberRepository.save(member);
    }
}
