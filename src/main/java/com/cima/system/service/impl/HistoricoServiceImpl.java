package com.cima.system.service.impl;

import com.cima.system.dto.response.HistoricoResponse;
import com.cima.system.entity.Historico;
import com.cima.system.entity.Inventario;
import com.cima.system.entity.Manutencao;
import com.cima.system.entity.Utilizador;
import com.cima.system.repository.HistoricoRepository;
import com.cima.system.repository.InventarioRepository;
import com.cima.system.repository.ManutencaoRepository;
import com.cima.system.repository.UtilizadorRepository;
import com.cima.system.service.HistoricoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class HistoricoServiceImpl implements HistoricoService {

    private final HistoricoRepository historicoRepository;
    private final UtilizadorRepository utilizadorRepository;
    private final InventarioRepository inventarioRepository;
    private final ManutencaoRepository manutencaoRepository;

    @Override
    public HistoricoResponse registar(String descricao, Long utilizadorId, Long inventarioId, Long manutencaoId) {
        Utilizador util = utilizadorId != null ? utilizadorRepository.findById(utilizadorId).orElse(null) : null;
        Inventario inv = inventarioId != null ? inventarioRepository.findById(inventarioId).orElse(null) : null;
        Manutencao man = manutencaoId != null ? manutencaoRepository.findById(manutencaoId).orElse(null) : null;

        Historico h = Historico.builder()
                .descricao(descricao)
                .utilizador(util)
                .inventario(inv)
                .manutencao(man)
                .build();
        return toResponse(historicoRepository.save(h));
    }

    @Override
    @Transactional(readOnly = true)
    public List<HistoricoResponse> listarTodos() {
        return historicoRepository.findAllByOrderByDataExecucaoDesc()
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<HistoricoResponse> listarPorUtilizador(Long utilizadorId) {
        return historicoRepository.findByUtilizadorIdOrderByDataExecucaoDesc(utilizadorId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<HistoricoResponse> listarPorInventario(Long inventarioId) {
        return historicoRepository.findByInventarioIdOrderByDataExecucaoDesc(inventarioId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<HistoricoResponse> listarPorManutencao(Long manutencaoId) {
        return historicoRepository.findByManutencaoIdOrderByDataExecucaoDesc(manutencaoId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<HistoricoResponse> listarPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        return historicoRepository.findByDataExecucaoBetweenOrderByDataExecucaoDesc(inicio, fim)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    private HistoricoResponse toResponse(Historico h) {
        return HistoricoResponse.builder()
                .id(h.getId())
                .descricao(h.getDescricao())
                .dataExecucao(h.getDataExecucao())
                .utilizadorId(h.getUtilizador() != null ? h.getUtilizador().getId() : null)
                .utilizadorNome(h.getUtilizador() != null ? h.getUtilizador().getNome() : null)
                .inventarioId(h.getInventario() != null ? h.getInventario().getId() : null)
                .manutencaoId(h.getManutencao() != null ? h.getManutencao().getId() : null)
                .build();
    }
}
