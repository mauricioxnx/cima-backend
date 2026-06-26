package com.cima.system.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class InventarioRequest {

    @NotBlank(message = "Descrição é obrigatória")
    @Size(max = 255)
    private String descricao;

    private String descricao3;

    @Size(max = 20)
    private String unidadeBase;

    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal precoVenda1;

    private BigDecimal precoVenda2;
    private BigDecimal precoVenda3;
    private BigDecimal preco;

    @NotNull(message = "Quantidade é obrigatória")
    @Min(value = 0, message = "Quantidade não pode ser negativa")
    private Integer quantidade;

    private Long fornecedorId; // opcional — pode ser null
}