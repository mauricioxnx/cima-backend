package com.cima.system.service.impl;

import com.cima.system.dto.request.TarefaRequest;
import com.cima.system.dto.response.TarefaResponse;
import com.cima.system.entity.Manutencao;
import com.cima.system.entity.Tarefa;
import com.cima.system.entity.Utilizador;
import com.cima.system.enums.EstadoTarefa;
import com.cima.system.exception.ResourceNotFoundException;
import com.cima.system.repository.ManutencaoRepository;
import com.cima.system.repository.TarefaRepository;
import com.cima.system.repository.UtilizadorRepository;
import com.cima.system.service.TarefaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TarefaServiceImpl implements TarefaService {

    private final TarefaRepository tarefaRepository;
    private final ManutencaoRepository manutencaoRepository;
    private final UtilizadorRepository utilizadorRepository;

    @Override
    public TarefaResponse criar(TarefaRequest request) {
        Manutencao man = manutencaoRepository.findById(request.getManutencaoId())
                .orElseThrow(() -> new ResourceNotFoundException("Manutenção não encontrada: " + request.getManutencaoId()));
        Utilizador util = request.getUtilizadorId() != null
                ? utilizadorRepository.findById(request.getUtilizadorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Utilizador não encontrado: " + request.getUtilizadorId()))
                : null;

        Tarefa t = Tarefa.builder()
                .status(EstadoTarefa.ABERTA)
                .descricao(request.getDescricao())
                .manutencao(man)
                .utilizador(util)
                .build();
        return toResponse(tarefaRepository.save(t));
    }

    @Override
    public TarefaResponse atualizar(Long id, TarefaRequest request) {
        Tarefa t = buscarEntidade(id);
        t.setDescricao(request.getDescricao());
        if (request.getStatus() != null) t.setStatus(request.getStatus());
        if (request.getUtilizadorId() != null) {
            t.setUtilizador(utilizadorRepository.findById(request.getUtilizadorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Utilizador não encontrado: " + request.getUtilizadorId())));
        }
        return toResponse(tarefaRepository.save(t));
    }

    @Override
    public TarefaResponse atualizarEstado(Long id, EstadoTarefa estado) {
        Tarefa t = buscarEntidade(id);
        t.setStatus(estado);
        return toResponse(tarefaRepository.save(t));
    }

    @Override
    public void remover(Long id) {
        tarefaRepository.delete(buscarEntidade(id));
    }

    @Override
    @Transactional(readOnly = true)
    public TarefaResponse buscarPorId(Long id) {
        return toResponse(buscarEntidade(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TarefaResponse> listarPorManutencao(Long manutencaoId) {
        return tarefaRepository.findByManutencaoId(manutencaoId).stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TarefaResponse> listarPorUtilizador(Long utilizadorId) {
        return tarefaRepository.findByUtilizadorId(utilizadorId).stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TarefaResponse> listarPorEstado(EstadoTarefa estado) {
        return tarefaRepository.findByStatus(estado).stream().map(this::toResponse).collect(Collectors.toList());
    }

    private Tarefa buscarEntidade(Long id) {
        return tarefaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tarefa não encontrada com ID: " + id));
    }

    private TarefaResponse toResponse(Tarefa t) {
        return TarefaResponse.builder()
                .idTarefa(t.getIdTarefa())
                .status(t.getStatus())
                .descricao(t.getDescricao())
                .utilizadorId(t.getUtilizador() != null ? t.getUtilizador().getId() : null)
                .utilizadorNome(t.getUtilizador() != null ? t.getUtilizador().getNome() : null)
                .manutencaoId(t.getManutencao() != null ? t.getManutencao().getId() : null)
                .build();
    }
}
