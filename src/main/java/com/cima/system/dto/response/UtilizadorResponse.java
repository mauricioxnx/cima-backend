package com.cima.system.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UtilizadorResponse {
    private Long id;
    private String nome;
    private String email;
    private String telefone;
    private String endereco;
    private Boolean ativo;
    private Long perfilId;
    private String perfilNome;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;
}
