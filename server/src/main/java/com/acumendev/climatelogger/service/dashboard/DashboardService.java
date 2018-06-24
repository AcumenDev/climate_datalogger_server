package com.acumendev.climatelogger.service.dashboard;

import com.acumendev.climatelogger.api.dto.dashboard.DashboardDto;
import com.acumendev.climatelogger.repository.DashboardRepository;
import com.acumendev.climatelogger.repository.SensorRepository;
import com.acumendev.climatelogger.repository.dbo.DashboardDbo;
import com.acumendev.climatelogger.repository.dbo.DashboardItemDbo;
import com.acumendev.climatelogger.repository.dbo.SensorDbo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DashboardService {
    private final DashboardRepository dashboardRepository;
    private final SensorRepository sensorRepository;

    public DashboardService(DashboardRepository dashboardRepository, SensorRepository sensorRepository) {
        this.dashboardRepository = dashboardRepository;
        this.sensorRepository = sensorRepository;
    }

    public DashboardDto getDefault(long userId) { ////todo 3 запроса в бд, подумать!
        DashboardDbo dashboardDbo = dashboardRepository.getDefault(userId);
        if (dashboardDbo == null) {
            return null;
        }

        List<DashboardItemDbo> dashboardItemDboList = dashboardRepository.getItems(dashboardDbo.id);

        if (dashboardItemDboList != null) {
            List<SensorDbo> sensorDbos = sensorRepository.getByIds(userId,
                    dashboardItemDboList.stream()
                            .map(dashboardItemDbo -> dashboardItemDbo.sensorId)
                            .collect(Collectors.toList()));


            return new DashboardDto(dashboardDbo.id, dashboardDbo.name, sensorDbos);
        }
        return null;
    }
}
