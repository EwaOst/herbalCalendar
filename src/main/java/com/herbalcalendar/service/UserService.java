package com.herbalcalendar.service;

import com.herbalcalendar.model.UserModel;
import com.herbalcalendar.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
@RequiredArgsConstructor

public class UserService {

    @Autowired
    private UserRepository userRepository;

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

}

