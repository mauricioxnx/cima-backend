package com.cima.system.entity;

import com.cima.system.enums.EstadoTarefa;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tarefa")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tarefa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tarefa")
    private Long idTarefa;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoTarefa status;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_utilizador")
    private Utilizador utilizador;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_manutencao", nullable = false)
    private Manutencao manutencao;
}
