package com.cima.system.dto.response;

import com.cima.system.enums.EstadoTarefa;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TarefaResponse {
    private Long idTarefa;
    private EstadoTarefa status;
    private String descricao;
    private Long utilizadorId;
    private String utilizadorNome;
    private Long manutencaoId;
}
