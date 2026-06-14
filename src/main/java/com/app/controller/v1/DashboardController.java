package com.app.controller.v1;

import com.app.dto.v1.ApiResponse;
import com.app.dto.v1.dashboard.SummaryDTO;
import com.app.dto.v1.dashboard.transaction.QueryParamsFilterDailyBalanceDTO;
import com.app.dto.v1.dashboard.transaction.SummaryBudgetDTO;
import com.app.dto.v1.dashboard.transaction.TransactionDailyBalanceDTO;
import com.app.model.User;
import com.app.service.dashboard.DashboardService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    @GetMapping("/summary/budget")
    public ResponseEntity<ApiResponse<SummaryBudgetDTO>> getBudgetSummary(
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity
                .ok()
                .body(
                        ApiResponse.success(
                                service.getBudgetSummary(user)
                        )
                );
    }

    @GetMapping("/transactions/daily-balance")
    public ResponseEntity<ApiResponse<List<TransactionDailyBalanceDTO>>> getDailyBalance(
            @Valid QueryParamsFilterDailyBalanceDTO filters,
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity
                .ok()
                .body(
                        ApiResponse.success(
                                service.getDailyBalance(user, filters)
                        )
                );
    }
}
