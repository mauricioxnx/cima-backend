package com.cima.system.dto.response;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PerfilResponse {
    private Long id;
    private String nome;
    private String descricao;
    private int totalUtilizadores;
}
