package com.cima.system.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TipoManutencaoRequest {

    @NotBlank(message = "Nome do tipo é obrigatório")
    private String nomeTipo;

    private String descricao;
}
