package com.app.theone_back.service;

import com.app.theone_back.dto.KaKaoUserInfoDto;
import com.app.theone_back.entity.User;
import com.app.theone_back.entity.UserRoleEnum;
import com.app.theone_back.repository.UserRepository;
import com.app.theone_back.security.JwtProvider;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class KakaoService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;
    private final JwtProvider jwtProvider;

    @Value("${SOCIAL_KAKAO_CLIENT_ID}")
    private String kakaoClientId;

    @Value("${SOCIAL_KAKAO_REDIRECT_URI}")
    private String kakaoRedirectUri;

    public List<String> kakaoLogin(String code, HttpServletResponse response) throws JsonProcessingException {
        List<String> res = new ArrayList<>();

        String accessToken = getToken(code);
        KaKaoUserInfoDto kakaoUserInfo = getKakaoUserInfo(accessToken);
        User kakaoUser = registerKakaoUserIfNeeded(kakaoUserInfo);

        String jwtAccessToken = jwtProvider.createAccessToken(kakaoUser.getUserId(), kakaoUser.getUserRole());
        String jwtRefreshToken = jwtProvider.createRefreshToken(kakaoUser.getUserId());

        kakaoUser.updateRefresh(jwtRefreshToken);
        userRepository.save(kakaoUser);

        response.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + jwtAccessToken);
        response.setStatus(HttpServletResponse.SC_OK);

        res.add(accessToken);
        res.add(kakaoUser.getUserName());

        return res;
    }

    private String getToken(String code) throws JsonProcessingException {
        URI uri = UriComponentsBuilder
                .fromUriString("https://kauth.kakao.com")
                .path("/oauth/token")
                .encode()
                .build()
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", kakaoClientId);
        body.add("redirect_uri", kakaoRedirectUri);
        body.add("code", code);

        RequestEntity<MultiValueMap<String, String>> requestEntity = RequestEntity
                .post(uri)
                .headers(headers)
                .body(body);

        ResponseEntity<String> response = restTemplate.exchange(
                requestEntity,
                String.class
        );
        JsonNode jsonNode = new ObjectMapper().readTree(response.getBody());

        return jsonNode.get("access_token").asText();
    }

    private KaKaoUserInfoDto getKakaoUserInfo(String accessToken) throws JsonProcessingException {
        URI uri = UriComponentsBuilder
                .fromUriString("https://kapi.kakao.com")
                .path("/v2/user/me")
                .encode()
                .build()
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        RequestEntity<MultiValueMap<String, String>> requestEntity = RequestEntity
                .post(uri)
                .headers(headers)
                .body(new LinkedMultiValueMap<>());

        ResponseEntity<String> response = restTemplate.exchange(
                requestEntity,
                String.class
        );

        JsonNode jsonNode = new ObjectMapper().readTree(response.getBody());
        Long id = jsonNode.get("id").asLong();
        String nickname = jsonNode.get("properties")
                .get("nickname").asText();
        String email = jsonNode.get("kakao_account")
                .get("email").asText();

        return new KaKaoUserInfoDto(id, nickname, email);
    }

    private User registerKakaoUserIfNeeded(KaKaoUserInfoDto kakaoUserInfo) {
        Long kakaoId = kakaoUserInfo.getId();
        User kakaoUser = userRepository.findByKakaoId(kakaoId).orElse(null);

        if (kakaoUser == null) {
            String kakaoEmail = kakaoUserInfo.getEmail();
            User sameEmailUser = userRepository.findByEmail(kakaoEmail).orElse(null);
            if (sameEmailUser != null) {
                kakaoUser = sameEmailUser;
                kakaoUser = kakaoUser.kakaoIdUpdate(kakaoId);
            } else {
                String password = UUID.randomUUID().toString();
                String encodedPassword = passwordEncoder.encode(password);
                String loginId = UUID.randomUUID().toString();
                String email = kakaoUserInfo.getEmail();

                kakaoUser = new User(email, encodedPassword, kakaoUserInfo.getNickname(), UserRoleEnum.USER, kakaoId, loginId);
            }
        }

        return kakaoUser;
    }

}
