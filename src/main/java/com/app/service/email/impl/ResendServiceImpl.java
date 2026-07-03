package com.app.service.email.impl;

import com.app.dto.v1.email.SendEmailDTO;
import com.app.exception.app.external_service.email.resend.ResendCustomException;
import com.app.service.email.EmailService;
import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@RequiredArgsConstructor
public class ResendServiceImpl implements EmailService {
    private final Resend resend;
    private final TemplateEngine templateEngine;

    @Value("${email.service.email-from}")
    private String from;

    @Override
    public void sendEmail(SendEmailDTO email) {
        Context context = new Context();
        context.setVariable("recipientName", email.to());
        context.setVariable("message", email.body());

        String html = email.body();

        if(email.templatePath() != null && !email.templatePath().isEmpty()) {
             html = templateEngine.process(email.templatePath(), context);
        }

        var params = CreateEmailOptions.builder()
                .from(from)
                .to(email.to())
                .subject(email.subject())
                .html(html)
                .build();

        try {
            resend.emails().send(params);
        } catch (ResendException e) {
            throw new ResendCustomException(e);
        }
    }
}
