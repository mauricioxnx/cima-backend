package com.cima.system.controller;

import com.cima.system.dto.request.FornecedorRequest;
import com.cima.system.dto.response.ApiResponse;
import com.cima.system.dto.response.FornecedorResponse;
import com.cima.system.service.FornecedorService;
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
@RequestMapping("/fornecedores")
@RequiredArgsConstructor
@Tag(name = "RF04 - Fornecedores", description = "Gestão de fornecedores")
@SecurityRequirement(name = "bearerAuth")
public class FornecedorController {

    private final FornecedorService fornecedorService;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR','GERENTE_STOCK')")
    @Operation(summary = "Cadastrar fornecedor")
    public ResponseEntity<ApiResponse<FornecedorResponse>> criar(@Valid @RequestBody FornecedorRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.sucesso(fornecedorService.criar(request), "Fornecedor cadastrado com sucesso"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR','GERENTE_STOCK')")
    @Operation(summary = "Atualizar fornecedor")
    public ResponseEntity<ApiResponse<FornecedorResponse>> atualizar(@PathVariable Long id, @Valid @RequestBody FornecedorRequest request) {
        return ResponseEntity.ok(ApiResponse.sucesso(fornecedorService.atualizar(id, request), "Fornecedor atualizado"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR','GERENTE_STOCK')")
    @Operation(summary = "Remover fornecedor")
    public ResponseEntity<ApiResponse<Void>> remover(@PathVariable Long id) {
        fornecedorService.remover(id);
        return ResponseEntity.ok(ApiResponse.sucesso(null, "Fornecedor removido"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar fornecedor por ID")
    public ResponseEntity<ApiResponse<FornecedorResponse>> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.sucesso(fornecedorService.buscarPorId(id)));
    }

    @GetMapping
    @Operation(summary = "Listar / pesquisar fornecedores")
    public ResponseEntity<ApiResponse<List<FornecedorResponse>>> listar(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String categoria) {
        List<FornecedorResponse> lista;
        if (nome != null) lista = fornecedorService.pesquisar(nome);
        else if (categoria != null) lista = fornecedorService.listarPorCategoria(categoria);
        else lista = fornecedorService.listarTodos();
        return ResponseEntity.ok(ApiResponse.sucesso(lista));
    }
}
