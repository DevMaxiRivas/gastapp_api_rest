package com.app.listener;

import com.app.dto.v1.email.SendEmailDTO;
import com.app.event.auth.UserCreatedEvent;
import com.app.service.email.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailServiceListener {
    private final EmailService emailService;

    @Async
    @EventListener
    public void handleUserCreatedEvent(UserCreatedEvent event) {
        SendEmailDTO email = new SendEmailDTO(
            event.user().email(),
            event.user().username(),
            "You just signed up"
        );

        emailService.sendEmail(email, "email/auth/register");
    }
}
