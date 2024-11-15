package com.app.theone_back.entity;

import com.app.theone_back.dto.SignupRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "users")
public class User extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userCode;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String userName;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String birthday;

    @Column(nullable = false)
    private String school;

    @Column(nullable = false)
    private String job;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String Tree;

    @Column(nullable = false)
    private String baptismStatus;

    @Column
    private String team;

    @Column
    private String teamRole;

    @Column
    private Long kakaoId;

    @Column
    private String refreshToken;

    @Column(nullable = false)
    private UserRoleEnum userRole;

    public User(SignupRequestDto signupRequestDto, UserRoleEnum userRole) {
        this.userId = signupRequestDto.getUserId();
        this.userName = signupRequestDto.getUserName();
        this.password = signupRequestDto.getPassword();
        this.phone = signupRequestDto.getPhone();
        this.birthday = signupRequestDto.getBirthday();
        this.school = signupRequestDto.getSchool();
        this.job = signupRequestDto.getJob();
        this.address = signupRequestDto.getAddress();
        this.Tree = signupRequestDto.getTree();
        this.baptismStatus = signupRequestDto.getBaptismStatus();
        this.userRole = userRole;
    }

    public User(String email, String encodedPassword, String userName, UserRoleEnum userRoleEnum, Long kakaoId, String userId) {
        this.password = encodedPassword;
        this.userName = userName;
        this.email = email;
        this.userRole = userRoleEnum;
        this.kakaoId = kakaoId;
        this.userId = userId;
    }

    public void encryptionPassword(String password) {
        this.password = password;
    }

    public void updateRefresh(String refresh) {
        this.refreshToken = refresh;
    }

    public User kakaoIdUpdate(Long kakaoId) {
        this.kakaoId = kakaoId;

        return this;
    }

}
