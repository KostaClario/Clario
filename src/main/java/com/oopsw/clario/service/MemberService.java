package com.oopsw.clario.service;


import com.oopsw.clario.domain.member.Member;
import com.oopsw.clario.domain.member.MemberRepository;
import com.oopsw.clario.domain.member.Role;
import com.oopsw.clario.dto.MemberUpdateDTO;
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

    public void resetMemberInfo(String email, MemberUpdateDTO dto){
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(()->new IllegalArgumentException("존재하지 않는 이메일"));
        if (dto.getName() != null && !dto.getName().equals(member.getName())) {
            member.setName(dto.getName());
        }

        if (dto.getPhonenum() != null && !dto.getPhonenum().equals(member.getPhonenum())) {
            member.setPhonenum(dto.getPhonenum());
        }

        if (dto.getPassword() != null && !dto.getPassword().isBlank()){
            if (!dto.getPassword().equals(dto.getConfirmPassword())) {
                throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
            }
            String encodedPassword = passwordEncoder.encode(dto.getPassword());
            member.setPassword(encodedPassword);
        }
    }

    public void resetPassword(String email, String password) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일"));

        String encodePw = passwordEncoder.encode(password);
        member.changePassword(encodePw);

        memberRepository.save(member);
    }

    public Member getMemberByEmail(String email) {
        return memberRepository.findByEmail(email).orElseThrow(
                ()->new IllegalArgumentException("등록된 사용자 없음")
        );
    }

    public void removeMember(String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 메일"));
        memberRepository.delete(member);
    }

    public boolean checkPassword(String email, String password) {
        return memberRepository.findByEmail(email)
                .map(member -> {
                    System.out.println("입력된 비밀번호: " + password);
                    System.out.println("DB 저장된 비밀번호: " + member.getPassword());
                    boolean result = passwordEncoder.matches(password, member.getPassword());
                    System.out.println("비밀번호 일치 여부: " + result);
                    return result;
                })
                .orElse(false);
    }

}
