package com.personal.website.repository;

import com.personal.website.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByUuid(String uid);

    Optional<UserEntity> findByUserName(String userName);

    Optional<UserEntity> findByUserNameOrEmail(String username, String email);

    boolean existsByEmail(String email);

    boolean existsByUserName(String userName);


}
