package com.cima.system.dto.response;

import com.cima.system.enums.EstadoManutencao;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ManutencaoResponse {
    private Long id;
    private String idTipo;
    private LocalDate dataAgendada;
    private LocalDate dataExecucao;
    private String descricao;
    private EstadoManutencao estado;
    private BigDecimal custo;
    private Long utilizadorId;
    private String utilizadorNome;
    private Long tipoManutencaoId;
    private String tipoManutencaoNome;
    private Long maquinaVeiculoId;
    private String maquinaVeiculoModelo;
    private Long inventarioId;
    private Long movimentoStockId;
    private String movimentoStockTipo;
    private LocalDateTime movimentoStockData;
}
