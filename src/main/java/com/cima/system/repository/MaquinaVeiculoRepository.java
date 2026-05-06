package com.cima.system.repository;

import com.cima.system.entity.MaquinaVeiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MaquinaVeiculoRepository extends JpaRepository<MaquinaVeiculo, Long> {
    Optional<MaquinaVeiculo> findByMatriculaNSerie(String matricula);
    boolean existsByMatriculaNSerie(String matricula);
    List<MaquinaVeiculo> findByEstado(String estado);
    List<MaquinaVeiculo> findByModeloContainingIgnoreCase(String modelo);
}
