package com.cima.system.dto.response;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private String tipo;
    private Long id;
    private String nome;
    private String email;
    private String perfil;
    private long expiresIn;
}
