package com.shopme.admin.user;

import com.shopme.common.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.email = ?1")
    Optional<User> findByEmail(String email);

    @Query("SELECT u.id FROM User u WHERE u.email = ?1")
    Optional<Integer> findIdByEmail(String email);

    @Modifying
    @Query("UPDATE User u SET u.enabled = ?2 where u.id = ?1 ")
    void updateEnableStatus(Integer id, boolean enable);
}
