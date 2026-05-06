package com.cima.system.dto.response;

import lombok.*;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResponse {
    private long totalUtilizadores;
    private long totalInventario;
    private long totalFornecedores;
    private long totalManutencoesAtivas;
    private long manutencoesPendentes;
    private long manutencoesEmCurso;
    private long manutencoesConcluidas;
    private long totalTarefasAbertas;
    private long itensEstoqueBaixo;
    private long itensStockZero;
    private long totalMovimentosHoje;
    private Map<String, Long> manutencoesPorEstado;
    private Map<String, Long> movimentosPorTipo;
}
