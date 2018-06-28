package com.acumendev.climatelogger.api;

import com.acumendev.climatelogger.api.dto.BaseResponse;
import com.acumendev.climatelogger.service.dashboard.DashboardService;
import com.acumendev.climatelogger.type.CurrentUser;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by vladimir akummail@gmail.com on 6/13/18.
 */

@RestController
public class DashboardController {
    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/api/dashboard")
    public BaseResponse get(@AuthenticationPrincipal CurrentUser user) {
        return BaseResponse.ok(dashboardService.getDefault(user.getId()));
    }
}
