package com.cima.system.service;

import com.cima.system.dto.response.HistoricoResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface HistoricoService {
    HistoricoResponse registar(String descricao, Long utilizadorId, Long inventarioId, Long manutencaoId);
    List<HistoricoResponse> listarTodos();
    List<HistoricoResponse> listarPorUtilizador(Long utilizadorId);
    List<HistoricoResponse> listarPorInventario(Long inventarioId);
    List<HistoricoResponse> listarPorManutencao(Long manutencaoId);
    List<HistoricoResponse> listarPorPeriodo(LocalDateTime inicio, LocalDateTime fim);
}
