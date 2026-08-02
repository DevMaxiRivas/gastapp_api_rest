package com.app.listener;

import com.app.dto.v1.email.SendEmailDTO;
import com.app.event.auth.ResetPasswordTokenConfirmEvent;
import com.app.event.auth.ResetPasswordTokenCreateEvent;
import com.app.model.User;
import com.app.service.email.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;

@Component
@RequiredArgsConstructor
public class ResetPasswordServiceListener {
    private final EmailService emailService;
    private final String ENDPOINT_RESET_PASSWORD = "/reset-password?token=:token";

    @Async
    @EventListener
    public void handleResetPasswordTokenCreate(ResetPasswordTokenCreateEvent event){
        User user = event.token().getUser();
        emailService.sendEmail(
                new SendEmailDTO(
                    user.getEmail(),
                    user.getUsername(),
                    "Forgot your password?",
                    ENDPOINT_RESET_PASSWORD.replace(":token", event.token().getToken())
                ),
                "email/auth/forgot-password",
                new Context()
        );
    }

    @Async
    @EventListener
    public void handleResetPasswordTokenConfirm(ResetPasswordTokenConfirmEvent event){
        User user = event.token().getUser();
        emailService.sendEmail(
                new SendEmailDTO(
                        user.getEmail(),
                        user.getUsername(),
                        "Forgot your password?",
                        null
                ),
                "email/auth/reset-password",
                new Context()
        );
    }
}
