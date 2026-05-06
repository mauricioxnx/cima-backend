package com.cima.system.controller;

import com.cima.system.dto.request.TipoManutencaoRequest;
import com.cima.system.dto.response.ApiResponse;
import com.cima.system.dto.response.TipoManutencaoResponse;
import com.cima.system.service.TipoManutencaoService;
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
@RequestMapping("/tipos-manutencao")
@RequiredArgsConstructor
@Tag(name = "Tipos de Manutenção", description = "Gestão de tipos de manutenção")
@SecurityRequirement(name = "bearerAuth")
public class TipoManutencaoController {

    private final TipoManutencaoService tipoManutencaoService;

    @PostMapping
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    @Operation(summary = "Criar tipo de manutenção")
    public ResponseEntity<ApiResponse<TipoManutencaoResponse>> criar(
            @Valid @RequestBody TipoManutencaoRequest request) {
        TipoManutencaoResponse response = tipoManutencaoService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.sucesso(response, "Tipo de manutenção criado com sucesso"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    @Operation(summary = "Atualizar tipo de manutenção")
    public ResponseEntity<ApiResponse<TipoManutencaoResponse>> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody TipoManutencaoRequest request) {
        TipoManutencaoResponse response = tipoManutencaoService.atualizar(id, request);
        return ResponseEntity.ok(ApiResponse.sucesso(response, "Tipo de manutenção atualizado com sucesso"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    @Operation(summary = "Remover tipo de manutenção")
    public ResponseEntity<ApiResponse<Void>> remover(@PathVariable Long id) {
        tipoManutencaoService.remover(id);
        return ResponseEntity.ok(ApiResponse.sucesso(null, "Tipo de manutenção removido com sucesso"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar tipo de manutenção por ID")
    public ResponseEntity<ApiResponse<TipoManutencaoResponse>> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.sucesso(tipoManutencaoService.buscarPorId(id)));
    }

    @GetMapping
    @Operation(summary = "Listar todos os tipos de manutenção")
    public ResponseEntity<ApiResponse<List<TipoManutencaoResponse>>> listarTodos() {
        return ResponseEntity.ok(ApiResponse.sucesso(tipoManutencaoService.listarTodos()));
    }
}
