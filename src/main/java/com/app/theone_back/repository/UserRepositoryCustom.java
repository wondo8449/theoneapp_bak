package com.app.theone_back.repository;

import org.springframework.stereotype.Repository;

@Repository
public interface UserRepositoryCustom {

    boolean existsByUserId(String userId);
    boolean existsByUserName(String userName);
    boolean existsByUserPhone(String email);

}
