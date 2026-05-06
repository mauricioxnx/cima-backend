package com.cima.system.controller;

import com.cima.system.dto.request.MovimentoStockRequest;
import com.cima.system.dto.response.ApiResponse;
import com.cima.system.dto.response.MovimentoStockResponse;
import com.cima.system.enums.TipoMovimento;
import com.cima.system.service.MovimentoStockService;
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

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/movimentos-stock")
@RequiredArgsConstructor
@Tag(name = "RF05 - Movimentos de Stock", description = "Registo de entradas, saídas e transferências")
@SecurityRequirement(name = "bearerAuth")
public class MovimentoStockController {

    private final MovimentoStockService movimentoService;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('Administrador','Gerente de Stock')")
    @Operation(summary = "Registar movimento de stock")
    public ResponseEntity<ApiResponse<MovimentoStockResponse>> registar(@Valid @RequestBody MovimentoStockRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.sucesso(movimentoService.registar(request), "Movimento registado com sucesso"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar movimento por ID")
    public ResponseEntity<ApiResponse<MovimentoStockResponse>> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.sucesso(movimentoService.buscarPorId(id)));
    }

    @GetMapping
    @Operation(summary = "Listar movimentos com filtros opcionais")
    public ResponseEntity<ApiResponse<List<MovimentoStockResponse>>> listar(
            @RequestParam(required = false) Long inventarioId,
            @RequestParam(required = false) TipoMovimento tipo,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim) {

        List<MovimentoStockResponse> lista;
        if (inventarioId != null) lista = movimentoService.listarPorInventario(inventarioId);
        else if (tipo != null) lista = movimentoService.listarPorTipo(tipo);
        else if (inicio != null && fim != null) lista = movimentoService.listarPorPeriodo(inicio, fim);
        else lista = movimentoService.listarTodos();

        return ResponseEntity.ok(ApiResponse.sucesso(lista));
    }
}
