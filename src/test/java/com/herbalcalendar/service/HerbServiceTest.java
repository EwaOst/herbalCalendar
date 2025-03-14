package com.herbalcalendar.service;

import com.herbalcalendar.enums.ActiveCompoundEnum;
import com.herbalcalendar.enums.HarvestTime;
import com.herbalcalendar.model.HarvestPeriod;
import com.herbalcalendar.model.HerbModel;
import com.herbalcalendar.model.UserHerbModel;
import com.herbalcalendar.repository.HerbRepository;
import com.herbalcalendar.repository.UserHerbRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class HerbServiceTest {
    @Mock
    private HerbRepository herbRepository;
    @Mock
    private UserHerbRepository userHerbRepository;

    @InjectMocks
    private HerbService herbService;

    @Test
    void getAllHerbs() {
        // Given
        HerbModel herb1 = new HerbModel(
                1L, "Mieta", "Mentha", "Opis miety", ActiveCompoundEnum.OLEJKI_ETERYCZNE,
                new HarvestPeriod(HarvestTime.POCZATEK, Month.JUNE, "kwiaty"), new ArrayList<>());

        HerbModel herb2 = new HerbModel(
                2L, "Nagietek", "Calendula officinalis", "Opis nagietka", ActiveCompoundEnum.FLAWONOIDY,
                new HarvestPeriod(HarvestTime.KONIEC, Month.APRIL, "korzenie"), new ArrayList<>());

        List<HerbModel> expectedHerbs = Arrays.asList(herb1, herb2);

        // When
        when(herbRepository.findAll()).thenReturn(expectedHerbs);  // Mockujemy odpowiedź z repozytorium

        List<HerbModel> actualHerbs = herbService.getAllHerbs();  // Wywołanie testowanej metody

        // Then
        assertNotNull(actualHerbs);  // Sprawdzamy, że wynik nie jest null
        assertEquals(2, actualHerbs.size());  // Sprawdzamy, czy lista ma 2 elementy
        assertEquals(expectedHerbs, actualHerbs);  // Sprawdzamy, czy listy są równe
    }


    @Test
    void createHerb() {
        // GIVEN
        HerbModel herbModel = new HerbModel();
        HarvestPeriod harvestPeriod = new HarvestPeriod(HarvestTime.POCZATEK, Month.JUNE, "liście");

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
        assertEquals(HarvestTime.POCZATEK, result.getHarvestPeriod().getHarvestTime());
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
        Long herbId = 1L;
        HerbModel existingHerb = new HerbModel();
        // Given
        existingHerb.setId(herbId);
        existingHerb.setHerb("Mieta");
        existingHerb.setLatinName("Mentha");
        existingHerb.setDescription("Mieta (z łac. Mentha L.) jest krzewinką szeroko rozpowszechnioną w uprawach, występującą również w stanie dzikim.");
        existingHerb.setActiveCompoundEnum(ActiveCompoundEnum.OLEJKI_ETERYCZNE);
        existingHerb.setHarvestPeriod(new HarvestPeriod(HarvestTime.POCZATEK, Month.JULY, "kwiaty"));

        HerbModel updateHerb = new HerbModel();
        updateHerb.setHerb("Nagietek lekarski");
        updateHerb.setLatinName("Calendula officinalis");
        updateHerb.setDescription("Nagietek lekarski jako surowiec leczniczy wykorzystywany jest w postaci kwiatów (łac. calendulae flos), z których przyrządza się herbatę, maści, olejek eteryczny, wyciągi wodne i alkoholowe.");
        updateHerb.setActiveCompoundEnum(ActiveCompoundEnum.FLAWONOIDY);
        updateHerb.setHarvestPeriod(new HarvestPeriod(HarvestTime.KONIEC, Month.APRIL, "korzenie"));

        // When
        when(herbRepository.findById(herbId)).thenReturn(Optional.of(existingHerb));
        when(herbRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        HerbModel result = herbService.updateHerb(herbId, updateHerb); // Bez Optional

        // Then
        assertEquals(updateHerb.getHerb(), result.getHerb()); // Sprawdź, czy nazwa zioła została zaktualizowana
        assertEquals(updateHerb.getLatinName(), result.getLatinName());
        assertEquals(updateHerb.getDescription(), result.getDescription());
        assertEquals(updateHerb.getActiveCompoundEnum(), result.getActiveCompoundEnum());
        assertEquals(updateHerb.getHarvestPeriod().getHarvestTime(), result.getHarvestPeriod().getHarvestTime());
        assertEquals(updateHerb.getHarvestPeriod().getHarvestMonth(), result.getHarvestPeriod().getHarvestMonth());
        assertEquals(updateHerb.getHarvestPeriod().getPart(), result.getHarvestPeriod().getPart());
    }

    @Test
    void deleteHerb() {
        Long id = 1L;

        when(herbRepository.existsById(id)).thenReturn(true);

        herbService.deleteHerb(id);

        verify(herbRepository).deleteById(id);
    }

    @Test
    void getHerbsByUserId() {
        // Given
        Long userId = 1L;

        // Przygotowanie danych testowych
        HerbModel herb1 = new HerbModel(1L, "Mięta", "Mentha", "Opis miety", ActiveCompoundEnum.OLEJKI_ETERYCZNE, new HarvestPeriod(HarvestTime.POCZATEK, Month.JUNE, "kwiaty"), new ArrayList<>());
        HerbModel herb2 = new HerbModel(2L, "Rumianek", "Calendula officinalis", "Opis rumianku", ActiveCompoundEnum.FLAWONOIDY, new HarvestPeriod(HarvestTime.KONIEC, Month.APRIL, "korzenie"), new ArrayList<>());

        UserHerbModel userHerb1 = new UserHerbModel();
        userHerb1.setHerb(herb1);

        UserHerbModel userHerb2 = new UserHerbModel();
        userHerb2.setHerb(herb2);

        List<UserHerbModel> userHerbs = List.of(userHerb1, userHerb2);

        // Mockowanie zachowania repozytorium
        when(userHerbRepository.findByUser_Id(userId)).thenReturn(userHerbs);

        // When
        List<HerbModel> result = herbService.getHerbsByUserId(userId);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(herb1, result.get(0));
        assertEquals(herb2, result.get(1));

        // Weryfikacja, czy metoda findByUser_Id została wywołana
        verify(userHerbRepository, times(1)).findByUser_Id(userId);
    }
}
