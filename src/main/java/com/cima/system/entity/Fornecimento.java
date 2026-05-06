package com.cima.system.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "fornecimento")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Fornecimento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_fornecedor", nullable = false)
    private Fornecedor fornecedor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_inventario", nullable = false)
    private Inventario inventario;

    @Column(length = 255)
    private String condicoes;

    @Column(length = 100)
    private String preco;
}
