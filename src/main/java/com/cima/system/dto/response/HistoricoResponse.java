package com.cima.system.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HistoricoResponse {
    private Long id;
    private String descricao;
    private LocalDateTime dataExecucao;
    private Long utilizadorId;
    private String utilizadorNome;
    private Long inventarioId;
    private Long manutencaoId;
}
