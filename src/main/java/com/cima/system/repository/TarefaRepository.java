package com.cima.system.repository;

import com.cima.system.entity.Tarefa;
import com.cima.system.enums.EstadoTarefa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TarefaRepository extends JpaRepository<Tarefa, Long> {
    List<Tarefa> findByManutencaoId(Long manutencaoId);
    List<Tarefa> findByUtilizadorId(Long utilizadorId);
    List<Tarefa> findByStatus(EstadoTarefa status);
}
