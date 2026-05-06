package com.cima.system.service;

import com.cima.system.dto.request.TipoManutencaoRequest;
import com.cima.system.dto.response.TipoManutencaoResponse;

import java.util.List;

public interface TipoManutencaoService {
    TipoManutencaoResponse criar(TipoManutencaoRequest request);
    TipoManutencaoResponse atualizar(Long id, TipoManutencaoRequest request);
    void remover(Long id);
    TipoManutencaoResponse buscarPorId(Long id);
    List<TipoManutencaoResponse> listarTodos();
}
