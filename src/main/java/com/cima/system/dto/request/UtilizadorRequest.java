package com.cima.system.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UtilizadorRequest {

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 3, max = 100)
    private String nome;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email inválido")
    private String email;

    @NotBlank(message = "Senha é obrigatória")
    @Size(min = 6, message = "Senha deve ter pelo menos 6 caracteres")
    private String senha;

    @Size(max = 20)
    private String telefone;

    @Size(max = 255)
    private String endereco;

    @NotNull(message = "Perfil é obrigatório")
    private Long perfilId;

    private Boolean ativo = true;
}
