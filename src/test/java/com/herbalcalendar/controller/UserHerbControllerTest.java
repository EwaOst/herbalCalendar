package com.herbalcalendar.controller;

import com.herbalcalendar.model.HerbModel;
import com.herbalcalendar.service.HerbService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserHerbControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private UserHerbController userHerbController;
    @Mock
    private HerbService herbService;

    @Test
    void getHerbsByUserId_WhenUserExists_ShouldReturnHerbs() {
        // GIVEN
        Long userId = 1L;
        List<HerbModel> herbs = new ArrayList<>();
        herbs.add(new HerbModel(1L, "Mięta", "Mentha", "Leczy żołądek", null, null, null));
        herbs.add(new HerbModel(2L, "Rumianek", "Matricaria", "Koi nerwy", null, null, null));

        when(herbService.getHerbsByUserId(userId)).thenReturn(herbs);

        // WHEN
        ResponseEntity<List<HerbModel>> response = userHerbController.getHerbsByUserId(userId);

        // THEN
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());

        verify(herbService).getHerbsByUserId(userId);
    }
}