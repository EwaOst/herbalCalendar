package com.herbalcalendar.service;

import static org.mockito.ArgumentMatchers.any;

import com.herbalcalendar.model.HerbModel;
import com.herbalcalendar.model.UserHerbModel;
import com.herbalcalendar.model.UserModel;
import com.herbalcalendar.repository.HerbRepository;

import com.herbalcalendar.repository.UserRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private HerbRepository herbRepository;
    @Mock
    private UserHerbModel userHerbModel;
    @Mock
    private HerbModel herbModel;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserService userService;

    @Test
    void getAllUsers() {
        UserModel user1 = new UserModel(
                1L, "Ewa", "ewaoster@muster.pl", "12552", true, new Date(), new ArrayList<>()
        );
        UserModel user2 = new UserModel(
                2L, "alanb", "awe@o2.pl", "dffd", false, new Date(), new ArrayList<>()
        );
        List<UserModel> expectedUsers = Arrays.asList(user1, user2);

        when(userRepository.findAll()).thenReturn(expectedUsers);

        List<UserModel> actualUsers = userService.getAllUsers();

        assertNotNull(actualUsers);
        assertEquals(2, actualUsers.size());
        assertEquals(expectedUsers, actualUsers);
    }

    @Test
    void createUser() {
        UserModel userModel = new UserModel();

        userModel.setUsername("John");
        userModel.setEmail("john@o2.pl");
        userModel.setPassword("dfsdf");
        userModel.setActive(true);
        userModel.setCreatedAt(new Date());
        userModel.setUserHerbs(new ArrayList<>());

        when(userRepository.save(any(UserModel.class))).thenReturn(userModel);
        UserModel result = userService.createUser(userModel);

        assertNotNull(result);
        assertEquals("John", result.getUsername());
        assertEquals("john@o2.pl", result.getEmail());
        assertEquals("dfsdf", result.getPassword());
        assertEquals(true, result.isActive());
        assertEquals(userModel.getCreatedAt().getTime(), result.getCreatedAt().getTime());

        assertTrue(result.getUserHerbs().isEmpty());
    }

    @Test
    void getUserById() {

        UserModel userModel = new UserModel();

        userModel.setId(1L);
        userModel.setUsername("Ewa");

        when(userRepository.findById(1L)).thenReturn(Optional.of(userModel));
        Optional<UserModel> result = userService.getUserById(1L);

        assertTrue(result.isPresent());
        assertEquals("Ewa", result.get().getUsername());
    }

    @Test
    void updateUser() {
        Long userId = 1L;
        UserModel existingUser = new UserModel();

        existingUser.setId(userId);
        existingUser.setUsername("Ewa");
        existingUser.setEmail("asd@o2.pl");
        existingUser.setPassword("2131");
        existingUser.setActive(true);


        UserModel updateUser = new UserModel();

        updateUser.setUsername("John");
        updateUser.setEmail("asw@o2.pl");
        updateUser.setPassword("4654");
        updateUser.setActive(false);


        when(userRepository.findById((userId))).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Optional<UserModel> result = userService.updateUser(userId, updateUser);

        assertEquals(updateUser.getUsername(), result.get().getUsername());
        assertEquals(updateUser.getEmail(), result.get().getEmail());
        assertEquals(updateUser.getPassword(), result.get().getPassword());
        assertEquals(updateUser.isActive(), result.get().isActive());

    }

    @Test
    void deleteUser() {
        Long id = 1L;

        when(userRepository.existsById(id)).thenReturn(true);

        userService.deleteUser(id);

        verify(userRepository).deleteById(id);
    }

    @Test
    void addHerbToUser() {
        // GIVEN
        Long userId = 1L;
        Long herbId = 1L;

        // Przygotowanie użytkownika
        UserModel user = new UserModel();
        user.setId(userId);
        user.setUsername("Ewa");
        user.setEmail("ewa@example.com");
        user.setPassword("password");
        user.setActive(true);
        user.setUserHerbs(new ArrayList<>()); // Inicjalizacja pustej listy

        // Przygotowanie zioła
        HerbModel herb = new HerbModel();
        herb.setId(herbId);
        herb.setHerb("Mięta");
        herb.setLatinName("Mentha");
        herb.setDescription("Leczy żołądek");

        // Symulacja zachowania repozytoriów
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(herbRepository.findById(herbId)).thenReturn(Optional.of(herb));
        when(userRepository.save(any(UserModel.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // WHEN
        UserModel result = userService.addHerbToUser(userId, herbId);

        // THEN
        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertEquals(1, result.getUserHerbs().size()); // Sprawdzenie, czy zioło zostało dodane

        UserHerbModel userHerb = result.getUserHerbs().get(0);
        assertNotNull(userHerb);
        assertEquals(user, userHerb.getUser());
        assertEquals(herb, userHerb.getHerb());

        // Weryfikacja, czy metody repozytoriów zostały wywołane
        verify(userRepository).findById(userId);
        verify(herbRepository).findById(herbId);
        verify(userRepository).save(user);
    }

    @Test
    void authenticate() {
        // GIVEN
        String username = "Ewa";
        String password = "password123";
        String encodedPassword = "$2a$10$..."; // Zakodowane hasło

        UserModel user = new UserModel();
        user.setUsername(username);
        user.setPassword(encodedPassword);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, encodedPassword)).thenReturn(true);

        // WHEN
        UserModel result = userService.authenticate(username, password);

        // THEN
        assertNotNull(result);
        assertEquals(username, result.getUsername());
        assertEquals(encodedPassword, result.getPassword());

        verify(userRepository).findByUsername(username);
        verify(passwordEncoder).matches(password, encodedPassword);
    }

    @Test
    void authenticate_WhenUserNotFound_ShouldThrowException() {
        // GIVEN
        String username = "NonExistentUser";
        String password = "password123";

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // WHEN + THEN
        assertThrows(RuntimeException.class, () -> userService.authenticate(username, password));

        verify(userRepository).findByUsername(username);
        verify(passwordEncoder, never()).matches(any(), any()); // passwordEncoder nie powinno być wywołane
    }

    @Test
    void authenticate_WhenPasswordDoesNotMatch_ShouldThrowException() {
        // GIVEN
        String username = "Ewa";
        String password = "wrongPassword";
        String encodedPassword = "$2a$10$..."; // Zakodowane hasło

        UserModel user = new UserModel();
        user.setUsername(username);
        user.setPassword(encodedPassword);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, encodedPassword)).thenReturn(false);

        // WHEN + THEN
        assertThrows(RuntimeException.class, () -> userService.authenticate(username, password));

        verify(userRepository).findByUsername(username);
        verify(passwordEncoder).matches(password, encodedPassword);
    }
}

