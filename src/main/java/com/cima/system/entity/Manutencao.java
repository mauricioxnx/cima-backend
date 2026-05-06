package com.cima.system.entity;

import com.cima.system.enums.EstadoManutencao;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "manutencao")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Manutencao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_tipo", nullable = false)
    private String idTipo;

    @Column(name = "data_agendada")
    private LocalDate dataAgendada;

    @Column(name = "id_inventario")
    private Long idInventario;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Column(length = 50)
    @Enumerated(EnumType.STRING)
    private EstadoManutencao estado;

    @Column(name = "data_execucao")
    private LocalDate dataExecucao;

    @Column(precision = 15, scale = 2)
    private BigDecimal custo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_utilizador")
    private Utilizador utilizador;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_manutencao_id")
    private TipoManutencao tipoManutencao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maquina_veiculo_id")
    private MaquinaVeiculo maquinaVeiculo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventario_id")
    private Inventario inventario;

    @OneToMany(mappedBy = "manutencao", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Tarefa> tarefas;

    @OneToMany(mappedBy = "manutencao", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Historico> historicos;
}
