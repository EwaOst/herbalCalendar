package com.herbalcalendar.service;

import com.herbalcalendar.model.HerbModel;
import com.herbalcalendar.repository.HerbRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HerbService {
    @Autowired
    private HerbRepository herbRepository;

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
                    herb.setName(updateHerb.getName());
                    herb.setDescription(updateHerb.getDescription());
                    herb.setActiveCompounds(updateHerb.getActiveCompounds());
                    herb.setHarvestTime(updateHerb.getHarvestTime());
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
}
