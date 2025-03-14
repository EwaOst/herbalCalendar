package com.herbalcalendar.controller;

import com.herbalcalendar.model.HerbModel;
import com.herbalcalendar.service.HerbService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/herbs")
public class HerbController {


    private HerbService herbService;

    public  HerbController(HerbService herbService){
        this.herbService = herbService;
    }

    @GetMapping
    public ResponseEntity<List<HerbModel>> getAllHerbs() {
        List<HerbModel> herbs = herbService.getAllHerbs();
        return ResponseEntity.ok(herbs);
    }

    @PostMapping
    public ResponseEntity<HerbModel> createHerb(@Valid @RequestBody HerbModel herbModel) {
        HerbModel newHerb = herbService.createHerb(herbModel);
        return new ResponseEntity<>(newHerb, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HerbModel> updateHerb(@PathVariable Long id, @RequestBody HerbModel herb) {
        // Wywołanie serwisu, który może rzucić wyjątek HerbNotFoundException
        HerbModel updatedHerb = herbService.updateHerb(id, herb);

        // Jeśli herb został zaktualizowany, zwracamy go w odpowiedzi
        return ResponseEntity.ok(updatedHerb);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HerbModel> getHerbById(@PathVariable Long id) {
        return herbService.getHerbById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound()
                        .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHerb(@PathVariable Long id) {
        herbService.deleteHerb(id);
        return ResponseEntity.noContent()
                .build();
    }

    @GetMapping("/{userId}/herbs")
    public ResponseEntity<List<HerbModel>> getHerbsByUserId(@PathVariable Long userId) {
        List<HerbModel> herbs = herbService.getHerbsByUserId(userId);
        return ResponseEntity.ok(herbs);
    }
}








