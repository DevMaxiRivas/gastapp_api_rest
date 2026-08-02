package com.app.service.auth;

import com.app.dto.v1.auth.ConfirmResetPasswordRequest;
import com.app.event.auth.ResetPasswordTokenConfirmEvent;
import com.app.event.auth.ResetPasswordTokenCreateEvent;
import com.app.exception.body.ValidationRequestBodyCustomException;
import com.app.model.PasswordResetToken;
import com.app.model.User;
import com.app.repository.PasswordResetTokenRepository;
import com.app.service.user.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class ResetPasswordService {
    private final PasswordResetTokenRepository repo;
    private static final long EXPIRATION_TIME = 30L;

    private final UserService userService;

    private final ApplicationEventPublisher eventPublisher;

    private static final SecureRandom secureRandom = new SecureRandom();
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder().withoutPadding();

    private String makeResetToken() {
        byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }

    @Transactional
    public void forgotPassword(String email) {
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new ValidationRequestBodyCustomException(
                        "email: No users with this email address were found.",
                        "body.email"
                ));

        PasswordResetToken token = PasswordResetToken.builder()
                .expiredAt(LocalDate.now().atStartOfDay().plusMinutes(EXPIRATION_TIME))
                .token(makeResetToken())
                .user(user)
                .build();

        repo.save(token);

        eventPublisher.publishEvent(
                new ResetPasswordTokenCreateEvent(token)
        );
    }

    @Transactional(dontRollbackOn = ValidationRequestBodyCustomException.class)
    public PasswordResetToken validateToken(String token) {
        PasswordResetToken resetToken = repo.findByToken(token)
                .orElseThrow(() -> new ValidationRequestBodyCustomException(
                "token: No users with this token were found.",
                "query_param.token"
        ));

        if (resetToken.isExpired()) {
            repo.delete(resetToken);
            throw new ValidationRequestBodyCustomException(
                    "token: The token has already expired",
                    "query_param.token"
            );
        }

        return resetToken;
    }

    @Transactional
    public void resetPassword(ConfirmResetPasswordRequest request) {
        PasswordResetToken resetToken = validateToken(request.token());

        User user = resetToken.getUser();
        userService.resetPasswordUser(user, request);

        eventPublisher.publishEvent(
                new ResetPasswordTokenConfirmEvent(resetToken)
        );

        repo.delete(resetToken);

    }
}
