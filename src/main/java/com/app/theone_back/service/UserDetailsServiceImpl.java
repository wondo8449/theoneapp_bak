package com.app.theone_back.service;

import com.app.theone_back.entity.CustomException;
import com.app.theone_back.entity.User;
import com.app.theone_back.repository.UserRepository;
import com.app.theone_back.security.UserDetailsImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {

        Optional<User> userOptional = userRepository.findByUserId(loginId);
        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + loginId);
        }

        User user = userOptional.get();

        return new UserDetailsImpl(user);
    }

}
