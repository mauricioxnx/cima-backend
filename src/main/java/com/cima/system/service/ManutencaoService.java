package com.cima.system.service;

import com.cima.system.dto.request.ManutencaoRequest;
import com.cima.system.dto.response.ManutencaoResponse;
import com.cima.system.enums.EstadoManutencao;

import java.time.LocalDate;
import java.util.List;

public interface ManutencaoService {
    ManutencaoResponse criar(ManutencaoRequest request);
    ManutencaoResponse atualizar(Long id, ManutencaoRequest request);
    ManutencaoResponse atualizarEstado(Long id, EstadoManutencao estado);
    void remover(Long id);
    ManutencaoResponse buscarPorId(Long id);
    List<ManutencaoResponse> listarTodos();
    List<ManutencaoResponse> listarPorEstado(EstadoManutencao estado);
    List<ManutencaoResponse> listarPorMaquina(Long maquinaId);
    List<ManutencaoResponse> listarPorPeriodo(LocalDate inicio, LocalDate fim);
    List<ManutencaoResponse> listarAtrasadas();
    List<ManutencaoResponse> listarProximas(int dias);
}
