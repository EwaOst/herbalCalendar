package com.herbalcalendar.service;

import com.herbalcalendar.model.HerbModel;
import com.herbalcalendar.model.UserHerbModel;
import com.herbalcalendar.repository.HerbRepository;
import com.herbalcalendar.repository.UserHerbRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HerbService {

    private final HerbRepository herbRepository;
    private final UserHerbRepository userHerbRepository;

    public HerbService(HerbRepository herbRepository, UserHerbRepository userHerbRepository){
        this.herbRepository = herbRepository;
        this.userHerbRepository = userHerbRepository;
    }

    public List<HerbModel> getAllHerbs() {
        return herbRepository.findAll();
    }

    public HerbModel createHerb(HerbModel herb) {
        return herbRepository.save(herb);
    }

    public Optional<HerbModel> getHerbById(Long id) {
        Optional<HerbModel> herb = herbRepository.findById(id);
        if (herb.isPresent()) {
            return herb;
        } else {
            throw new EntityNotFoundException("Herb not found");
        }
    }

    public HerbModel updateHerb(Long id, HerbModel updateHerb) {
        HerbModel existingHerb = herbRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Herb do not exist"));

        existingHerb.setHerb(updateHerb.getHerb());
        existingHerb.setLatinName(updateHerb.getLatinName());
        existingHerb.setDescription(updateHerb.getDescription());
        existingHerb.setActiveCompoundEnum(updateHerb.getActiveCompoundEnum());
        existingHerb.setHarvestPeriod(updateHerb.getHarvestPeriod());

        return herbRepository.save(existingHerb);
    }

    public void deleteHerb(Long id) {
        if (!herbRepository.existsById(id)) {
            throw new EntityNotFoundException("Herb not found");
        }
        herbRepository.deleteById(id);
    }
    public List<HerbModel> getHerbsByUserId(Long userId) {
        // Pobieramy wszystkie rekordy z tabeli UserHerbModel, które są związane z użytkownikiem
        List<UserHerbModel> userHerbs = userHerbRepository.findByUser_Id(userId);

        // Jeśli nie ma żadnych ziół dla danego użytkownika, rzucamy wyjątek
        if (userHerbs.isEmpty()) {
            throw new EntityNotFoundException("No herbs found for user with ID: " + userId);
        }

        // Mapujemy UserHerbModel na HerbModel
        return userHerbs.stream()
                .map(UserHerbModel::getHerb) // Pobieramy HerbModel z UserHerbModel
                .toList();
    }
}

