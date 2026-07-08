package com.cima.system.dto.request;

import com.cima.system.enums.EstadoManutencao;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ManutencaoRequest {

    @NotBlank(message = "Tipo é obrigatório")
    private String idTipo;

    @NotNull(message = "Data agendada é obrigatória")
    private LocalDate dataAgendada;

    private String descricao;

    private EstadoManutencao estado;

    private LocalDate dataExecucao;

    private BigDecimal custo;

    private Long utilizadorId;

    private Long tipoManutencaoId;

    private Long maquinaVeiculoId;

    private Long inventarioId;

    private Long movimentoStockId;
    // ✅ adiciona este campo
    @Min(value = 0)
    private Integer quantidade;
}
