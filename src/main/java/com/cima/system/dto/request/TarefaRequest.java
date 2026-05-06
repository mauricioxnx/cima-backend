package com.cima.system.dto.request;

import com.cima.system.enums.EstadoTarefa;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TarefaRequest {

    @NotNull(message = "Estado é obrigatório")
    private EstadoTarefa status;

    private String descricao;

    private Long utilizadorId;

    @NotNull(message = "ID da manutenção é obrigatório")
    private Long manutencaoId;
}
