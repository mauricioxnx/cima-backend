package com.cima.system.service.impl;

import com.cima.system.dto.request.FornecedorRequest;
import com.cima.system.dto.response.FornecedorResponse;
import com.cima.system.entity.Fornecedor;
import com.cima.system.exception.BusinessException;
import com.cima.system.exception.ResourceNotFoundException;
import com.cima.system.repository.FornecedorRepository;
import com.cima.system.service.FornecedorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class FornecedorServiceImpl implements FornecedorService {

    private final FornecedorRepository fornecedorRepository;

    @Override
    public FornecedorResponse criar(FornecedorRequest request) {
        if (request.getNif() != null && fornecedorRepository.existsByNif(request.getNif())) {
            throw new BusinessException("Já existe um fornecedor com o NIF: " + request.getNif());
        }
        Fornecedor f = Fornecedor.builder()
                .nome(request.getNome())
                .nif(request.getNif())
                .telefone(request.getTelefone())
                .email(request.getEmail())
                .categoria(request.getCategoria())
                .endereco(request.getEndereco())
                .build();
        return toResponse(fornecedorRepository.save(f));
    }

    @Override
    public FornecedorResponse atualizar(Long id, FornecedorRequest request) {
        Fornecedor f = buscarEntidade(id);
        if (request.getNif() != null && !request.getNif().equals(f.getNif())
                && fornecedorRepository.existsByNif(request.getNif())) {
            throw new BusinessException("Já existe um fornecedor com o NIF: " + request.getNif());
        }
        f.setNome(request.getNome());
        f.setNif(request.getNif());
        f.setTelefone(request.getTelefone());
        f.setEmail(request.getEmail());
        f.setCategoria(request.getCategoria());
        f.setEndereco(request.getEndereco());
        return toResponse(fornecedorRepository.save(f));
    }

    @Override
    public void remover(Long id) {
        fornecedorRepository.delete(buscarEntidade(id));
    }

    @Override
    @Transactional(readOnly = true)
    public FornecedorResponse buscarPorId(Long id) {
        return toResponse(buscarEntidade(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<FornecedorResponse> listarTodos() {
        return fornecedorRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<FornecedorResponse> pesquisar(String nome) {
        return fornecedorRepository.findByNomeContainingIgnoreCase(nome)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<FornecedorResponse> listarPorCategoria(String categoria) {
        return fornecedorRepository.findByCategoria(categoria)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    private Fornecedor buscarEntidade(Long id) {
        return fornecedorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fornecedor não encontrado com ID: " + id));
    }

    private FornecedorResponse toResponse(Fornecedor f) {
        return FornecedorResponse.builder()
                .id(f.getId()).nome(f.getNome()).nif(f.getNif())
                .telefone(f.getTelefone()).email(f.getEmail())
                .categoria(f.getCategoria()).endereco(f.getEndereco())
                .build();
    }
}
