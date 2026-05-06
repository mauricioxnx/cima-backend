package com.cima.system.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class MaquinaVeiculoRequest {

    @NotBlank(message = "Modelo é obrigatório")
    private String modelo;

    private LocalDate dataAquisicao;

    private String estado;

    private String matriculaNSerie;

    private String tipo;

    private Long inventarioId;
}
