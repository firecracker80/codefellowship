package com.codefellowship.fellowship.repo;

import com.codefellowship.fellowship.models.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRepo extends JpaRepository<AppUser, Long> {
    AppUser findByUserName(String username);
}
