package com.cima.system.dto.response;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TipoManutencaoResponse {
    private Long id;
    private String nomeTipo;
    private String descricao;
}
