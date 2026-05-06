package com.cima.system.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "tipo_manutencao")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipoManutencao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome_tipo", nullable = false, unique = true, length = 100)
    private String nomeTipo;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @OneToMany(mappedBy = "tipoManutencao", fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Manutencao> manutencoes;
}
