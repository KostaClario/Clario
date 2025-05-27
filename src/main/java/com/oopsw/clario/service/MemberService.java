package com.oopsw.clario.service;


import com.oopsw.clario.domain.member.Member;
import com.oopsw.clario.domain.member.MemberRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class MemberService {

    private final MemberRepository memberRepository;


    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public boolean existsByEmail(String email) {
        return memberRepository.existsByEmail(email);
    }

    public void saveGoogleMember(String email, String name, String phonenum, String password){
        Member member = Member.builder()
                .email(email)
                .oauth("google")
                .name(name)
                .phonenum(phonenum)
                .password(password)
                .totalAssets(0L)
                .targetAssets(0L)
                .activation(true)
                .lastSyncedAt(LocalDateTime.now())
                .build();
        memberRepository.save(member);
    }
}
