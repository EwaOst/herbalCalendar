package com.herbalcalendar.service;

import com.herbalcalendar.NotificationHandler;
import com.herbalcalendar.enums.NotificationPreference;
import com.herbalcalendar.exception.NotificationException;
import com.herbalcalendar.model.HerbModel;
import com.herbalcalendar.model.UserModel;
import com.herbalcalendar.repository.HerbRepository;
import com.herbalcalendar.repository.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@Service
public class NotificationService {
    private final HerbRepository herbRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final NotificationHandler notificationHandler;
    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    public NotificationService(HerbRepository herbRepository, UserRepository userRepository,
                               EmailService emailService, NotificationHandler notificationHandler) {
        this.herbRepository = herbRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.notificationHandler = notificationHandler;
    }

    @Scheduled(cron = "0 0 8 * * *") // Codziennie o 8 rano
    public void sendHarvestNotifications() {
        LocalDate today = LocalDate.now();

        // Pobierz tylko te zioła, których czas zbioru zbliża się
        List<HerbModel> herbs = herbRepository.findByHarvestPeriod_HarvestMonth(Month.JUNE);

        for (HerbModel herb : herbs) {
            // Pobierz tylko tych użytkowników, którzy mają aktywne powiadomienia
            List<UserModel> users = userRepository.findByNotificationPreferenceIsNotNull();
            for (UserModel user : users) {
                try {
                    sendNotificationToUser(user, herb);
                } catch (NotificationException e) {
                    // Logowanie błędu bez przerywania pętli
                    logger.error("Błąd podczas wysyłania powiadomienia do użytkownika: {} - {}", user.getId(), e.getMessage(), e);
                }
            }
        }
    }

    public void sendNotificationToUser(UserModel user, HerbModel herb) throws NotificationException {
        if (user.getNotificationPreference() == null) {
            throw new NotificationException("Nieznany typ powiadomienia: null");
        }

        if (user.getEmail() == null && (user.getNotificationPreference() == NotificationPreference.EMAIL || user.getNotificationPreference() == NotificationPreference.BOTH)) {
            throw new NotificationException("Użytkownik nie ma ustawionego adresu e-mail");
        }

        String message = "Zbiór " + herb.getHerb() + " za 3 dni!";

        switch (user.getNotificationPreference()) {
            case EMAIL:
                emailService.sendEmail(user.getEmail(), "Powiadomienie o zbiorach", message);
                break;
            case APP:
                notificationHandler.sendNotification(message);
                break;
            case BOTH:
                emailService.sendEmail(user.getEmail(), "Powiadomienie o zbiorach", message);
                notificationHandler.sendNotification(message);
                break;
            default:
                throw new NotificationException("Nieznany typ powiadomienia: " + user.getNotificationPreference());
        }
    }
}