package com.herbalcalendar.service;

import com.herbalcalendar.enums.ActiveCompoundEnum;
import com.herbalcalendar.enums.HarvestTime;
import com.herbalcalendar.model.HarvestPeriod;
import com.herbalcalendar.model.HerbModel;
import com.herbalcalendar.repository.HerbRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Month;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HerbServiceTest {
    @Mock
    private HerbRepository herbRepository;
    @InjectMocks
    private HerbService herbService;

    @Test
    void getAllHerbs() {
    }

    @Test
    void createHerb() {
        // GIVEN
        HerbModel herbModel = new HerbModel();
        HarvestPeriod harvestPeriod = new HarvestPeriod(HarvestTime.POCZĄTEK, Month.JUNE, "liście");


        herbModel.setHerb("Mięta");
        herbModel.setLatinName("Mentha");
        herbModel.setDescription("Leczy żołądek");
        herbModel.setActiveCompoundEnum(ActiveCompoundEnum.OLEJKI_ETERYCZNE);
        herbModel.setHarvestPeriod(harvestPeriod);
        herbModel.setUserHerbs(new ArrayList<>());

        // WHEN
        when(herbRepository.save(any(HerbModel.class))).thenReturn(herbModel);
        HerbModel result = herbService.createHerb(herbModel);

        // THEN
        assertNotNull(result);
        assertEquals("Mięta", result.getHerb());
        assertEquals("Mentha", result.getLatinName());
        assertEquals("Leczy żołądek", result.getDescription());
        assertEquals(ActiveCompoundEnum.OLEJKI_ETERYCZNE, result.getActiveCompoundEnum());
        assertEquals(HarvestTime.POCZĄTEK, result.getHarvestPeriod().getHarvestTime());
        assertEquals(Month.JUNE, result.getHarvestPeriod().getHarvestMonth());
        assertEquals("liście", result.getHarvestPeriod().getPart()); // Porównanie z "liście"

        assertTrue(result.getUserHerbs().isEmpty());
    }


    @Test
    void getHerbById() {

        HerbModel herbModel = new HerbModel();
        herbModel.setId(1L);
        herbModel.setHerb("Mieta");

        when(herbRepository.findById(1L)).thenReturn(Optional.of(herbModel));
        Optional<HerbModel> result = herbService.getHerbById(1L);

        assertTrue(result.isPresent());
        assertEquals("Mieta", result.get().getHerb());
    }

    @Test
    void updateHerb() {
        Long herbId =1L;
        HerbModel existingHerb = new HerbModel();
        existingHerb.setId(herbId);
        existingHerb.setHerb("Mieta");
        existingHerb.setLatinName("Mentha");
        existingHerb.setDescription("Mieta (z łac. Mentha L.) jest krzewinką szeroko rozpowszechnioną w uprawach, występującą również w stanie dzikim.");
        existingHerb.setActiveCompoundEnum(ActiveCompoundEnum.OLEJKI_ETERYCZNE);


        
    }

    @Test
    void deleteHerb() {
    }

    @Test
    void getHerbsByUserId() {
    }
}