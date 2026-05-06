package com.cima.system.service;

import com.cima.system.dto.request.MaquinaVeiculoRequest;
import com.cima.system.dto.response.MaquinaVeiculoResponse;

import java.util.List;

public interface MaquinaVeiculoService {
    MaquinaVeiculoResponse criar(MaquinaVeiculoRequest request);
    MaquinaVeiculoResponse atualizar(Long id, MaquinaVeiculoRequest request);
    void remover(Long id);
    MaquinaVeiculoResponse buscarPorId(Long id);
    List<MaquinaVeiculoResponse> listarTodos();
    List<MaquinaVeiculoResponse> listarPorEstado(String estado);
    List<MaquinaVeiculoResponse> pesquisar(String modelo);
}
