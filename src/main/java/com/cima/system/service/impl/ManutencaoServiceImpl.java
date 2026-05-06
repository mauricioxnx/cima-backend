package com.cima.system.service.impl;

import com.cima.system.dto.request.ManutencaoRequest;
import com.cima.system.dto.response.ManutencaoResponse;
import com.cima.system.entity.*;
import com.cima.system.enums.EstadoManutencao;
import com.cima.system.exception.ResourceNotFoundException;
import com.cima.system.repository.*;
import com.cima.system.service.ManutencaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ManutencaoServiceImpl implements ManutencaoService {

    private final ManutencaoRepository manutencaoRepository;
    private final UtilizadorRepository utilizadorRepository;
    private final TipoManutencaoRepository tipoManutencaoRepository;
    private final MaquinaVeiculoRepository maquinaVeiculoRepository;
    private final InventarioRepository inventarioRepository;

    @Override
    public ManutencaoResponse criar(ManutencaoRequest request) {
        Manutencao m = buildFromRequest(new Manutencao(), request);
        m.setEstado(EstadoManutencao.PENDENTE);
        return toResponse(manutencaoRepository.save(m));
    }

    @Override
    public ManutencaoResponse atualizar(Long id, ManutencaoRequest request) {
        Manutencao m = buscarEntidade(id);
        buildFromRequest(m, request);
        return toResponse(manutencaoRepository.save(m));
    }

    @Override
    public ManutencaoResponse atualizarEstado(Long id, EstadoManutencao estado) {
        Manutencao m = buscarEntidade(id);
        m.setEstado(estado);
        if (estado == EstadoManutencao.CONCLUIDA && m.getDataExecucao() == null) {
            m.setDataExecucao(LocalDate.now());
        }
        return toResponse(manutencaoRepository.save(m));
    }

    @Override
    public void remover(Long id) {
        manutencaoRepository.delete(buscarEntidade(id));
    }

    @Override
    @Transactional(readOnly = true)
    public ManutencaoResponse buscarPorId(Long id) {
        return toResponse(buscarEntidade(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ManutencaoResponse> listarTodos() {
        return manutencaoRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ManutencaoResponse> listarPorEstado(EstadoManutencao estado) {
        return manutencaoRepository.findByEstado(estado).stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ManutencaoResponse> listarPorMaquina(Long maquinaId) {
        return manutencaoRepository.findByMaquinaVeiculoId(maquinaId).stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ManutencaoResponse> listarPorPeriodo(LocalDate inicio, LocalDate fim) {
        return manutencaoRepository.findByDataAgendadaBetween(inicio, fim).stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ManutencaoResponse> listarAtrasadas() {
        return manutencaoRepository.findAtrasadas(LocalDate.now()).stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ManutencaoResponse> listarProximas(int dias) {
        return manutencaoRepository.findProximas(LocalDate.now(), LocalDate.now().plusDays(dias))
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    private Manutencao buildFromRequest(Manutencao m, ManutencaoRequest r) {
        m.setIdTipo(r.getIdTipo());
        m.setDataAgendada(r.getDataAgendada());
        m.setDescricao(r.getDescricao());
        m.setCusto(r.getCusto());
        m.setDataExecucao(r.getDataExecucao());
        if (r.getEstado() != null) m.setEstado(r.getEstado());

        if (r.getUtilizadorId() != null) {
            m.setUtilizador(utilizadorRepository.findById(r.getUtilizadorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Utilizador não encontrado: " + r.getUtilizadorId())));
        }
        if (r.getTipoManutencaoId() != null) {
            m.setTipoManutencao(tipoManutencaoRepository.findById(r.getTipoManutencaoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Tipo de manutenção não encontrado: " + r.getTipoManutencaoId())));
        }
        if (r.getMaquinaVeiculoId() != null) {
            m.setMaquinaVeiculo(maquinaVeiculoRepository.findById(r.getMaquinaVeiculoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Máquina/Veículo não encontrado: " + r.getMaquinaVeiculoId())));
        }
        if (r.getInventarioId() != null) {
            m.setInventario(inventarioRepository.findById(r.getInventarioId())
                    .orElseThrow(() -> new ResourceNotFoundException("Inventário não encontrado: " + r.getInventarioId())));
        }
        return m;
    }

    private Manutencao buscarEntidade(Long id) {
        return manutencaoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Manutenção não encontrada com ID: " + id));
    }

    private ManutencaoResponse toResponse(Manutencao m) {
        return ManutencaoResponse.builder()
                .id(m.getId()).idTipo(m.getIdTipo())
                .dataAgendada(m.getDataAgendada()).dataExecucao(m.getDataExecucao())
                .descricao(m.getDescricao()).estado(m.getEstado()).custo(m.getCusto())
                .utilizadorId(m.getUtilizador() != null ? m.getUtilizador().getId() : null)
                .utilizadorNome(m.getUtilizador() != null ? m.getUtilizador().getNome() : null)
                .tipoManutencaoId(m.getTipoManutencao() != null ? m.getTipoManutencao().getId() : null)
                .tipoManutencaoNome(m.getTipoManutencao() != null ? m.getTipoManutencao().getNomeTipo() : null)
                .maquinaVeiculoId(m.getMaquinaVeiculo() != null ? m.getMaquinaVeiculo().getId() : null)
                .maquinaVeiculoModelo(m.getMaquinaVeiculo() != null ? m.getMaquinaVeiculo().getModelo() : null)
                .inventarioId(m.getInventario() != null ? m.getInventario().getId() : null)
                .build();
    }
}
