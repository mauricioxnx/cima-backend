package com.cima.system.repository;

import com.cima.system.entity.Inventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventarioRepository extends JpaRepository<Inventario, Long> {

    List<Inventario> findByDescricaoContainingIgnoreCase(String descricao);

    List<Inventario> findByFornecedorId(Long fornecedorId);

    @Query("SELECT i FROM Inventario i WHERE i.quantidade <= :minimo")
    List<Inventario> findEstoqueBaixo(int minimo);

    @Query("SELECT i FROM Inventario i WHERE i.quantidade = 0")
    List<Inventario> findSemEstoque();
}
