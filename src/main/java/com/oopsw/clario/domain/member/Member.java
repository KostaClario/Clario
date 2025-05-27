package com.oopsw.clario.domain.member;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "member", uniqueConstraints = {
        @UniqueConstraint(name = "UK_Member", columnNames = "email")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id", nullable = false)
    private Integer memberId;

    @Column(name = "Oauth", nullable = false, length = 30)
    private String oauth;

    @Column(name = "name", nullable = false, length = 30)
    private String name;

    @Column(name = "phonenum", nullable = false, length = 30)
    private String phonenum;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "email", nullable = false, length = 100, unique = true)
    private String email;

    @Column(name = "password", nullable = false, length = 30)
    private String password;

    @Column(name = "total_assets", nullable = false)
    private Long totalAssets;

    @Column(name = "target_assets")
    private Long targetAssets;

    @Column(name = "activation", nullable = false)
    private Boolean activation;

    @Column(name = "last_synced_at", nullable = false)
    private LocalDateTime lastSyncedAt;
}
