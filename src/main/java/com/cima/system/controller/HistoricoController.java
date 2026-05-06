package com.cima.system.controller;

import com.cima.system.dto.response.ApiResponse;
import com.cima.system.dto.response.HistoricoResponse;
import com.cima.system.service.HistoricoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/historico")
@RequiredArgsConstructor
@Tag(name = "Histórico", description = "RF09 – Consultar histórico de atividades")
@SecurityRequirement(name = "bearerAuth")
public class HistoricoController {

    private final HistoricoService historicoService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR','GERENTE_STOCK','GERENTE_MANUTENCAO')")
    @Operation(summary = "Listar todo o histórico (ordem decrescente)")
    public ResponseEntity<ApiResponse<List<HistoricoResponse>>> listarTodos() {
        return ResponseEntity.ok(ApiResponse.sucesso(historicoService.listarTodos()));
    }

    @GetMapping("/utilizador/{utilizadorId}")
    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR','GERENTE_STOCK','GERENTE_MANUTENCAO')")
    @Operation(summary = "Histórico por utilizador")
    public ResponseEntity<ApiResponse<List<HistoricoResponse>>> porUtilizador(
            @PathVariable Long utilizadorId) {
        return ResponseEntity.ok(ApiResponse.sucesso(historicoService.listarPorUtilizador(utilizadorId)));
    }

    @GetMapping("/inventario/{inventarioId}")
    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR','GERENTE_STOCK','GERENTE_MANUTENCAO')")
    @Operation(summary = "Histórico por item de inventário")
    public ResponseEntity<ApiResponse<List<HistoricoResponse>>> porInventario(
            @PathVariable Long inventarioId) {
        return ResponseEntity.ok(ApiResponse.sucesso(historicoService.listarPorInventario(inventarioId)));
    }

    @GetMapping("/manutencao/{manutencaoId}")
    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR','GERENTE_STOCK','GERENTE_MANUTENCAO')")
    @Operation(summary = "Histórico por manutenção")
    public ResponseEntity<ApiResponse<List<HistoricoResponse>>> porManutencao(
            @PathVariable Long manutencaoId) {
        return ResponseEntity.ok(ApiResponse.sucesso(historicoService.listarPorManutencao(manutencaoId)));
    }

    @GetMapping("/periodo")
    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR','GERENTE_STOCK','GERENTE_MANUTENCAO')")
    @Operation(summary = "Histórico por período")
    public ResponseEntity<ApiResponse<List<HistoricoResponse>>> porPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim) {
        return ResponseEntity.ok(ApiResponse.sucesso(historicoService.listarPorPeriodo(inicio, fim)));
    }
}
