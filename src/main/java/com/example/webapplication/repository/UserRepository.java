package com.example.webapplication.repository;

import com.example.webapplication.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  User findByUsername(String username);
  Boolean existsByUsername(String username);
  Boolean existsByEmail(String email);

  User findByEmail(String email);

  User findByResetPasswordToken(String token);
  @Query("SELECT u FROM User u WHERE u.verify_token = :token")
  User findByVerifyToken(@Param("token") String token);
}
