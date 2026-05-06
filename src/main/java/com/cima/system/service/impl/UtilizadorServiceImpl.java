package com.cima.system.service.impl;

import com.cima.system.dto.request.UtilizadorRequest;
import com.cima.system.dto.response.UtilizadorResponse;
import com.cima.system.entity.Perfil;
import com.cima.system.entity.Utilizador;
import com.cima.system.exception.BusinessException;
import com.cima.system.exception.ResourceNotFoundException;
import com.cima.system.repository.PerfilRepository;
import com.cima.system.repository.UtilizadorRepository;
import com.cima.system.service.UtilizadorService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UtilizadorServiceImpl implements UtilizadorService {

    private final UtilizadorRepository utilizadorRepository;
    private final PerfilRepository perfilRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UtilizadorResponse criar(UtilizadorRequest request) {
        if (utilizadorRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Já existe um utilizador com o email: " + request.getEmail());
        }
        Perfil perfil = perfilRepository.findById(request.getPerfilId())
                .orElseThrow(() -> new ResourceNotFoundException("Perfil não encontrado com ID: " + request.getPerfilId()));

        Utilizador utilizador = Utilizador.builder()
                .nome(request.getNome())
                .email(request.getEmail())
                .senha(passwordEncoder.encode(request.getSenha()))
                .telefone(request.getTelefone())
                .endereco(request.getEndereco())
                .ativo(request.getAtivo() != null ? request.getAtivo() : true)
                .perfil(perfil)
                .build();
        return toResponse(utilizadorRepository.save(utilizador));
    }

    @Override
    public UtilizadorResponse atualizar(Long id, UtilizadorRequest request) {
        Utilizador utilizador = buscarEntidade(id);
        if (!utilizador.getEmail().equals(request.getEmail()) && utilizadorRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Já existe um utilizador com o email: " + request.getEmail());
        }
        Perfil perfil = perfilRepository.findById(request.getPerfilId())
                .orElseThrow(() -> new ResourceNotFoundException("Perfil não encontrado com ID: " + request.getPerfilId()));

        utilizador.setNome(request.getNome());
        utilizador.setEmail(request.getEmail());
        utilizador.setTelefone(request.getTelefone());
        utilizador.setEndereco(request.getEndereco());
        utilizador.setPerfil(perfil);
        if (request.getAtivo() != null) utilizador.setAtivo(request.getAtivo());

        return toResponse(utilizadorRepository.save(utilizador));
    }

    @Override
    public void remover(Long id) {
        Utilizador u = buscarEntidade(id);
        utilizadorRepository.delete(u);
    }

    @Override
    public void desativar(Long id) {
        Utilizador u = buscarEntidade(id);
        u.setAtivo(false);
        utilizadorRepository.save(u);
    }

    @Override
    public void ativar(Long id) {
        Utilizador u = buscarEntidade(id);
        u.setAtivo(true);
        utilizadorRepository.save(u);
    }

    @Override
    @Transactional(readOnly = true)
    public UtilizadorResponse buscarPorId(Long id) {
        return toResponse(buscarEntidade(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UtilizadorResponse> listarTodos() {
        return utilizadorRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UtilizadorResponse> listarAtivos() {
        return utilizadorRepository.findByAtivoTrue().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UtilizadorResponse> listarPorPerfil(Long perfilId) {
        return utilizadorRepository.findByPerfilId(perfilId).stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public void alterarSenha(Long id, String senhaAtual, String novaSenha) {
        Utilizador u = buscarEntidade(id);
        if (!passwordEncoder.matches(senhaAtual, u.getSenha())) {
            throw new BusinessException("Senha atual incorreta.");
        }
        u.setSenha(passwordEncoder.encode(novaSenha));
        utilizadorRepository.save(u);
    }

    private Utilizador buscarEntidade(Long id) {
        return utilizadorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilizador não encontrado com ID: " + id));
    }

    private UtilizadorResponse toResponse(Utilizador u) {
        return UtilizadorResponse.builder()
                .id(u.getId())
                .nome(u.getNome())
                .email(u.getEmail())
                .telefone(u.getTelefone())
                .endereco(u.getEndereco())
                .ativo(u.getAtivo())
                .perfilId(u.getPerfil() != null ? u.getPerfil().getId() : null)
                .perfilNome(u.getPerfil() != null ? u.getPerfil().getNome() : null)
                .criadoEm(u.getCriadoEm())
                .atualizadoEm(u.getAtualizadoEm())
                .build();
    }
}
