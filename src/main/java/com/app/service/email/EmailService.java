package com.app.service.email;

import com.app.dto.v1.email.SendEmailDTO;

public interface EmailService {
    void sendEmail(SendEmailDTO email);
}
