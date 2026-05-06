package com.cima.system.repository;

import com.cima.system.entity.MovimentoStock;
import com.cima.system.enums.TipoMovimento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MovimentoStockRepository extends JpaRepository<MovimentoStock, Long> {
    List<MovimentoStock> findByInventarioId(Long inventarioId);
    List<MovimentoStock> findByTipoMovimento(TipoMovimento tipo);
    List<MovimentoStock> findByDataMovimentoBetween(LocalDateTime inicio, LocalDateTime fim);

    @Query("SELECT m FROM MovimentoStock m WHERE m.inventario.id = :inventarioId ORDER BY m.dataMovimento DESC")
    List<MovimentoStock> findByInventarioIdOrderByDataDesc(Long inventarioId);

    @Query("SELECT SUM(m.quantidade) FROM MovimentoStock m WHERE m.inventario.id = :id AND m.tipoMovimento = 'ENTRADA'")
    Integer totalEntradasPorInventario(Long id);

    @Query("SELECT SUM(m.quantidade) FROM MovimentoStock m WHERE m.inventario.id = :id AND m.tipoMovimento = 'SAIDA'")
    Integer totalSaidasPorInventario(Long id);
}
