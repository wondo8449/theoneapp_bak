package com.app.theone_back.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KaKaoUserInfoDto {

    private Long id;
    private String nickname;
    private String email;

    public KaKaoUserInfoDto(Long id, String nickname, String email) {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
    }

}
