package com.app.service.dashboard;

import com.app.dto.v1.dashboard.SummaryDTO;
import com.app.model.User;

public interface DashboardService {
    SummaryDTO getSummary(User user);
}
