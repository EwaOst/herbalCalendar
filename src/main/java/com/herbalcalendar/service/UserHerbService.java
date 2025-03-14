package com.herbalcalendar.service;

import com.herbalcalendar.model.HerbModel;
import com.herbalcalendar.model.UserHerbModel;
import com.herbalcalendar.repository.UserHerbRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class UserHerbService {
    private final UserHerbRepository userHerbRepository;


    public List<HerbModel> getHerbsByUserId(Long userId) {
        List<UserHerbModel> userHerbs = userHerbRepository.findByUser_Id(userId);
        return userHerbs.stream()
                .map(UserHerbModel::getHerb)
                .toList();
    }
}
