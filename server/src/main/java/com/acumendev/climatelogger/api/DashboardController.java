package com.acumendev.climatelogger.api;

import com.acumendev.climatelogger.api.dto.BaseResponse;
import com.acumendev.climatelogger.api.dto.dashboard.DashboardItemDto;
import com.acumendev.climatelogger.service.dashboard.DashboardService;
import com.acumendev.climatelogger.type.CurrentUser;
import com.acumendev.climatelogger.utils.SecurityUtils;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    public BaseResponse get() {
        return BaseResponse.ok(dashboardService.getDefault( SecurityUtils.getUser().getId()));
    }

    @PostMapping("/api/dashboard")
    public BaseResponse create(@RequestBody String name) {
        dashboardService.create(SecurityUtils.getUser().getId(), name);
        return BaseResponse.ok();
    }

    @PostMapping("/api/dashbord/item")
    public BaseResponse addItem(@RequestBody DashboardItemDto itemDto) {
        dashboardService.addItem(SecurityUtils.getUser().getId(), itemDto);
        return BaseResponse.ok();
    }



}
