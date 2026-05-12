package com.cima.system.controller;

import com.cima.system.dto.request.PerfilRequest;
import com.cima.system.dto.response.ApiResponse;
import com.cima.system.dto.response.PerfilResponse;
import com.cima.system.service.PerfilService;
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
@RequestMapping("/perfis")
@RequiredArgsConstructor
@Tag(name = "RF01 - Perfis", description = "Gestão de perfis de utilizador")
@SecurityRequirement(name = "bearerAuth")
public class PerfilController {

    private final PerfilService perfilService;

    @PostMapping
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    @Operation(summary = "Criar perfil")
    public ResponseEntity<ApiResponse<PerfilResponse>> criar(@Valid @RequestBody PerfilRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.sucesso(perfilService.criar(request), "Perfil criado com sucesso"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    @Operation(summary = "Atualizar perfil")
    public ResponseEntity<ApiResponse<PerfilResponse>> atualizar(@PathVariable Long id, @Valid @RequestBody PerfilRequest request) {
        return ResponseEntity.ok(ApiResponse.sucesso(perfilService.atualizar(id, request), "Perfil atualizado"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    @Operation(summary = "Remover perfil")
    public ResponseEntity<ApiResponse<Void>> remover(@PathVariable Long id) {
        perfilService.remover(id);
        return ResponseEntity.ok(ApiResponse.sucesso(null, "Perfil removido com sucesso"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar perfil por ID")
    public ResponseEntity<ApiResponse<PerfilResponse>> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.sucesso(perfilService.buscarPorId(id)));
    }

    @GetMapping
    @Operation(summary = "Listar todos os perfis")
    public ResponseEntity<ApiResponse<List<PerfilResponse>>> listarTodos() {
        return ResponseEntity.ok(ApiResponse.sucesso(perfilService.listarTodos()));
    }
}
