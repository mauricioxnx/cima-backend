package com.cima.system.service.impl;

import com.cima.system.dto.request.ManutencaoRequest;
import com.cima.system.dto.response.ManutencaoResponse;
import com.cima.system.entity.*;
import com.cima.system.enums.EstadoManutencao;
import com.cima.system.enums.TipoMovimento;
import com.cima.system.exception.ResourceNotFoundException;
import com.cima.system.repository.*;
import com.cima.system.service.HistoricoService;
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
    private final MovimentoStockRepository movimentoStockRepository;
    private final HistoricoService historicoService;

    @Override
    public ManutencaoResponse criar(ManutencaoRequest request) {
        Manutencao m = buildFromRequest(new Manutencao(), request);
        m.setEstado(EstadoManutencao.PENDENTE);
        Manutencao saved = manutencaoRepository.save(m);

        // ✅ se tem peça de inventário, regista saída de stock automaticamente
        if (request.getInventarioId() != null && request.getQuantidade() != null && request.getQuantidade() > 0) {
            Inventario inv = inventarioRepository.findById(request.getInventarioId()).orElse(null);
            if (inv != null) {
                if (inv.getQuantidade() >= request.getQuantidade()) {
                    inv.setQuantidade(inv.getQuantidade() - request.getQuantidade());
                    inventarioRepository.save(inv);
                }
                MovimentoStock mov = MovimentoStock.builder()
                        .tipoMovimento(TipoMovimento.SAIDA)
                        .quantidade(request.getQuantidade())
                        .documentoRef("MANUTENCAO-#" + saved.getId())
                        .inventario(inv)
                        .build();
                movimentoStockRepository.save(mov);
            }
        }

        // ✅ histórico
        historicoService.registar(
                "Manutenção criada: " + (saved.getDescricao() != null ? saved.getDescricao() : saved.getIdTipo()),
                request.getUtilizadorId(),
                request.getInventarioId(),
                saved.getId()
        );

        return toResponse(saved);
    }

    @Override
    public ManutencaoResponse atualizar(Long id, ManutencaoRequest request) {
        Manutencao m = buscarEntidade(id);
        buildFromRequest(m, request);
        Manutencao saved = manutencaoRepository.save(m);

        historicoService.registar(
                "Manutenção #" + id + " actualizada",
                request.getUtilizadorId(),
                request.getInventarioId(),
                id
        );

        return toResponse(saved);
    }

    @Override
    public ManutencaoResponse atualizarEstado(Long id, EstadoManutencao estado) {
        Manutencao m = buscarEntidade(id);
        m.setEstado(estado);
        if (estado == EstadoManutencao.CONCLUIDA && m.getDataExecucao() == null) {
            m.setDataExecucao(LocalDate.now());
        }
        Manutencao saved = manutencaoRepository.save(m);

        historicoService.registar(
                "Estado da manutenção #" + id + " alterado para: " + estado,
                saved.getUtilizador() != null ? saved.getUtilizador().getId() : null,
                null,
                id
        );

        return toResponse(saved);
    }

    @Override
    public void remover(Long id) {
        Manutencao m = buscarEntidade(id);

        historicoService.registar(
                "Manutenção #" + id + " eliminada: " + (m.getDescricao() != null ? m.getDescricao() : m.getIdTipo()),
                null, null, id
        );

        manutencaoRepository.delete(m);
    }

    @Override
    @Transactional(readOnly = true)
    public ManutencaoResponse buscarPorId(Long id) {
        return toResponse(buscarEntidade(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ManutencaoResponse> listarTodos() {
        return manutencaoRepository.findAll().stream()
                .map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ManutencaoResponse> listarPorEstado(EstadoManutencao estado) {
        return manutencaoRepository.findByEstado(estado).stream()
                .map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ManutencaoResponse> listarPorMaquina(Long maquinaId) {
        return manutencaoRepository.findByMaquinaVeiculoId(maquinaId).stream()
                .map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ManutencaoResponse> listarPorPeriodo(LocalDate inicio, LocalDate fim) {
        return manutencaoRepository.findByDataAgendadaBetween(inicio, fim).stream()
                .map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ManutencaoResponse> listarAtrasadas() {
        return manutencaoRepository.findAtrasadas(LocalDate.now()).stream()
                .map(this::toResponse).collect(Collectors.toList());
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
        } else {
            m.setInventario(null);
        }
        if (r.getMovimentoStockId() != null) {
            m.setMovimentoStock(movimentoStockRepository.findById(r.getMovimentoStockId())
                    .orElseThrow(() -> new ResourceNotFoundException("Movimento de stock não encontrado: " + r.getMovimentoStockId())));
        } else {
            m.setMovimentoStock(null);
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
                .movimentoStockId(m.getMovimentoStock() != null ? m.getMovimentoStock().getIdMovimento() : null)
                .movimentoStockTipo(m.getMovimentoStock() != null ? m.getMovimentoStock().getTipoMovimento().name() : null)
                .movimentoStockData(m.getMovimentoStock() != null ? m.getMovimentoStock().getDataMovimento() : null)
                .build();
    }
}