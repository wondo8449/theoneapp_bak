package com.app.theone_back.controller;

import com.app.theone_back.dto.CommonResponseDto;
import com.app.theone_back.dto.SignupRequestDto;
import com.app.theone_back.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<CommonResponseDto> signup(@Valid @RequestBody SignupRequestDto signupRequestDto) {
        userService.signup(signupRequestDto);

        return new ResponseEntity<>(new CommonResponseDto(201, "회원가입에 성공하였습니다. 🌠", null), HttpStatus.CREATED);
    }

}

