package com.cima.system.service;

import com.cima.system.dto.request.UtilizadorRequest;
import com.cima.system.dto.response.UtilizadorResponse;

import java.util.List;

public interface UtilizadorService {
    UtilizadorResponse criar(UtilizadorRequest request);
    UtilizadorResponse atualizar(Long id, UtilizadorRequest request);
    void remover(Long id);
    void desativar(Long id);
    void ativar(Long id);
    UtilizadorResponse buscarPorId(Long id);
    List<UtilizadorResponse> listarTodos();
    List<UtilizadorResponse> listarAtivos();
    List<UtilizadorResponse> listarPorPerfil(Long perfilId);
    void alterarSenha(Long id, String senhaAtual, String novaSenha);
}
