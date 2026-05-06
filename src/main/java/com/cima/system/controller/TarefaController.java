package com.cima.system.controller;

import com.cima.system.dto.request.TarefaRequest;
import com.cima.system.dto.response.ApiResponse;
import com.cima.system.dto.response.TarefaResponse;
import com.cima.system.enums.EstadoTarefa;
import com.cima.system.service.TarefaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tarefas")
@RequiredArgsConstructor
@Tag(name = "Tarefas", description = "RF08 – Gerir tarefas de manutenção")
@SecurityRequirement(name = "bearerAuth")
public class TarefaController {

    private final TarefaService tarefaService;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR','GERENTE_MANUTENCAO','TECNICO')")
    @Operation(summary = "Criar tarefa de manutenção")
    public ResponseEntity<ApiResponse<TarefaResponse>> criar(
            @Valid @RequestBody TarefaRequest request) {
        TarefaResponse response = tarefaService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.sucesso(response, "Tarefa criada com sucesso"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR','GERENTE_MANUTENCAO','TECNICO')")
    @Operation(summary = "Atualizar tarefa")
    public ResponseEntity<ApiResponse<TarefaResponse>> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody TarefaRequest request) {
        TarefaResponse response = tarefaService.atualizar(id, request);
        return ResponseEntity.ok(ApiResponse.sucesso(response, "Tarefa atualizada com sucesso"));
    }

    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR','GERENTE_MANUTENCAO','TECNICO')")
    @Operation(summary = "Atualizar estado da tarefa")
    public ResponseEntity<ApiResponse<TarefaResponse>> atualizarEstado(
            @PathVariable Long id,
            @RequestParam EstadoTarefa estado) {
        TarefaResponse response = tarefaService.atualizarEstado(id, estado);
        return ResponseEntity.ok(ApiResponse.sucesso(response, "Estado atualizado para " + estado));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR','GERENTE_MANUTENCAO')")
    @Operation(summary = "Remover tarefa")
    public ResponseEntity<ApiResponse<Void>> remover(@PathVariable Long id) {
        tarefaService.remover(id);
        return ResponseEntity.ok(ApiResponse.sucesso(null, "Tarefa removida com sucesso"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar tarefa por ID")
    public ResponseEntity<ApiResponse<TarefaResponse>> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.sucesso(tarefaService.buscarPorId(id)));
    }

    @GetMapping("/manutencao/{manutencaoId}")
    @Operation(summary = "Listar tarefas por manutenção")
    public ResponseEntity<ApiResponse<List<TarefaResponse>>> listarPorManutencao(
            @PathVariable Long manutencaoId) {
        return ResponseEntity.ok(ApiResponse.sucesso(tarefaService.listarPorManutencao(manutencaoId)));
    }

    @GetMapping("/utilizador/{utilizadorId}")
    @Operation(summary = "Listar tarefas por utilizador")
    public ResponseEntity<ApiResponse<List<TarefaResponse>>> listarPorUtilizador(
            @PathVariable Long utilizadorId) {
        return ResponseEntity.ok(ApiResponse.sucesso(tarefaService.listarPorUtilizador(utilizadorId)));
    }

    @GetMapping("/estado/{estado}")
    @Operation(summary = "Listar tarefas por estado")
    public ResponseEntity<ApiResponse<List<TarefaResponse>>> listarPorEstado(
            @PathVariable EstadoTarefa estado) {
        return ResponseEntity.ok(ApiResponse.sucesso(tarefaService.listarPorEstado(estado)));
    }
}
