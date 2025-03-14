package com.herbalcalendar.service;

import com.herbalcalendar.exception.ForbiddenException;
import com.herbalcalendar.exception.UserAlreadyExistsException;
import com.herbalcalendar.model.HerbModel;
import com.herbalcalendar.model.UserHerbModel;
import com.herbalcalendar.model.UserModel;
import com.herbalcalendar.repository.HerbRepository;
import com.herbalcalendar.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final HerbRepository herbRepository;
    private final PasswordEncoder passwordEncoder;

    // Konstruktor wstrzykujący zależności
    public UserService(UserRepository userRepository, HerbRepository herbRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.herbRepository = herbRepository;
        this.passwordEncoder = passwordEncoder;
    }

    private static final String USER_NOT_FOUND = "User not found";

    public List<UserModel> getAllUsers() {
        return userRepository.findAll();
    }

    public UserModel createUser(UserModel userModel) {
        // Sprawdzenie, czy użytkownik z tym adresem e-mail już istnieje
        if (userRepository.existsByEmail(userModel.getEmail())) {
            // Rzucenie wyjątku, jeśli użytkownik już istnieje
            throw new UserAlreadyExistsException("User already exists with email: " + userModel.getEmail());
        }

        // Jeśli użytkownik nie istnieje, tworzymy nowego użytkownika
        return userRepository.save(userModel);
    }

    public Optional<UserModel> getUserById(Long id) {
        Optional<UserModel> user = userRepository.findById(id);
        if (user.isPresent()) {
            return user;
        } else {
            throw new EntityNotFoundException(USER_NOT_FOUND);
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
                .orElseThrow(() -> new EntityNotFoundException("User do not exist")));
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException(USER_NOT_FOUND);
        }
        userRepository.deleteById(id);
    }


    public UserModel addHerbToUser(Long userId, Long herbId) {
        // Pobierz użytkownika i zioło z bazy danych
        UserModel user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND));
        HerbModel herb = herbRepository.findById(herbId)
                .orElseThrow(() -> new EntityNotFoundException("Herb not found"));

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
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new ForbiddenException("Invalid password");
        }

        return user;
    }

}

