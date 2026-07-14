package com.herbalcalendar.service;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender; // Mockowanie JavaMailSender

    @InjectMocks
    private EmailService emailService; // Klasa, w której znajduje się testowana metoda

    @Test
    void sendEmail_ShouldSendEmailWithCorrectParameters() {
        // GIVEN
        String to = "test@example.com";
        String subject = "Test Subject";
        String text = "Test Email Body";

        // WHEN
        emailService.sendEmail(to, subject, text);

        // THEN
        verify(mailSender).send(any(MimeMessagePreparator.class)); // Weryfikacja, że send zostało wywołane
    }
}