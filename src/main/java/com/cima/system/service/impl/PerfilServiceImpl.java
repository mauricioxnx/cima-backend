package com.cima.system.service.impl;

import com.cima.system.dto.request.PerfilRequest;
import com.cima.system.dto.response.PerfilResponse;
import com.cima.system.entity.Perfil;
import com.cima.system.exception.BusinessException;
import com.cima.system.exception.ResourceNotFoundException;
import com.cima.system.repository.PerfilRepository;
import com.cima.system.service.PerfilService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PerfilServiceImpl implements PerfilService {

    private final PerfilRepository perfilRepository;

    @Override
    public PerfilResponse criar(PerfilRequest request) {
        if (perfilRepository.existsByNome(request.getNome())) {
            throw new BusinessException("Já existe um perfil com o nome: " + request.getNome());
        }
        Perfil perfil = Perfil.builder()
                .nome(request.getNome())
                .descricao(request.getDescricao())
                .build();
        return toResponse(perfilRepository.save(perfil));
    }

    @Override
    public PerfilResponse atualizar(Long id, PerfilRequest request) {
        Perfil perfil = buscarEntidade(id);
        if (!perfil.getNome().equals(request.getNome()) && perfilRepository.existsByNome(request.getNome())) {
            throw new BusinessException("Já existe um perfil com o nome: " + request.getNome());
        }
        perfil.setNome(request.getNome());
        perfil.setDescricao(request.getDescricao());
        return toResponse(perfilRepository.save(perfil));
    }

    @Override
    public void remover(Long id) {
        Perfil perfil = buscarEntidade(id);
        if (perfil.getUtilizadores() != null && !perfil.getUtilizadores().isEmpty()) {
            throw new BusinessException("Não é possível remover o perfil pois existem utilizadores associados.");
        }
        perfilRepository.delete(perfil);
    }

    @Override
    @Transactional(readOnly = true)
    public PerfilResponse buscarPorId(Long id) {
        return toResponse(buscarEntidade(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PerfilResponse> listarTodos() {
        return perfilRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    private Perfil buscarEntidade(Long id) {
        return perfilRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Perfil não encontrado com ID: " + id));
    }

    private PerfilResponse toResponse(Perfil p) {
        return PerfilResponse.builder()
                .id(p.getId())
                .nome(p.getNome())
                .descricao(p.getDescricao())
                .totalUtilizadores(p.getUtilizadores() != null ? p.getUtilizadores().size() : 0)
                .build();
    }
}
