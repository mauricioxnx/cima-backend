package com.cima.system.controller;

import com.cima.system.dto.request.UtilizadorRequest;
import com.cima.system.dto.response.ApiResponse;
import com.cima.system.dto.response.UtilizadorResponse;
import com.cima.system.service.UtilizadorService;
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
import java.util.Map;

@RestController
@RequestMapping("/utilizadores")
@RequiredArgsConstructor
@Tag(name = "RF02 - Utilizadores", description = "Gestão de utilizadores")
@SecurityRequirement(name = "bearerAuth")
public class UtilizadorController {

    private final UtilizadorService utilizadorService;

    @PostMapping
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    @Operation(summary = "Criar utilizador")
    public ResponseEntity<ApiResponse<UtilizadorResponse>> criar(@Valid @RequestBody UtilizadorRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.sucesso(utilizadorService.criar(request), "Utilizador criado com sucesso"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    @Operation(summary = "Atualizar utilizador")
    public ResponseEntity<ApiResponse<UtilizadorResponse>> atualizar(@PathVariable Long id, @Valid @RequestBody UtilizadorRequest request) {
        return ResponseEntity.ok(ApiResponse.sucesso(utilizadorService.atualizar(id, request), "Utilizador atualizado"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    @Operation(summary = "Remover utilizador")
    public ResponseEntity<ApiResponse<Void>> remover(@PathVariable Long id) {
        utilizadorService.remover(id);
        return ResponseEntity.ok(ApiResponse.sucesso(null, "Utilizador removido"));
    }

    @PatchMapping("/{id}/desativar")
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    @Operation(summary = "Desativar utilizador")
    public ResponseEntity<ApiResponse<Void>> desativar(@PathVariable Long id) {
        utilizadorService.desativar(id);
        return ResponseEntity.ok(ApiResponse.sucesso(null, "Utilizador desativado"));
    }

    @PatchMapping("/{id}/ativar")
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    @Operation(summary = "Ativar utilizador")
    public ResponseEntity<ApiResponse<Void>> ativar(@PathVariable Long id) {
        utilizadorService.ativar(id);
        return ResponseEntity.ok(ApiResponse.sucesso(null, "Utilizador ativado"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar utilizador por ID")
    public ResponseEntity<ApiResponse<UtilizadorResponse>> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.sucesso(utilizadorService.buscarPorId(id)));
    }

    @GetMapping
    @Operation(summary = "Listar todos os utilizadores")
    public ResponseEntity<ApiResponse<List<UtilizadorResponse>>> listarTodos(
            @RequestParam(required = false) Boolean apenasAtivos,
            @RequestParam(required = false) Long perfilId) {
        List<UtilizadorResponse> lista;
        if (perfilId != null) lista = utilizadorService.listarPorPerfil(perfilId);
        else if (Boolean.TRUE.equals(apenasAtivos)) lista = utilizadorService.listarAtivos();
        else lista = utilizadorService.listarTodos();
        return ResponseEntity.ok(ApiResponse.sucesso(lista));
    }

    @PatchMapping("/{id}/alterar-senha")
    @Operation(summary = "Alterar senha do utilizador")
    public ResponseEntity<ApiResponse<Void>> alterarSenha(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        utilizadorService.alterarSenha(id, body.get("senhaAtual"), body.get("novaSenha"));
        return ResponseEntity.ok(ApiResponse.sucesso(null, "Senha alterada com sucesso"));
    }
}
