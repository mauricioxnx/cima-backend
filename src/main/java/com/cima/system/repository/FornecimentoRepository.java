package com.cima.system.repository;

import com.cima.system.entity.Fornecimento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FornecimentoRepository extends JpaRepository<Fornecimento, Long> {
    List<Fornecimento> findByFornecedorId(Long fornecedorId);
    List<Fornecimento> findByInventarioId(Long inventarioId);
}
