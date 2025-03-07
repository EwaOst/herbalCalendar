package com.herbalcalendar.service;

import com.herbalcalendar.model.HerbModel;
import com.herbalcalendar.model.UserHerbModel;
import com.herbalcalendar.repository.HerbRepository;
import com.herbalcalendar.repository.UserHerbRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HerbService {
    @Autowired
    private HerbRepository herbRepository;
    private final UserHerbRepository userHerbRepository;

    public List<HerbModel> getAllHerbs() {
        return herbRepository.findAll();
    }

    public HerbModel createHerb(HerbModel herb) {
        HerbModel newHerb = new HerbModel();
        newHerb = herbRepository.save(herb);
        return newHerb;
    }

    public Optional<HerbModel> getHerbById(Long id) {
        Optional<HerbModel> herb = herbRepository.findById(id);
        if (herb.isPresent()) {
            return herb;
        } else {
            throw new EntityNotFoundException("Herb not found");
        }
    }

    public Optional<HerbModel> updateHerb(Long id, HerbModel updateHerb) {
        return Optional.ofNullable(herbRepository.findById(id)
                .map(herb -> {
                    herb.setLatinName(updateHerb.getLatinName());
                    herb.setDescription(updateHerb.getDescription());
                    herb.setActiveCompoundEnum(updateHerb.getActiveCompoundEnum());
                    herb.setHarvestPeriod(updateHerb.getHarvestPeriod());
                    return herbRepository.save(herb);
                })
                .orElseThrow(() -> new RuntimeException("Herb do not exists")));
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
                .collect(Collectors.toList());
    }

}

