package com.app.controller.v1;

import com.app.dto.v1.ApiResponse;
import com.app.dto.v1.dashboard.SummaryDTO;
import com.app.model.User;
import com.app.service.dashboard.DashboardService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/dashboard")
@AllArgsConstructor
public class DashboardController {
    private final DashboardService service;

    @GetMapping("/summary")
    public ResponseEntity<ApiResponse<SummaryDTO>> getSummary(
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity
                .ok()
                .body(
                        ApiResponse.success(
                                service.getSummary(user)
                        )
                );
    }
}
