package com.cima.system.dto.response;

import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MaquinaVeiculoResponse {
    private Long id;
    private String modelo;
    private LocalDate dataAquisicao;
    private String estado;
    private String matriculaNSerie;
    private String tipo;
    private Long inventarioId;
}
