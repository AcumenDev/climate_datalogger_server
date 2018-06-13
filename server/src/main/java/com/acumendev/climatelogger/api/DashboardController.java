package com.acumendev.climatelogger.api;

import com.acumendev.climatelogger.api.dto.BaseResponse;
import com.acumendev.climatelogger.repository.DashboardRepository;
import com.acumendev.climatelogger.type.CurrentUser;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by vladimir akummail@gmail.com on 6/13/18.
 */
@RestController
public class DashboardController {
    private final DashboardRepository dashboardRepository;

    public DashboardController(DashboardRepository dashboardRepository) {
        this.dashboardRepository = dashboardRepository;
    }

    public BaseResponse get(@AuthenticationPrincipal CurrentUser user) {

        return BaseResponse.ok(dashboardRepository.getDefault(user.getId()));
    }
}
