package com.app.controller.v1;

import com.app.model.User;
import com.app.service.SSE.SseNotificationService;
import com.app.service.dashboard.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@RestController
@RequestMapping("api/v1/stream-sse")
@RequiredArgsConstructor
public class SSEDashboardController {

    private final SseNotificationService sseService;
    private final DashboardService dashboardService;

    @GetMapping(value = "/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @PreAuthorize("isAuthenticated()")
    public SseEmitter streamSseEvents(
            @AuthenticationPrincipal User user
    ) {
        String userId = user.getId().toString();

        // % minutes available
        SseEmitter emitter = new SseEmitter(300000L);
        sseService.addEmitter(userId, emitter);

        sendInitData(userId, emitter);

        return emitter;
    }

    private void sendInitData(String userId, SseEmitter emitter) {
        try {
            emitter.send(SseEmitter.event().name("connection-established").data("Connection have been establish for user with id " + userId));
        } catch (IOException e) {
            emitter.completeWithError(e);
        }
    }
}
