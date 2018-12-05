package com.acumendev.climatelogger.api;

import com.acumendev.climatelogger.api.dto.BaseResponse;
import com.acumendev.climatelogger.api.dto.dashboardValue.BatchValuesDto;
import com.acumendev.climatelogger.service.dashboard.DashboardValueService;
import com.acumendev.climatelogger.utils.SecurityUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DashboardValuesController { ////todo может другое название?

    private final DashboardValueService dashboardValueService;

    public DashboardValuesController(DashboardValueService dashboardValueService) {
        this.dashboardValueService = dashboardValueService;
    }

    @PostMapping("/api/dashboard/values/get")
    public BaseResponse getValues(@RequestBody BatchValuesDto batchValuesDto) {
        return BaseResponse.ok(dashboardValueService.getItemsValue(SecurityUtils.getUser(), batchValuesDto));
    }
}
