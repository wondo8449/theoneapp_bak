package com.app.theone_back.repository;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import com.querydsl.jpa.impl.JPAQueryFactory;

import static com.app.theone_back.entity.QUser.user;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public boolean existsByUserId(String userId) {
        Integer fetchOne = jpaQueryFactory
                .selectOne()
                .from(user)
                .where(user.userId.eq(userId))
                .fetchFirst();

        return fetchOne != null;
    }

    @Override
    public boolean existsByUserName(String userName) {
        Integer fetchOne = jpaQueryFactory
                .selectOne()
                .from(user)
                .where(user.userName.eq(userName))
                .fetchFirst();

        return fetchOne != null;
    }

    @Override
    public boolean existsByUserPhone(String phone) {
        Integer fetchOne = jpaQueryFactory
                .selectOne()
                .from(user)
                .where(user.phone.eq(phone))
                .fetchFirst();

        return fetchOne != null;
    }

}
