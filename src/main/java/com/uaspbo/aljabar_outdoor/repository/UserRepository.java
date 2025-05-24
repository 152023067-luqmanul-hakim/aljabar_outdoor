package com.uaspbo.aljabar_outdoor.repository;

import com.uaspbo.aljabar_outdoor.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    java.util.Optional<User> findByUsername(String username);
}
