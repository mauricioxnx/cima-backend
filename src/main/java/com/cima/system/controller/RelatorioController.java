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
// ✅ Remove o @PreAuthorize da classe — cada método define as suas permissões
public class RelatorioController {

    private final RelatorioService relatorioService;

    // ✅ Dashboard acessível a todos os perfis
    @GetMapping("/dashboard")
    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR','GERENTE_STOCK','GERENTE_MANUTENCAO','TECNICO')")
    @Operation(summary = "Resumo geral do sistema (dashboard)")
    public ResponseEntity<ApiResponse<DashboardResponse>> dashboard() {
        return ResponseEntity.ok(ApiResponse.sucesso(relatorioService.obterDashboard()));
    }

    // ✅ Inventário acessível a ADMINISTRADOR e GERENTE_STOCK
    @GetMapping("/inventario/resumo")
    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR','GERENTE_STOCK')")
    @Operation(summary = "Relatório de resumo do inventário")
    public ResponseEntity<ApiResponse<Map<String, Object>>> relatorioInventario() {
        return ResponseEntity.ok(ApiResponse.sucesso(relatorioService.relatorioInventario()));
    }

    // ✅ Manutenções acessível a ADMINISTRADOR e GERENTE_MANUTENCAO
    @GetMapping("/manutencao/resumo")
    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR','GERENTE_MANUTENCAO')")
    @Operation(summary = "Relatório de manutenções por período")
    public ResponseEntity<ApiResponse<Map<String, Object>>> relatorioManutencao(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        if (inicio == null) inicio = LocalDate.now().minusMonths(1);
        if (fim == null)    fim    = LocalDate.now();
        return ResponseEntity.ok(ApiResponse.sucesso(relatorioService.relatorioManutencoes(inicio, fim)));
    }

    // ✅ Movimentos acessível a ADMINISTRADOR e GERENTE_STOCK
    @GetMapping("/stock/movimentos")
    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR','GERENTE_STOCK')")
    @Operation(summary = "Relatório de movimentos de stock por período")
    public ResponseEntity<ApiResponse<Map<String, Object>>> relatorioMovimentos(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        if (inicio == null) inicio = LocalDate.now().minusMonths(1);
        if (fim == null)    fim    = LocalDate.now();
        return ResponseEntity.ok(ApiResponse.sucesso(relatorioService.relatorioMovimentosStock(inicio, fim)));
    }

    // ✅ Utilizadores apenas ADMINISTRADOR
    @GetMapping("/utilizadores/resumo")
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    @Operation(summary = "Relatório de utilizadores por perfil")
    public ResponseEntity<ApiResponse<Map<String, Object>>> relatorioUtilizadores() {
        return ResponseEntity.ok(ApiResponse.sucesso(relatorioService.relatorioUtilizadores()));
    }
}