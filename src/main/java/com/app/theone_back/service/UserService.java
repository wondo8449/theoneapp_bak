package com.app.theone_back.service;

import com.app.theone_back.dto.LoginRequestDto;
import com.app.theone_back.dto.SignupRequestDto;
import com.app.theone_back.entity.CustomException;
import com.app.theone_back.entity.ErrorCode;
import com.app.theone_back.entity.User;
import com.app.theone_back.entity.UserRoleEnum;
import com.app.theone_back.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public void signup(SignupRequestDto signupRequestDto) {
        if (isUserIdExist(signupRequestDto.getUserId())) {
            throw new CustomException(ErrorCode.DUPLICATE_USER_ID);
        }

        if (isUserNameExist(signupRequestDto.getUserName())) {
            throw new CustomException(ErrorCode.DUPLICATE_NICKNAME);
        }

        User user = new User(signupRequestDto, UserRoleEnum.USER);

        userRepository.save(user);
    }

    @Transactional
    public User login(LoginRequestDto loginRequestDto) {
        System.out.println("id : " + loginRequestDto.getUserId());
        System.out.println("password : " + loginRequestDto.getPassword());
        User user = userRepository.findByUserId(loginRequestDto.getUserId()).orElseThrow(
                () -> new CustomException(ErrorCode.LOGIN_FAIL)
        );
        System.out.println("user : " + user);
        if(!user.getPassword().equals(loginRequestDto.getPassword())) {
            System.out.println("들어옴1");
            throw new CustomException(ErrorCode.LOGIN_FAIL);
        }
        System.out.println("들어옴2");
        return user;
    }

    @Transactional(readOnly = true)
    public boolean isUserIdExist(String userId) {

        return userRepository.existsByUserId(userId);
    }

    @Transactional(readOnly = true)
    public boolean isUserNameExist(String userName) {

        return userRepository.existsByUserName(userName);
    }
}
