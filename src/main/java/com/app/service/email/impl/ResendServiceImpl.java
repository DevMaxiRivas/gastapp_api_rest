package com.app.service.email.impl;

import com.app.dto.v1.email.SendEmailDTO;
import com.app.exception.app.external_service.email.resend.ResendCustomException;
import com.app.service.email.EmailService;
import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.Year;

@Service
@RequiredArgsConstructor
public class ResendServiceImpl implements EmailService {
    private final Resend resend;
    private final TemplateEngine templateEngine;

    @Value("${email.service.email-from}")
    private String from;

    @Value("${spring.application.name}")
    private String appName;

    @Value("${frontend.application.public.url}")
    private String websiteURL;

    @Override
    public void sendEmail(SendEmailDTO email, String templatePath) {
        Context context = new Context();
        context.setVariable("appName", appName);
        context.setVariable("recipientName", email.recipientName());
        context.setVariable("website", websiteURL);
        context.setVariable("currentYear", String.valueOf(Year.now().getValue()));

        String html = templateEngine.process(templatePath, context);

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
