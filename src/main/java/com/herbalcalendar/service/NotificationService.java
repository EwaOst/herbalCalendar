package com.herbalcalendar.service;

import com.herbalcalendar.NotificationHandler;
import com.herbalcalendar.exception.NotificationException;
import com.herbalcalendar.model.HarvestPeriod;
import com.herbalcalendar.model.HerbModel;
import com.herbalcalendar.model.UserModel;
import com.herbalcalendar.repository.HerbRepository;
import com.herbalcalendar.repository.UserRepository;
import java.io.IOException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class NotificationService {
    private final HerbRepository herbRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final NotificationHandler notificationHandler;

    public NotificationService(HerbRepository herbRepository, UserRepository userRepository,
                               EmailService emailService, NotificationHandler notificationHandler) {
        this.herbRepository = herbRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.notificationHandler = notificationHandler;
    }

    @Scheduled(cron = "0 0 8 * * *") // Codziennie o 8 rano
    public void sendHarvestNotifications() throws IOException {
        List<HerbModel> herbs = herbRepository.findAll();
        LocalDate today = LocalDate.now();

        for (HerbModel herb : herbs) {
            if (isHarvestTimeApproaching(herb.getHarvestPeriod(), today)) {
                List<UserModel> users = userRepository.findAll();
                for (UserModel user : users) {
                    sendNotificationToUser(user, herb);
                }
            }
        }
    }

    private void sendNotificationToUser(UserModel user, HerbModel herb) throws NotificationException {
        String message = "Zbiór " + herb.getHerb() + " za 3 dni!";

        try {
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
            }
        } catch (IOException e) {
            throw new NotificationException("Błąd podczas wysyłania powiadomienia", e);
        }
    }

    private boolean isHarvestTimeApproaching(HarvestPeriod harvestPeriod, LocalDate today) {
        return harvestPeriod.getHarvestStartDate().minusDays(3).isEqual(today);
    }
}

