package com.cima.system.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "inventario")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Inventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String descricao;

    @Column(name = "descricao3", columnDefinition = "TEXT")
    private String descricao3;

    @Column(name = "unidade_base", length = 20)
    private String unidadeBase;

    @Column(name = "preco_venda1", precision = 15, scale = 2)
    private BigDecimal precoVenda1;

    @Column(name = "preco_venda2", precision = 15, scale = 2)
    private BigDecimal precoVenda2;

    @Column(name = "preco_venda3", precision = 15, scale = 2)
    private BigDecimal precoVenda3;

    @Column(precision = 15, scale = 2)
    private BigDecimal preco;

    @Column(nullable = false)
    @Builder.Default
    private Integer quantidade = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fornecedor_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Fornecedor fornecedor;

    @OneToMany(mappedBy = "inventario", fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<MovimentoStock> movimentos;

    @OneToMany(mappedBy = "inventario", fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Historico> historicos;

    @OneToMany(mappedBy = "inventario", fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Manutencao> manutencoes;
}