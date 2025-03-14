package com.herbalcalendar.repository;

import com.herbalcalendar.model.UserHerbModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserHerbRepository extends JpaRepository<UserHerbModel, Long> {
    List<UserHerbModel> findByUser_Id(Long userId);
}

