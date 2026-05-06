package com.cima.system.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventarioResponse {
    private Long id;
    private String codigo;
    private String descricao;
    private String descricao3;
    private String unidadeBase;
    private BigDecimal precoVenda1;
    private BigDecimal precoVenda2;
    private BigDecimal precoVenda3;
    private BigDecimal preco;
    private Integer quantidade;
}
