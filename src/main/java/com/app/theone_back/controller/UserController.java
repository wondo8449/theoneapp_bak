package com.app.theone_back.controller;

import com.app.theone_back.dto.CommonResponseDto;
import com.app.theone_back.dto.SMSRequestDto;
import com.app.theone_back.dto.SignupRequestDto;
import com.app.theone_back.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.message.exception.NurigoEmptyResponseException;
import net.nurigo.sdk.message.exception.NurigoMessageNotReceivedException;
import net.nurigo.sdk.message.exception.NurigoUnknownException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<CommonResponseDto> signup(@Valid @RequestBody SignupRequestDto signupRequestDto) {
        userService.signup(signupRequestDto);

        return new ResponseEntity<>(new CommonResponseDto(201, "회원가입에 성공하였습니다. 🌠", null), HttpStatus.CREATED);
    }

    @PostMapping("/sendSMS")
    public ResponseEntity<CommonResponseDto> sendSMS(@RequestBody SMSRequestDto smsRequestDto) throws NurigoMessageNotReceivedException, NurigoEmptyResponseException, NurigoUnknownException {
        userService.sendSMS(smsRequestDto);

        return new ResponseEntity<>(new CommonResponseDto(201, "SMS 전송에 성공하였습니다. 🌠", null), HttpStatus.CREATED);
    }

}

