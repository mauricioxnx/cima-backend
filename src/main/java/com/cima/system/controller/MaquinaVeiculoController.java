package com.cima.system.controller;

import com.cima.system.dto.request.MaquinaVeiculoRequest;
import com.cima.system.dto.response.ApiResponse;
import com.cima.system.dto.response.MaquinaVeiculoResponse;
import com.cima.system.service.MaquinaVeiculoService;
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
@RequestMapping("/maquinas-veiculos")
@RequiredArgsConstructor
@Tag(name = "Máquinas e Veículos", description = "Gestão de máquinas e veículos")
@SecurityRequirement(name = "bearerAuth")
public class MaquinaVeiculoController {

    private final MaquinaVeiculoService maquinaVeiculoService;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR','GERENTE_MANUTENCAO')")
    @Operation(summary = "Registar máquina ou veículo")
    public ResponseEntity<ApiResponse<MaquinaVeiculoResponse>> criar(
            @Valid @RequestBody MaquinaVeiculoRequest request) {
        MaquinaVeiculoResponse response = maquinaVeiculoService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.sucesso(response, "Máquina/Veículo registado com sucesso"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR','GERENTE_MANUTENCAO')")
    @Operation(summary = "Atualizar máquina ou veículo")
    public ResponseEntity<ApiResponse<MaquinaVeiculoResponse>> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody MaquinaVeiculoRequest request) {
        MaquinaVeiculoResponse response = maquinaVeiculoService.atualizar(id, request);
        return ResponseEntity.ok(ApiResponse.sucesso(response, "Máquina/Veículo atualizado com sucesso"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    @Operation(summary = "Remover máquina ou veículo")
    public ResponseEntity<ApiResponse<Void>> remover(@PathVariable Long id) {
        maquinaVeiculoService.remover(id);
        return ResponseEntity.ok(ApiResponse.sucesso(null, "Máquina/Veículo removido com sucesso"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar por ID")
    public ResponseEntity<ApiResponse<MaquinaVeiculoResponse>> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.sucesso(maquinaVeiculoService.buscarPorId(id)));
    }

    @GetMapping
    @Operation(summary = "Listar todas as máquinas e veículos")
    public ResponseEntity<ApiResponse<List<MaquinaVeiculoResponse>>> listarTodos() {
        return ResponseEntity.ok(ApiResponse.sucesso(maquinaVeiculoService.listarTodos()));
    }

    @GetMapping("/estado/{estado}")
    @Operation(summary = "Listar por estado (ACTIVO, INACTIVO, EM_MANUTENCAO)")
    public ResponseEntity<ApiResponse<List<MaquinaVeiculoResponse>>> listarPorEstado(
            @PathVariable String estado) {
        return ResponseEntity.ok(ApiResponse.sucesso(maquinaVeiculoService.listarPorEstado(estado)));
    }

    @GetMapping("/pesquisar")
    @Operation(summary = "Pesquisar por modelo")
    public ResponseEntity<ApiResponse<List<MaquinaVeiculoResponse>>> pesquisar(
            @RequestParam String modelo) {
        return ResponseEntity.ok(ApiResponse.sucesso(maquinaVeiculoService.pesquisar(modelo)));
    }
}
