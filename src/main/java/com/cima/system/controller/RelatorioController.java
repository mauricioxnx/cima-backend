package com.cima.system.controller;

import com.cima.system.dto.response.ApiResponse;
import com.cima.system.dto.response.DashboardResponse;
import com.cima.system.service.RelatorioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/relatorios")
@RequiredArgsConstructor
@Tag(name = "Relatórios", description = "RF10 – Gerar relatórios")
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasAuthority('ADMINISTRADOR')")
public class RelatorioController {

    private final RelatorioService relatorioService;

    @GetMapping("/dashboard")
    @Operation(summary = "Resumo geral do sistema (dashboard)")
    public ResponseEntity<ApiResponse<DashboardResponse>> dashboard() {
        return ResponseEntity.ok(ApiResponse.sucesso(relatorioService.obterDashboard()));
    }

    @GetMapping("/inventario/resumo")
    @Operation(summary = "Relatório de resumo do inventário")
    public ResponseEntity<ApiResponse<Map<String, Object>>> relatorioInventario() {
        return ResponseEntity.ok(ApiResponse.sucesso(relatorioService.relatorioInventario()));
    }

    @GetMapping("/manutencao/resumo")
    @Operation(summary = "Relatório de manutenções por período")
    public ResponseEntity<ApiResponse<Map<String, Object>>> relatorioManutencao(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        return ResponseEntity.ok(ApiResponse.sucesso(relatorioService.relatorioManutencoes(inicio, fim)));
    }

    @GetMapping("/stock/movimentos")
    @Operation(summary = "Relatório de movimentos de stock por período")
    public ResponseEntity<ApiResponse<Map<String, Object>>> relatorioMovimentos(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        return ResponseEntity.ok(ApiResponse.sucesso(relatorioService.relatorioMovimentosStock(inicio, fim)));
    }

    @GetMapping("/utilizadores/resumo")
    @Operation(summary = "Relatório de utilizadores por perfil")
    public ResponseEntity<ApiResponse<Map<String, Object>>> relatorioUtilizadores() {
        return ResponseEntity.ok(ApiResponse.sucesso(relatorioService.relatorioUtilizadores()));
    }
}
