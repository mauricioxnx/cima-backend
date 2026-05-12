package com.cima.system.controller;

import com.cima.system.dto.request.InventarioRequest;
import com.cima.system.dto.response.ApiResponse;
import com.cima.system.dto.response.InventarioResponse;
import com.cima.system.service.InventarioService;
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
@RequestMapping("/inventario")
@RequiredArgsConstructor
@Tag(name = "RF03 - Inventário", description = "Gestão de inventário de produtos")
@SecurityRequirement(name = "bearerAuth")
public class InventarioController {

    private final InventarioService inventarioService;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR','GERENTE_STOCK')")
    @Operation(summary = "Criar produto no inventário")
    public ResponseEntity<ApiResponse<InventarioResponse>> criar(@Valid @RequestBody InventarioRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.sucesso(inventarioService.criar(request), "Produto criado com sucesso"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR','GERENTE_STOCK')")
    @Operation(summary = "Atualizar produto")
    public ResponseEntity<ApiResponse<InventarioResponse>> atualizar(@PathVariable Long id, @Valid @RequestBody InventarioRequest request) {
        return ResponseEntity.ok(ApiResponse.sucesso(inventarioService.atualizar(id, request), "Produto atualizado"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR','GERENTE_STOCK')")
    @Operation(summary = "Remover produto")
    public ResponseEntity<ApiResponse<Void>> remover(@PathVariable Long id) {
        inventarioService.remover(id);
        return ResponseEntity.ok(ApiResponse.sucesso(null, "Produto removido"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar produto por ID")
    public ResponseEntity<ApiResponse<InventarioResponse>> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.sucesso(inventarioService.buscarPorId(id)));
    }

    @GetMapping("/codigo/{codigo}")
    @Operation(summary = "Buscar produto por código")
    public ResponseEntity<ApiResponse<InventarioResponse>> buscarPorCodigo(@PathVariable String codigo) {
        return ResponseEntity.ok(ApiResponse.sucesso(inventarioService.buscarPorCodigo(codigo)));
    }

    @GetMapping
    @Operation(summary = "Listar / pesquisar produtos")
    public ResponseEntity<ApiResponse<List<InventarioResponse>>> listar(
            @RequestParam(required = false) String descricao) {
        List<InventarioResponse> lista = descricao != null
                ? inventarioService.pesquisar(descricao)
                : inventarioService.listarTodos();
        return ResponseEntity.ok(ApiResponse.sucesso(lista));
    }

    @GetMapping("/estoque-baixo")
    @Operation(summary = "Listar produtos com estoque baixo")
    public ResponseEntity<ApiResponse<List<InventarioResponse>>> estoqueBaixo(
            @RequestParam(defaultValue = "5") int minimo) {
        return ResponseEntity.ok(ApiResponse.sucesso(inventarioService.listarEstoqueBaixo(minimo)));
    }

    @GetMapping("/sem-estoque")
    @Operation(summary = "Listar produtos sem estoque")
    public ResponseEntity<ApiResponse<List<InventarioResponse>>> semEstoque() {
        return ResponseEntity.ok(ApiResponse.sucesso(inventarioService.listarSemEstoque()));
    }
}
