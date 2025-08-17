package com.english.enky.repository;

import com.english.enky.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Boolean existsByUsername(String username);
    Boolean existsByNickname(String nickname);

    User findByUsername(String username);
}
