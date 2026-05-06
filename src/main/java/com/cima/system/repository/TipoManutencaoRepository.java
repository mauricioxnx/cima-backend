package com.cima.system.repository;

import com.cima.system.entity.TipoManutencao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TipoManutencaoRepository extends JpaRepository<TipoManutencao, Long> {
    Optional<TipoManutencao> findByNomeTipo(String nomeTipo);
    boolean existsByNomeTipo(String nomeTipo);
}
