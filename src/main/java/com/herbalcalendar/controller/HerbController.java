package com.herbalcalendar.controller;

import com.herbalcalendar.model.HerbModel;
import com.herbalcalendar.service.HerbService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/herbs")
public class HerbController {


    private HerbService herbService;

    @GetMapping
    public List<HerbModel> getAllHerbs() {
        return herbService.getAllHerbs();
    }

    @PostMapping
    public ResponseEntity<HerbModel> createHerb(@Valid @RequestBody HerbModel herbModel) {
        HerbModel newHerb = herbService.createHerb(herbModel);
        return new ResponseEntity<>(newHerb, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HerbModel> getHerbById(@PathVariable Long id) {
        return herbService.getHerbById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());

    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateHerb(@PathVariable Long id, @RequestBody HerbModel herb) {
        return herbService.updateHerb(id, herb)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound()
                        .build());
    }

   @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHerb (@PathVariable Long id) {
        herbService.deleteHerb(id);
            return ResponseEntity.noContent().build();
    }
}








