package com.oopsw.clario.domain.member;

import org.springframework.data.jpa.repository.JpaRepository;


public interface MemberRepository extends JpaRepository<Member, Integer> {
    boolean existsByEmail(String email);
}
