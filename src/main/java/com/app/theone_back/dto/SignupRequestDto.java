package com.app.theone_back.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class SignupRequestDto {

    @NotBlank(message = "사용자 ID는 비워둘 수 없습니다.")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d]{4,10}$", message = "사용자 ID는 알파벳 소문자와 숫자로 이루어진 4자에서 10자 사이여야 합니다.")
    private String userId;

    @NotBlank(message = "비밀번호는 필수 입력 값 입니다.")
    @Size(min = 8, max = 15, message = "비밀번호는 8자 이상, 15자 이하여야 합니다.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[\\W_])\\S{10,}$", message = "password는 알파벳 대소문자(a~z, A~Z), 숫자(0~9), 특수문자로만 구성되어야 합니다.")
    private String password;

    @NotBlank(message = "phone은 비워둘 수 없습니다.")
    private String phone;

    @NotBlank(message = " userName은 비워둘 수 없습니다.")
    private String userName;

    @NotBlank(message = " birthday은 비워둘 수 없습니다.")
    private String birthday;

    @NotBlank(message = " address은 비워둘 수 없습니다.")
    private String address;

    private String school;

    private String job;

    private String tree;

    private String baptismStatus;

}
