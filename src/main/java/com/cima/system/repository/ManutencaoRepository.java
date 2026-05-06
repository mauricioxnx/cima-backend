package com.cima.system.repository;

import com.cima.system.entity.Manutencao;
import com.cima.system.enums.EstadoManutencao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ManutencaoRepository extends JpaRepository<Manutencao, Long> {
    List<Manutencao> findByEstado(EstadoManutencao estado);
    List<Manutencao> findByUtilizadorId(Long utilizadorId);
    List<Manutencao> findByMaquinaVeiculoId(Long maquinaId);
    List<Manutencao> findByDataAgendadaBetween(LocalDate inicio, LocalDate fim);

    @Query("SELECT m FROM Manutencao m WHERE m.dataAgendada <= :hoje AND m.estado = 'PENDENTE'")
    List<Manutencao> findAtrasadas(LocalDate hoje);

    @Query("SELECT m FROM Manutencao m WHERE m.dataAgendada BETWEEN :hoje AND :limite AND m.estado = 'PENDENTE'")
    List<Manutencao> findProximas(LocalDate hoje, LocalDate limite);

    @Query("SELECT COUNT(m) FROM Manutencao m WHERE m.estado = :estado")
    long countByEstado(EstadoManutencao estado);
}
