package com.herbalcalendar.repository;

import com.herbalcalendar.model.HerbModel;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface HerbRepository extends JpaRepository<HerbModel, Long> {

}
