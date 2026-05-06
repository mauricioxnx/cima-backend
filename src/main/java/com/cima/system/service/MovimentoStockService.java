package com.cima.system.service;

import com.cima.system.dto.request.MovimentoStockRequest;
import com.cima.system.dto.response.MovimentoStockResponse;
import com.cima.system.enums.TipoMovimento;

import java.time.LocalDateTime;
import java.util.List;

public interface MovimentoStockService {
    MovimentoStockResponse registar(MovimentoStockRequest request);
    MovimentoStockResponse buscarPorId(Long id);
    List<MovimentoStockResponse> listarTodos();
    List<MovimentoStockResponse> listarPorInventario(Long inventarioId);
    List<MovimentoStockResponse> listarPorTipo(TipoMovimento tipo);
    List<MovimentoStockResponse> listarPorPeriodo(LocalDateTime inicio, LocalDateTime fim);
}
