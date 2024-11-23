package com.app.theone_back.service;

import com.app.theone_back.dto.SMSRequestDto;
import com.app.theone_back.dto.SignupRequestDto;
import com.app.theone_back.entity.CustomException;
import com.app.theone_back.entity.ErrorCode;
import com.app.theone_back.entity.User;
import com.app.theone_back.entity.UserRoleEnum;
import com.app.theone_back.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.exception.NurigoEmptyResponseException;
import net.nurigo.sdk.message.exception.NurigoMessageNotReceivedException;
import net.nurigo.sdk.message.exception.NurigoUnknownException;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Value("${coolsms.api.key}")
    private String apiKey;
    @Value("${coolsms.api.secret}")
    private String apiSecretKey;

    @Transactional
    public void signup(SignupRequestDto signupRequestDto) {

        if (isUserIdExist(signupRequestDto.getUserId())) {
            throw new CustomException(ErrorCode.DUPLICATE_USER_ID);
        }

        if (isUserNameExist(signupRequestDto.getUserName())) {
            throw new CustomException(ErrorCode.DUPLICATE_NICKNAME);
        }

        String encodedPassword = passwordEncoder.encode(signupRequestDto.getPassword());

        User user = new User(signupRequestDto, UserRoleEnum.USER);

        user.encryptionPassword(encodedPassword);

        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public boolean isUserIdExist(String userId) {

        return userRepository.existsByUserId(userId);
    }

    @Transactional(readOnly = true)
    public boolean isUserNameExist(String userName) {

        return userRepository.existsByUserName(userName);
    }

    @Transactional(readOnly = true)
    public void sendSMS(SMSRequestDto smsRequestDto) throws NurigoMessageNotReceivedException, NurigoEmptyResponseException, NurigoUnknownException {

        if (isUserPhoneExist(smsRequestDto.getPhone())) {
            throw new CustomException(ErrorCode.DUPLICATE_USER_ID);
        }

        DefaultMessageService messageService =  NurigoApp.INSTANCE.initialize("NCSYGWBUDHRDEPOX", "M4XIOCTYTQKEW4ZVD1EHIJSKKTUHVDMM", "https://api.coolsms.co.kr");

        Random rand = new Random();
        StringBuilder numStr = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            numStr.append(rand.nextInt(10));
        }

        Message message = new Message();
        message.setFrom("01099408245");
        message.setTo(smsRequestDto.getPhone());
        message.setText("인증번호는 [" + numStr + "] 입니다.");

        try {
            messageService.send(message);
        } catch (NurigoMessageNotReceivedException exception) {
        }
    }

    @Transactional(readOnly = true)
    public boolean isUserPhoneExist(String phone) {

        return userRepository.existsByUserPhone(phone);
    }

}
