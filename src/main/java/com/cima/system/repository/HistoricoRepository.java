package com.cima.system.repository;

import com.cima.system.entity.Historico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface HistoricoRepository extends JpaRepository<Historico, Long> {
    List<Historico> findByUtilizadorIdOrderByDataExecucaoDesc(Long utilizadorId);
    List<Historico> findByInventarioIdOrderByDataExecucaoDesc(Long inventarioId);
    List<Historico> findByManutencaoIdOrderByDataExecucaoDesc(Long manutencaoId);
    List<Historico> findByDataExecucaoBetweenOrderByDataExecucaoDesc(LocalDateTime inicio, LocalDateTime fim);
    List<Historico> findAllByOrderByDataExecucaoDesc();
}
