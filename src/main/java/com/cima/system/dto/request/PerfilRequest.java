package com.cima.system.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PerfilRequest {

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, max = 100)
    private String nome;

    private String descricao;
}
