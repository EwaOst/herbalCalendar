package com.herbalcalendar.controller;

import com.herbalcalendar.model.Herb;
import com.herbalcalendar.service.HerbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/herbs")
public class HerbController {
    @Autowired
    private HerbService herbService;

    @GetMapping
    public List<Herb> getAllHerbs() {
        return herbService.getAllHerbs();
    }

    @GetMapping("/{id}")
    public Herb getHerbById(@PathVariable Long id) {
        return herbService.getHerbById(id);
    }

    @PostMapping
    public Herb createHerb(@RequestBody Herb herb) {
        return herbService.saveHerb(herb);
    }

    @DeleteMapping("/{id}")
    public void deleteHerb(@PathVariable Long id) {
        herbService.deleteHerb(id);
    }
}
