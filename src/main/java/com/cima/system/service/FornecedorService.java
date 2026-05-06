package com.cima.system.service;

import com.cima.system.dto.request.FornecedorRequest;
import com.cima.system.dto.response.FornecedorResponse;

import java.util.List;

public interface FornecedorService {
    FornecedorResponse criar(FornecedorRequest request);
    FornecedorResponse atualizar(Long id, FornecedorRequest request);
    void remover(Long id);
    FornecedorResponse buscarPorId(Long id);
    List<FornecedorResponse> listarTodos();
    List<FornecedorResponse> pesquisar(String nome);
    List<FornecedorResponse> listarPorCategoria(String categoria);
}
