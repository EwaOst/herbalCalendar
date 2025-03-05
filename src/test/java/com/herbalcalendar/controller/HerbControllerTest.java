package com.herbalcalendar.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.herbalcalendar.model.HerbModel;
import com.herbalcalendar.service.HerbService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;


import static org.mockito.Mockito.when;


@AutoConfigureMockMvc
@WebMvcTest(HerbController.class)
class HerbControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @MockBean
    private HerbService herbService;

    private ObjectMapper objectMapper = new ObjectMapper();


    @Test
    void getAllHerbs_ShouldReturnHerbListAndStatusOk() throws Exception {
        // Przygotowanie pustych obiektów bez podawania wartości
        HerbModel herb1 = new HerbModel();
        HerbModel herb2 = new HerbModel();
        List<HerbModel> herbs = List.of(herb1, herb2);

        // Mockowanie serwisu
        when(herbService.getAllHerbs()).thenReturn(herbs);

        // Wysłanie żądania GET i sprawdzenie odpowiedzi
        mockMvc.perform(get("/herbs")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(herbs)));
    }

    @Test
    void createHerb_ShouldCreateHerbAndReturnCreatedStatus() throws Exception {
        HerbModel herb = new HerbModel();
        HerbModel savedHerb = new HerbModel();
        when(herbService.createHerb(any(HerbModel.class))).thenReturn(savedHerb);
        mockMvc.perform(post("/herbs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(herb)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(savedHerb)));

    }

    @Test
    void updateHerb_ShouldUpdateHerbAndReturnNewHerb() throws Exception {
        HerbModel herb = new HerbModel();
        HerbModel newHerb = new HerbModel();
        when(herbService.updateHerb(any(Long.class), any(HerbModel.class))).thenReturn(Optional.of(newHerb));
        mockMvc.perform(put("/herbs/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(herb)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(newHerb)));

    }

    @Test
    void findHerbById_ShouldFindGoalWithProvidedId() throws Exception {
        HerbModel foundHerb = new HerbModel();
        foundHerb.setId(1L);
        when(herbService.getHerbById(any(Long.class))).thenReturn(Optional.of(foundHerb));
        mockMvc.perform(get("/herbs/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(foundHerb)));
    }

    @Test
    void findHerbById_ShouldReturnNotFoundOrNonExistingHerb() throws Exception {
        when(herbService.getHerbById(any(Long.class))).thenReturn(Optional.empty());
        mockMvc.perform(get("/herbs/{id}", 2L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

    @Test
    void deleteHerb_ShouldDeleteWithProvidedId() throws Exception {
        Long herbId = 1L;
        doNothing().when(herbService).deleteHerb(any(Long.class));
        mockMvc.perform(delete("/herbs/{id}", herbId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}



