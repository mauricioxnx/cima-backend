package com.cima.system.dto.response;

import com.cima.system.enums.TipoMovimento;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovimentoStockResponse {
    private Long idMovimento;
    private TipoMovimento tipoMovimento;
    private LocalDateTime dataMovimento;
    private String documentoRef;
    private BigDecimal preco;
    private Integer quantidade;
    private Long inventarioId;
    private String inventarioCodigo;
    private String inventarioDescricao;
}
