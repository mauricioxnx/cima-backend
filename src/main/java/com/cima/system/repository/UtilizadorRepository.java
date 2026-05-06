package com.cima.system.repository;

import com.cima.system.entity.Utilizador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UtilizadorRepository extends JpaRepository<Utilizador, Long> {
    Optional<Utilizador> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByNome(String nome);
    List<Utilizador> findByAtivoTrue();
    List<Utilizador> findByPerfilId(Long perfilId);

    @Query("SELECT u FROM Utilizador u WHERE u.ativo = true AND u.perfil.nome = :perfilNome")
    List<Utilizador> findByPerfilNome(String perfilNome);
}
