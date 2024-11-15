package com.app.theone_back.controller;

import com.app.theone_back.dto.CommonResponseDto;
import com.app.theone_back.dto.LoginRequestDto;
import com.app.theone_back.dto.SignupRequestDto;
import com.app.theone_back.entity.User;
import com.app.theone_back.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<CommonResponseDto> signup(@Valid @RequestBody SignupRequestDto signupRequestDto) {
        userService.signup(signupRequestDto);

        return new ResponseEntity<>(new CommonResponseDto(201, "회원가입에 성공하였습니다. 🌠", null), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<CommonResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        User user = userService.login(loginRequestDto);

        return new ResponseEntity<>(new CommonResponseDto(200, "로그인에 성공하였습니다. 🌠", user), HttpStatus.OK);
    }
}

