package com.cima.system.service;

import com.cima.system.dto.request.TarefaRequest;
import com.cima.system.dto.response.TarefaResponse;
import com.cima.system.enums.EstadoTarefa;

import java.util.List;

public interface TarefaService {
    TarefaResponse criar(TarefaRequest request);
    TarefaResponse atualizar(Long id, TarefaRequest request);
    TarefaResponse atualizarEstado(Long id, EstadoTarefa estado);
    void remover(Long id);
    TarefaResponse buscarPorId(Long id);
    List<TarefaResponse> listarPorManutencao(Long manutencaoId);
    List<TarefaResponse> listarPorUtilizador(Long utilizadorId);
    List<TarefaResponse> listarPorEstado(EstadoTarefa estado);
}
