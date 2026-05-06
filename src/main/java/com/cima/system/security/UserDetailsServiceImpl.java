package com.cima.system.security;

import com.cima.system.entity.Utilizador;
import com.cima.system.repository.UtilizadorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UtilizadorRepository utilizadorRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Utilizador utilizador = utilizadorRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Utilizador não encontrado: " + email));

        if (!utilizador.getAtivo()) {
            throw new UsernameNotFoundException("Utilizador inativo: " + email);
        }

        // ✅ Usa o nome exacto da BD — "ADMINISTRADOR" coincide com @PreAuthorize("hasAuthority('ADMINISTRADOR')")
        String authority = utilizador.getPerfil().getNome();

        return new User(
                utilizador.getEmail(),
                utilizador.getSenha(),
                List.of(new SimpleGrantedAuthority(authority))
        );
    }
}