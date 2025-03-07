package com.herbalcalendar.service;

import com.herbalcalendar.model.HerbModel;
import com.herbalcalendar.model.UserHerbModel;
import com.herbalcalendar.model.UserModel;
import com.herbalcalendar.repository.HerbRepository;
import com.herbalcalendar.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
@RequiredArgsConstructor

public class UserService {

    @Autowired
    private UserRepository userRepository;
    private final HerbRepository herbRepository;
    private PasswordEncoder passwordEncoder;

    public List<UserModel> getAllUsers() {
        return userRepository.findAll();
    }

    public UserModel createUser(UserModel user) {
        UserModel newUser = new UserModel();
        newUser = userRepository.save(user);
        return newUser;
    }

    public Optional<UserModel> getUserById(Long id) {
        Optional<UserModel> user = userRepository.findById(id);
        if (user.isPresent()) {
            return user;
        } else {
            throw new EntityNotFoundException("User not found");
        }
    }
    public Optional<UserModel> updateUser(Long id, UserModel updateUser) {
        return Optional.ofNullable(userRepository.findById(id)
                .map(user -> {
                    user.setUsername(updateUser.getUsername());
                    user.setEmail(updateUser.getEmail());
                    user.setPassword(updateUser.getPassword());
                    user.setActive(updateUser.isActive());
                    return userRepository.save(user);
                })
                .orElseThrow(() -> new RuntimeException("User do not exist")));

    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("User not found");
        }
        userRepository.deleteById(id);
    }


    public UserModel addHerbToUser(Long userId, Long herbId) {
        // Pobierz użytkownika i zioło z bazy danych
        UserModel user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        HerbModel herb = herbRepository.findById(herbId)
                .orElseThrow(() -> new RuntimeException("Herb not found"));

        // Utwórz encję UserHerb
        UserHerbModel userHerb = new UserHerbModel();
        userHerb.setUser(user);
        userHerb.setHerb(herb);

        // Dodaj UserHerb do listy użytkownika
        user.getUserHerbs().add(userHerb);

        // Zapisz zmiany w bazie danych
        return userRepository.save(user);
    }

    public UserModel authenticate(String username, String password) {
        UserModel user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        return user;
    }

}

