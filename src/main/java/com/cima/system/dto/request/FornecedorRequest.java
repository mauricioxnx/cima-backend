package com.cima.system.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class FornecedorRequest {

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    private String nif;

    private String telefone;

    @Email(message = "Email inválido")
    private String email;

    private String categoria;

    private String endereco;
}
