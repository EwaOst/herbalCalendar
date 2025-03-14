package com.herbalcalendar.service;

import com.herbalcalendar.model.HerbModel;
import com.herbalcalendar.model.UserHerbModel;
import com.herbalcalendar.model.UserModel;
import com.herbalcalendar.repository.UserHerbRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserHerbServiceTest {

    @Mock
    private HerbService herbService;

    @Mock
    private UserHerbRepository userHerbRepository;

    @InjectMocks  // <-- To wstrzyknie zależności do UserHerbService
    private UserHerbService userHerbService;

    @Test
    void getHerbsByUserId_WhenUserHasHerbs_ShouldReturnHerbs() {
        // GIVEN
        Long userId = 1L;

        UserModel user = new UserModel();
        user.setId(userId);

        HerbModel herb1 = new HerbModel(1L, "Mięta", "Mentha", "Leczy żołądek", null, null, null);
        HerbModel herb2 = new HerbModel(2L, "Rumianek", "Matricaria", "Koi nerwy", null, null, null);

        UserHerbModel userHerb1 = new UserHerbModel();
        userHerb1.setUser(user);
        userHerb1.setHerb(herb1);

        UserHerbModel userHerb2 = new UserHerbModel();
        userHerb2.setUser(user);
        userHerb2.setHerb(herb2);

        List<UserHerbModel> userHerbs = List.of(userHerb1, userHerb2);

        when(userHerbRepository.findByUser_Id(userId)).thenReturn(userHerbs);

        // WHEN
        List<HerbModel> result = userHerbService.getHerbsByUserId(userId);

        // THEN
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(herb1));
        assertTrue(result.contains(herb2));

        verify(userHerbRepository).findByUser_Id(userId);
    }
}