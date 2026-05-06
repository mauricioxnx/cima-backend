package com.cima.system.service.impl;

import com.cima.system.dto.response.DashboardResponse;
import com.cima.system.enums.EstadoManutencao;
import com.cima.system.enums.TipoMovimento;
import com.cima.system.repository.*;
import com.cima.system.service.RelatorioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RelatorioServiceImpl implements RelatorioService {

    private final UtilizadorRepository utilizadorRepository;
    private final InventarioRepository inventarioRepository;
    private final FornecedorRepository fornecedorRepository;
    private final ManutencaoRepository manutencaoRepository;
    private final TarefaRepository tarefaRepository;
    private final MovimentoStockRepository movimentoRepository;

    @Override
    public DashboardResponse obterDashboard() {
        LocalDateTime inicioHoje = LocalDate.now().atStartOfDay();
        LocalDateTime fimHoje = inicioHoje.plusDays(1);

        Map<String, Long> porEstado = new LinkedHashMap<>();
        for (EstadoManutencao e : EstadoManutencao.values()) {
            porEstado.put(e.name(), manutencaoRepository.countByEstado(e));
        }

        Map<String, Long> porMovimento = new LinkedHashMap<>();
        for (TipoMovimento t : TipoMovimento.values()) {
            porMovimento.put(t.name(), (long) movimentoRepository.findByTipoMovimento(t).size());
        }

        return DashboardResponse.builder()
                .totalUtilizadores(utilizadorRepository.count())
                .totalInventario(inventarioRepository.count())
                .totalFornecedores(fornecedorRepository.count())
                .totalManutencoesAtivas(manutencaoRepository.countByEstado(EstadoManutencao.EM_CURSO))
                .manutencoesPendentes(manutencaoRepository.countByEstado(EstadoManutencao.PENDENTE))
                .manutencoesEmCurso(manutencaoRepository.countByEstado(EstadoManutencao.EM_CURSO))
                .manutencoesConcluidas(manutencaoRepository.countByEstado(EstadoManutencao.CONCLUIDA))
                .totalTarefasAbertas((long) tarefaRepository.findByStatus(com.cima.system.enums.EstadoTarefa.ABERTA).size())
                .itensEstoqueBaixo((long) inventarioRepository.findEstoqueBaixo(5).size())
                .itensStockZero((long) inventarioRepository.findSemEstoque().size())
                .totalMovimentosHoje((long) movimentoRepository.findByDataMovimentoBetween(inicioHoje, fimHoje).size())
                .manutencoesPorEstado(porEstado)
                .movimentosPorTipo(porMovimento)
                .build();
    }

    @Override
    public Map<String, Object> relatorioInventario() {
        Map<String, Object> rel = new HashMap<>();
        rel.put("totalProdutos", inventarioRepository.count());
        rel.put("semEstoque", inventarioRepository.findSemEstoque().size());
        rel.put("estoqueBaixo", inventarioRepository.findEstoqueBaixo(5).size());
        rel.put("produtos", inventarioRepository.findAll());
        return rel;
    }

    @Override
    public Map<String, Object> relatorioManutencoes(LocalDate inicio, LocalDate fim) {
        Map<String, Object> rel = new HashMap<>();
        var manutencoes = manutencaoRepository.findByDataAgendadaBetween(inicio, fim);
        rel.put("periodo", Map.of("inicio", inicio, "fim", fim));
        rel.put("total", manutencoes.size());
        rel.put("pendentes", manutencoes.stream().filter(m -> m.getEstado() == EstadoManutencao.PENDENTE).count());
        rel.put("concluidas", manutencoes.stream().filter(m -> m.getEstado() == EstadoManutencao.CONCLUIDA).count());
        rel.put("atrasadas", manutencaoRepository.findAtrasadas(LocalDate.now()).size());
        rel.put("manutencoes", manutencoes);
        return rel;
    }

    @Override
    public Map<String, Object> relatorioMovimentosStock(LocalDate inicio, LocalDate fim) {
        LocalDateTime dtInicio = inicio.atStartOfDay();
        LocalDateTime dtFim = fim.plusDays(1).atStartOfDay();
        var movimentos = movimentoRepository.findByDataMovimentoBetween(dtInicio, dtFim);
        Map<String, Object> rel = new HashMap<>();
        rel.put("periodo", Map.of("inicio", inicio, "fim", fim));
        rel.put("total", movimentos.size());
        rel.put("entradas", movimentos.stream().filter(m -> m.getTipoMovimento() == TipoMovimento.ENTRADA).count());
        rel.put("saidas", movimentos.stream().filter(m -> m.getTipoMovimento() == TipoMovimento.SAIDA).count());
        rel.put("movimentos", movimentos);
        return rel;
    }

    @Override
    public Map<String, Object> relatorioUtilizadores() {
        Map<String, Object> rel = new HashMap<>();
        rel.put("total", utilizadorRepository.count());
        rel.put("ativos", utilizadorRepository.findByAtivoTrue().size());
        rel.put("utilizadores", utilizadorRepository.findAll());
        return rel;
    }
}
