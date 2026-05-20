package com.example.zerodelta.repository;

import com.example.zerodelta.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
     Optional<User>findByEmail(String email);
}
