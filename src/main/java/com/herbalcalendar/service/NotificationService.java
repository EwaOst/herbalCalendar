package com.herbalcalendar.service;

import com.herbalcalendar.model.HerbModel;
import com.herbalcalendar.model.UserModel;
import com.herbalcalendar.repository.HerbRepository;
import com.herbalcalendar.repository.UserRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class NotificationService {
    private final HerbRepository herbRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    public NotificationService(HerbRepository herbRepository, UserRepository userRepository, EmailService emailService) {
        this.herbRepository = herbRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    @Scheduled(cron = "0 0 8 * * *") // Uruchamia się codziennie o 8 rano
    public void sendHarvestNotifications() {
        List<HerbModel> herbs = herbRepository.findAll();
        LocalDate today = LocalDate.now();

        for (HerbModel herb : herbs) {
            if (isHarvestTimeApproaching(herb, today)) {
                List<UserModel> users = userRepository.findAll();
                for (UserModel user : users) {
                    emailService.sendEmail(
                            user.getEmail(),
                            "Zbiór ziół: " + herb.getHerb(),
                            "To najlepszy czas na zbieranie " + herb.getHerb() + "!"
                    );
                }
            }
        }
    }
    private boolean isHarvestTimeApproaching(HerbModel herb, LocalDate today) {
        return herb.getHarvestPeriod() != null &&
                herb.getHarvestPeriod().getHarvestStartDate() != null &&
                herb.getHarvestPeriod().getHarvestStartDate().minusDays(3).isEqual(today);
    }
}

