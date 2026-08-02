package com.app.service.email;

import com.app.dto.v1.email.SendEmailDTO;
import org.thymeleaf.context.Context;

public interface EmailService {
    void sendEmail(SendEmailDTO email, String templatePath, Context context);
}
