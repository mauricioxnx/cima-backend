package com.cima.system.dto.request;

import com.cima.system.enums.TipoMovimento;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class MovimentoStockRequest {

    @NotNull(message = "Tipo de movimento é obrigatório")
    private TipoMovimento tipoMovimento;

    private String documentoRef;

    private BigDecimal preco;

    @NotNull(message = "Quantidade é obrigatória")
    @Min(value = 1, message = "Quantidade deve ser pelo menos 1")
    private Integer quantidade;

    @NotNull(message = "ID do inventário é obrigatório")
    private Long inventarioId;
}
