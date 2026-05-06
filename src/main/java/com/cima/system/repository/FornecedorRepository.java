package com.cima.system.repository;

import com.cima.system.entity.Fornecedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FornecedorRepository extends JpaRepository<Fornecedor, Long> {
    Optional<Fornecedor> findByNif(String nif);
    boolean existsByNif(String nif);
    List<Fornecedor> findByNomeContainingIgnoreCase(String nome);
    List<Fornecedor> findByCategoria(String categoria);
}
