package com.cima.system.controller;

import com.cima.system.dto.request.LoginRequest;
import com.cima.system.dto.response.ApiResponse;
import com.cima.system.dto.response.LoginResponse;
import com.cima.system.entity.Utilizador;
import com.cima.system.repository.UtilizadorRepository;
import com.cima.system.security.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticação", description = "Login e gestão de sessão")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UtilizadorRepository utilizadorRepository;

    @PostMapping("/login")
    @Operation(summary = "Login no sistema")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getSenha())
        );
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        String token = jwtUtil.generateToken(userDetails);

        Utilizador utilizador = utilizadorRepository.findByEmail(request.getEmail())
                .orElseThrow();

        LoginResponse response = LoginResponse.builder()
                .token(token)
                .tipo("Bearer")
                .id(utilizador.getId())
                .nome(utilizador.getNome())
                .email(utilizador.getEmail())
                .perfil(utilizador.getPerfil().getNome())
                .expiresIn(jwtUtil.getExpiration())
                .build();

        return ResponseEntity.ok(ApiResponse.sucesso(response, "Login realizado com sucesso"));
    }
}
