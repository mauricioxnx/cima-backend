package com.cima.system.dto.response;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FornecedorResponse {
    private Long id;
    private String nome;
    private String nif;
    private String telefone;
    private String email;
    private String categoria;
    private String endereco;
}
