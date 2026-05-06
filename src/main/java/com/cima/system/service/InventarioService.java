package com.cima.system.service;

import com.cima.system.dto.request.InventarioRequest;
import com.cima.system.dto.response.InventarioResponse;

import java.util.List;

public interface InventarioService {
    InventarioResponse criar(InventarioRequest request);
    InventarioResponse atualizar(Long id, InventarioRequest request);
    void remover(Long id);
    InventarioResponse buscarPorId(Long id);
    InventarioResponse buscarPorCodigo(String codigo);
    List<InventarioResponse> listarTodos();
    List<InventarioResponse> pesquisar(String descricao);
    List<InventarioResponse> listarEstoqueBaixo(int minimo);
    List<InventarioResponse> listarSemEstoque();
}
