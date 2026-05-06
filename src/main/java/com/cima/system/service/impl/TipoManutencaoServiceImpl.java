package com.cima.system.service.impl;

import com.cima.system.dto.request.TipoManutencaoRequest;
import com.cima.system.dto.response.TipoManutencaoResponse;
import com.cima.system.entity.TipoManutencao;
import com.cima.system.exception.BusinessException;
import com.cima.system.exception.ResourceNotFoundException;
import com.cima.system.repository.TipoManutencaoRepository;
import com.cima.system.service.TipoManutencaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TipoManutencaoServiceImpl implements TipoManutencaoService {

    private final TipoManutencaoRepository tipoManutencaoRepository;

    @Override
    public TipoManutencaoResponse criar(TipoManutencaoRequest request) {
        if (tipoManutencaoRepository.existsByNomeTipo(request.getNomeTipo())) {
            throw new BusinessException("Já existe um tipo com o nome: " + request.getNomeTipo());
        }
        TipoManutencao tipo = TipoManutencao.builder()
                .nomeTipo(request.getNomeTipo())
                .descricao(request.getDescricao())
                .build();
        return toResponse(tipoManutencaoRepository.save(tipo));
    }

    @Override
    public TipoManutencaoResponse atualizar(Long id, TipoManutencaoRequest request) {
        TipoManutencao tipo = buscarEntidade(id);
        if (!tipo.getNomeTipo().equals(request.getNomeTipo())
                && tipoManutencaoRepository.existsByNomeTipo(request.getNomeTipo())) {
            throw new BusinessException("Já existe um tipo com o nome: " + request.getNomeTipo());
        }
        tipo.setNomeTipo(request.getNomeTipo());
        tipo.setDescricao(request.getDescricao());
        return toResponse(tipoManutencaoRepository.save(tipo));
    }

    @Override
    public void remover(Long id) {
        TipoManutencao tipo = buscarEntidade(id);
        if (tipo.getManutencoes() != null && !tipo.getManutencoes().isEmpty()) {
            throw new BusinessException("Não é possível remover este tipo pois existem manutenções associadas.");
        }
        tipoManutencaoRepository.delete(tipo);
    }

    @Override
    @Transactional(readOnly = true)
    public TipoManutencaoResponse buscarPorId(Long id) {
        return toResponse(buscarEntidade(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TipoManutencaoResponse> listarTodos() {
        return tipoManutencaoRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private TipoManutencao buscarEntidade(Long id) {
        return tipoManutencaoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TipoManutencao não encontrado com ID: " + id));
    }

    private TipoManutencaoResponse toResponse(TipoManutencao t) {
        return TipoManutencaoResponse.builder()
                .id(t.getId())
                .nomeTipo(t.getNomeTipo())
                .descricao(t.getDescricao())
                .build();
    }
}
