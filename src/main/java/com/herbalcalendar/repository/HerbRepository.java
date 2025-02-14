package com.herbalcalendar.repository;

import com.herbalcalendar.model.Herb;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;


@Repository
public interface HerbRepository extends JpaRepository<Herb, Long> {
}
