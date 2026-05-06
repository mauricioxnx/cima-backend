package com.cima.system.controller;

import com.cima.system.dto.request.ManutencaoRequest;
import com.cima.system.dto.response.ApiResponse;
import com.cima.system.dto.response.ManutencaoResponse;
import com.cima.system.enums.EstadoManutencao;
import com.cima.system.service.ManutencaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/manutencoes")
@RequiredArgsConstructor
@Tag(name = "Manutenções", description = "RF06 – Gerir manutenções | RF07 – Agendar manutenções")
@SecurityRequirement(name = "bearerAuth")
public class ManutencaoController {

    private final ManutencaoService manutencaoService;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR','GERENTE_MANUTENCAO')")
    @Operation(summary = "Criar manutenção")
    public ResponseEntity<ApiResponse<ManutencaoResponse>> criar(
            @Valid @RequestBody ManutencaoRequest request) {
        ManutencaoResponse response = manutencaoService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.sucesso(response, "Manutenção criada com sucesso"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR','GERENTE_MANUTENCAO')")
    @Operation(summary = "Atualizar manutenção")
    public ResponseEntity<ApiResponse<ManutencaoResponse>> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ManutencaoRequest request) {
        ManutencaoResponse response = manutencaoService.atualizar(id, request);
        return ResponseEntity.ok(ApiResponse.sucesso(response, "Manutenção atualizada com sucesso"));
    }

    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR','GERENTE_MANUTENCAO','TECNICO')")
    @Operation(summary = "Atualizar estado da manutenção")
    public ResponseEntity<ApiResponse<ManutencaoResponse>> atualizarEstado(
            @PathVariable Long id,
            @RequestParam EstadoManutencao estado) {
        ManutencaoResponse response = manutencaoService.atualizarEstado(id, estado);
        return ResponseEntity.ok(ApiResponse.sucesso(response, "Estado atualizado para " + estado));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    @Operation(summary = "Remover manutenção")
    public ResponseEntity<ApiResponse<Void>> remover(@PathVariable Long id) {
        manutencaoService.remover(id);
        return ResponseEntity.ok(ApiResponse.sucesso(null, "Manutenção removida com sucesso"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar manutenção por ID")
    public ResponseEntity<ApiResponse<ManutencaoResponse>> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.sucesso(manutencaoService.buscarPorId(id)));
    }

    @GetMapping
    @Operation(summary = "Listar todas as manutenções")
    public ResponseEntity<ApiResponse<List<ManutencaoResponse>>> listarTodos() {
        return ResponseEntity.ok(ApiResponse.sucesso(manutencaoService.listarTodos()));
    }

    @GetMapping("/estado/{estado}")
    @Operation(summary = "Listar manutenções por estado")
    public ResponseEntity<ApiResponse<List<ManutencaoResponse>>> listarPorEstado(
            @PathVariable EstadoManutencao estado) {
        return ResponseEntity.ok(ApiResponse.sucesso(manutencaoService.listarPorEstado(estado)));
    }

    @GetMapping("/maquina/{maquinaId}")
    @Operation(summary = "Listar manutenções por máquina/veículo")
    public ResponseEntity<ApiResponse<List<ManutencaoResponse>>> listarPorMaquina(
            @PathVariable Long maquinaId) {
        return ResponseEntity.ok(ApiResponse.sucesso(manutencaoService.listarPorMaquina(maquinaId)));
    }

    @GetMapping("/periodo")
    @Operation(summary = "Listar manutenções por período")
    public ResponseEntity<ApiResponse<List<ManutencaoResponse>>> listarPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        return ResponseEntity.ok(ApiResponse.sucesso(manutencaoService.listarPorPeriodo(inicio, fim)));
    }

    @GetMapping("/atrasadas")
    @Operation(summary = "Listar manutenções atrasadas")
    public ResponseEntity<ApiResponse<List<ManutencaoResponse>>> listarAtrasadas() {
        return ResponseEntity.ok(ApiResponse.sucesso(manutencaoService.listarAtrasadas()));
    }

    @GetMapping("/proximas")
    @Operation(summary = "Listar próximas manutenções (nos próximos N dias)")
    public ResponseEntity<ApiResponse<List<ManutencaoResponse>>> listarProximas(
            @RequestParam(defaultValue = "7") int dias) {
        return ResponseEntity.ok(ApiResponse.sucesso(manutencaoService.listarProximas(dias)));
    }
}
