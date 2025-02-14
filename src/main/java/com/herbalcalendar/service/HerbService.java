package com.herbalcalendar.service;

import com.herbalcalendar.model.Herb;
import com.herbalcalendar.repository.HerbRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HerbService {
    @Autowired
    private HerbRepository herbRepository;

    public List<Herb> getAllHerbs() {
        return herbRepository.findAll();
    }

    public Herb getHerbById(Long id) {
        return herbRepository.findById(id).orElse(null);
    }

    public Herb saveHerb(Herb herb) {
        return herbRepository.save(herb);
    }

    public void deleteHerb(Long id) {
        herbRepository.deleteById(id);
    }
}
