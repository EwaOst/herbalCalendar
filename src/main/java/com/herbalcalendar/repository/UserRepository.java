package com.herbalcalendar.repository;

import com.herbalcalendar.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository <UserModel, Long> {
}
