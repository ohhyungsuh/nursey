package com.example.be.user.domain;

import com.example.be.global.entity.BaseTimeEntity;
import com.example.be.global.util.CustomUUIDGenerator;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // UUID를 BINARY(16)으로 저장
    @Column(unique = true, nullable = false, columnDefinition = "BYTEA")
    private byte[] userId;

    @Column(unique = true, nullable = false, length = 60)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String imageUrl;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Gender gender;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Role role;

    @Column(nullable = false)
    private boolean isBlocked;

    @Builder
    public User(String email, String password, String nickname, Gender gender) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.gender = gender;

        this.userId = CustomUUIDGenerator.toBytes(CustomUUIDGenerator.generate());
        this.imageUrl = "https://img.icons8.com/?size=100&id=85147&format=png&color=000000";
        this.role = Role.USER;
        this.isBlocked = false;
    }

    public String getUserIdAsString() {
        return CustomUUIDGenerator.fromBytes(this.userId).toString();
    }
}
