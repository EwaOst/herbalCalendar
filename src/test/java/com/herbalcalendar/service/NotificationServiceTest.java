package com.herbalcalendar.service;

import com.herbalcalendar.NotificationHandler;
import com.herbalcalendar.enums.NotificationPreference;
import com.herbalcalendar.exception.NotificationException;
import com.herbalcalendar.model.HarvestPeriodModel;
import com.herbalcalendar.model.HerbModel;
import com.herbalcalendar.model.UserModel;
import com.herbalcalendar.repository.HerbRepository;
import com.herbalcalendar.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private HerbRepository herbRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmailService emailService;

    @Mock
    private NotificationHandler notificationHandler;

    @InjectMocks
    private NotificationService notificationService;

    @Test
    void sendHarvestNotifications_ShouldSendNotifications_WhenHerbsAndUsersExist() throws NotificationException {
        // GIVEN
        LocalDate today = LocalDate.now();
        LocalDate harvestStartDate = today.plusDays(3);

        // Przygotowanie obiektu HerbModel i HarvestPeriodModel
        HerbModel herb = new HerbModel();
        HarvestPeriodModel period = new HarvestPeriodModel();

        // Ustawienie wartości w modelu period (np. ustawiamy miesiąc na ten, który odpowiada dacie zbiorów)
        period.setHarvestMonth(harvestStartDate.getMonth()); // Ustawiamy miesiąc
        herb.setHerb("Mięta");
        herb.setHarvestPeriod(period); // Ustawiamy okres zbiorów w ziole

        UserModel user = new UserModel();
        user.setId(1L);
        user.setEmail("user@example.com");
        user.setNotificationPreference(NotificationPreference.EMAIL);

        // Mockowanie repozytoriów
        when(herbRepository.findByHarvestPeriod_HarvestMonth(today.plusDays(3).getMonth()))
                .thenReturn(List.of(herb));
        when(userRepository.findByNotificationPreferenceIsNotNull()).thenReturn(List.of(user));

        // WHEN
        notificationService.sendHarvestNotifications();

        // THEN
        verify(emailService).sendEmail(user.getEmail(), "Powiadomienie o zbiorach", "Zbiór Mięta za 3 dni!");
    }

    @Test
    void sendHarvestNotifications_ShouldNotSendNotifications_WhenNoHerbsExist() {
        // GIVEN
        LocalDate today = LocalDate.now();

        when(herbRepository.findByHarvestPeriod_HarvestMonth(today.plusDays(3).getMonth()))
                .thenReturn(Collections.emptyList());

        // WHEN
        notificationService.sendHarvestNotifications();

        // THEN
        verify(emailService, never()).sendEmail(anyString(), anyString(), anyString());
        verify(notificationHandler, never()).sendNotification(anyString());
    }

    @Test
    void sendHarvestNotifications_ShouldNotSendNotifications_WhenNoUsersExist() {
        // GIVEN
        LocalDate today = LocalDate.now();
        LocalDate harvestStartDate = today.plusDays(3);

        HerbModel herb = new HerbModel();
        HarvestPeriodModel period = new HarvestPeriodModel();
        period.setHarvestMonth(harvestStartDate.getMonth()); // Ustaw miesiąc na podstawie daty

        herb.setHarvestPeriod(period); // Przypisz okres zbiorów do zioła

        when(herbRepository.findByHarvestPeriod_HarvestMonth(today.plusDays(3).getMonth()))
                .thenReturn(List.of(herb));
        when(userRepository.findByNotificationPreferenceIsNotNull()).thenReturn(Collections.emptyList());

        // WHEN
        notificationService.sendHarvestNotifications();

        // THEN
        verify(emailService, never()).sendEmail(anyString(), anyString(), anyString());
        verify(notificationHandler, never()).sendNotification(anyString());
    }

    @Test
    void sendNotificationToUser_ShouldSendEmail_WhenPreferenceIsEmail() throws NotificationException {
        // GIVEN
        UserModel user = new UserModel();
        user.setEmail("user@example.com");
        user.setNotificationPreference(NotificationPreference.EMAIL);

        HerbModel herb = new HerbModel();
        herb.setHerb("Mięta");

        // WHEN
        notificationService.sendNotificationToUser(user, herb);

        // THEN
        verify(emailService).sendEmail(user.getEmail(), "Powiadomienie o zbiorach", "Zbiór Mięta za 3 dni!");
        verify(notificationHandler, never()).sendNotification(anyString());
    }

    @Test
    void sendNotificationToUser_ShouldSendAppNotification_WhenPreferenceIsApp() throws NotificationException {
        // GIVEN
        UserModel user = new UserModel();
        user.setNotificationPreference(NotificationPreference.APP);

        HerbModel herb = new HerbModel();
        herb.setHerb("Mięta");

        // Mockowanie sendNotification
        doNothing().when(notificationHandler).sendNotification(anyString());

        // WHEN
        notificationService.sendNotificationToUser(user, herb);

        // THEN
        verify(notificationHandler).sendNotification("Zbiór Mięta za 3 dni!");
        verify(emailService, never()).sendEmail(anyString(), anyString(), anyString());
    }

    @Test
    void sendNotificationToUser_ShouldSendEmailAndAppNotification_WhenPreferenceIsBoth() throws NotificationException {
        // GIVEN
        UserModel user = new UserModel();
        user.setEmail("user@example.com");
        user.setNotificationPreference(NotificationPreference.BOTH);

        HerbModel herb = new HerbModel();
        herb.setHerb("Mięta");

        // Mockowanie sendNotification
        doNothing().when(notificationHandler).sendNotification(anyString());

        // WHEN
        notificationService.sendNotificationToUser(user, herb);

        // THEN
        verify(emailService).sendEmail(user.getEmail(), "Powiadomienie o zbiorach", "Zbiór Mięta za 3 dni!");
        verify(notificationHandler).sendNotification("Zbiór Mięta za 3 dni!");
    }

    @Test
    void sendNotificationToUser_ShouldThrowException_WhenEmailIsMissing() {
        // GIVEN
        UserModel user = new UserModel();
        user.setNotificationPreference(NotificationPreference.EMAIL);

        HerbModel herb = new HerbModel();
        herb.setHerb("Mięta");

        // WHEN + THEN
        NotificationException exception = assertThrows(NotificationException.class, ()
                -> notificationService.sendNotificationToUser(user, herb));

        assertEquals("Użytkownik nie ma ustawionego adresu e-mail", exception.getMessage());
    }

    @Test
    void sendNotificationToUser_ShouldThrowException_WhenPreferenceIsUnknown() {
        // GIVEN
        UserModel user = new UserModel();
        user.setNotificationPreference(null); // Nieznany typ powiadomienia

        HerbModel herb = new HerbModel();
        herb.setHerb("Mięta");

        // WHEN + THEN
        NotificationException exception = assertThrows(NotificationException.class, ()
                -> notificationService.sendNotificationToUser(user, herb));

        assertEquals("Nieznany typ powiadomienia: null", exception.getMessage());
    }
}