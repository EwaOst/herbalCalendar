package com.herbalcalendar.controller;


import com.herbalcalendar.model.HerbModel;
import com.herbalcalendar.service.HerbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;

@RestController
@RequestMapping("/user-herbs")
public class UserHerbController {

    private final HerbService herbService;

    @Autowired
    public UserHerbController(HerbService herbService) {
        this.herbService = herbService;
    }

    @GetMapping("/{userId}/herbs")
    public ResponseEntity<List<HerbModel>> getHerbsByUserId(@PathVariable Long userId) {
        List<HerbModel> herbs = herbService.getHerbsByUserId(userId);
        return ResponseEntity.ok(herbs);
    }
}
