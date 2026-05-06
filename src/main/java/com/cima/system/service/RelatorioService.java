package com.cima.system.service;

import com.cima.system.dto.response.DashboardResponse;

import java.time.LocalDate;
import java.util.Map;

public interface RelatorioService {
    DashboardResponse obterDashboard();
    Map<String, Object> relatorioInventario();
    Map<String, Object> relatorioManutencoes(LocalDate inicio, LocalDate fim);
    Map<String, Object> relatorioMovimentosStock(LocalDate inicio, LocalDate fim);
    Map<String, Object> relatorioUtilizadores();
}
