package com.herbalcalendar.repository;

import com.herbalcalendar.model.HerbModel;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@Repository
public interface HerbRepository extends JpaRepository<HerbModel, Long> {
    List<HerbModel> findByHarvestPeriod_HarvestMonth(Month harvestMonth);
}