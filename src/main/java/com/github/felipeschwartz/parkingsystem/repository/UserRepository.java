package com.github.felipeschwartz.parkingsystem.repository;

import com.github.felipeschwartz.parkingsystem.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Query("select count(u) > 0 from User u join u.roles r where r = :role")
    boolean existsByRole(@Param("role") String role);
}