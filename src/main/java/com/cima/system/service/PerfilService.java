package com.cima.system.service;

import com.cima.system.dto.request.PerfilRequest;
import com.cima.system.dto.response.PerfilResponse;

import java.util.List;

public interface PerfilService {
    PerfilResponse criar(PerfilRequest request);
    PerfilResponse atualizar(Long id, PerfilRequest request);
    void remover(Long id);
    PerfilResponse buscarPorId(Long id);
    List<PerfilResponse> listarTodos();
}
