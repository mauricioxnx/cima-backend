package com.cima.system.service.impl;

import com.cima.system.dto.request.InventarioRequest;
import com.cima.system.dto.response.InventarioResponse;
import com.cima.system.entity.Inventario;
import com.cima.system.exception.BusinessException;
import com.cima.system.exception.ResourceNotFoundException;
import com.cima.system.repository.InventarioRepository;
import com.cima.system.service.InventarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class InventarioServiceImpl implements InventarioService {

    private final InventarioRepository inventarioRepository;

    @Override
    public InventarioResponse criar(InventarioRequest request) {
        if (inventarioRepository.existsByCodigo(request.getCodigo())) {
            throw new BusinessException("Já existe um item com o código: " + request.getCodigo());
        }
        Inventario inv = Inventario.builder()
                .codigo(request.getCodigo())
                .descricao(request.getDescricao())
                .descricao3(request.getDescricao3())
                .unidadeBase(request.getUnidadeBase())
                .precoVenda1(request.getPrecoVenda1())
                .precoVenda2(request.getPrecoVenda2())
                .precoVenda3(request.getPrecoVenda3())
                .preco(request.getPreco())
                .quantidade(request.getQuantidade())
                .build();
        return toResponse(inventarioRepository.save(inv));
    }

    @Override
    public InventarioResponse atualizar(Long id, InventarioRequest request) {
        Inventario inv = buscarEntidade(id);
        if (!inv.getCodigo().equals(request.getCodigo()) && inventarioRepository.existsByCodigo(request.getCodigo())) {
            throw new BusinessException("Já existe um item com o código: " + request.getCodigo());
        }
        inv.setCodigo(request.getCodigo());
        inv.setDescricao(request.getDescricao());
        inv.setDescricao3(request.getDescricao3());
        inv.setUnidadeBase(request.getUnidadeBase());
        inv.setPrecoVenda1(request.getPrecoVenda1());
        inv.setPrecoVenda2(request.getPrecoVenda2());
        inv.setPrecoVenda3(request.getPrecoVenda3());
        inv.setPreco(request.getPreco());
        inv.setQuantidade(request.getQuantidade());
        return toResponse(inventarioRepository.save(inv));
    }

    @Override
    public void remover(Long id) {
        inventarioRepository.delete(buscarEntidade(id));
    }

    @Override
    @Transactional(readOnly = true)
    public InventarioResponse buscarPorId(Long id) {
        return toResponse(buscarEntidade(id));
    }

    @Override
    @Transactional(readOnly = true)
    public InventarioResponse buscarPorCodigo(String codigo) {
        return toResponse(inventarioRepository.findByCodigo(codigo)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com código: " + codigo)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventarioResponse> listarTodos() {
        return inventarioRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventarioResponse> pesquisar(String descricao) {
        return inventarioRepository.findByDescricaoContainingIgnoreCase(descricao)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventarioResponse> listarEstoqueBaixo(int minimo) {
        return inventarioRepository.findEstoqueBaixo(minimo)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventarioResponse> listarSemEstoque() {
        return inventarioRepository.findSemEstoque()
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public Inventario buscarEntidade(Long id) {
        return inventarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com ID: " + id));
    }

    private InventarioResponse toResponse(Inventario i) {
        return InventarioResponse.builder()
                .id(i.getId())
                .codigo(i.getCodigo())
                .descricao(i.getDescricao())
                .descricao3(i.getDescricao3())
                .unidadeBase(i.getUnidadeBase())
                .precoVenda1(i.getPrecoVenda1())
                .precoVenda2(i.getPrecoVenda2())
                .precoVenda3(i.getPrecoVenda3())
                .preco(i.getPreco())
                .quantidade(i.getQuantidade())
                .build();
    }
}
