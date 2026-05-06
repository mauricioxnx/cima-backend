package com.cima.system.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "maquina_veiculo")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaquinaVeiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String modelo;

    @Column(name = "data_aquisicao")
    private LocalDate dataAquisicao;

    @Column(length = 50)
    private String estado;

    @Column(name = "matricula_n_serie", unique = true, length = 50)
    private String matriculaNSerie;

    @Column(length = 100)
    private String tipo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_inventario")
    private Inventario inventario;

    @OneToMany(mappedBy = "maquinaVeiculo", fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Manutencao> manutencoes;
}
