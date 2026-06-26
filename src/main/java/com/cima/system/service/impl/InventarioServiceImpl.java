package com.cima.system.service.impl;

import com.cima.system.dto.request.InventarioRequest;
import com.cima.system.dto.response.InventarioResponse;
import com.cima.system.entity.Fornecedor;
import com.cima.system.entity.Inventario;
import com.cima.system.exception.ResourceNotFoundException;
import com.cima.system.repository.FornecedorRepository;
import com.cima.system.repository.InventarioRepository;
import com.cima.system.service.HistoricoService;
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
    private final FornecedorRepository fornecedorRepository;
    private final HistoricoService historicoService;

    @Override
    public InventarioResponse criar(InventarioRequest request) {
        Fornecedor fornecedor = resolverFornecedor(request.getFornecedorId());

        Inventario inv = Inventario.builder()
                .descricao(request.getDescricao())
                .descricao3(request.getDescricao3())
                .unidadeBase(request.getUnidadeBase())
                .precoVenda1(request.getPrecoVenda1())
                .precoVenda2(request.getPrecoVenda2())
                .precoVenda3(request.getPrecoVenda3())
                .preco(request.getPreco())
                .quantidade(request.getQuantidade())
                .fornecedor(fornecedor)
                .build();

        Inventario saved = inventarioRepository.save(inv);

        historicoService.registar(
                "Produto criado: [ID " + saved.getId() + "] " + saved.getDescricao()
                        + (fornecedor != null ? " | Fornecedor: " + fornecedor.getNome() : ""),
                null, saved.getId(), null
        );

        return toResponse(saved);
    }

    @Override
    public InventarioResponse atualizar(Long id, InventarioRequest request) {
        Inventario inv = buscarEntidade(id);
        Fornecedor fornecedor = resolverFornecedor(request.getFornecedorId());

        inv.setDescricao(request.getDescricao());
        inv.setDescricao3(request.getDescricao3());
        inv.setUnidadeBase(request.getUnidadeBase());
        inv.setPrecoVenda1(request.getPrecoVenda1());
        inv.setPrecoVenda2(request.getPrecoVenda2());
        inv.setPrecoVenda3(request.getPrecoVenda3());
        inv.setPreco(request.getPreco());
        inv.setQuantidade(request.getQuantidade());
        inv.setFornecedor(fornecedor);

        Inventario saved = inventarioRepository.save(inv);

        historicoService.registar(
                "Produto actualizado: [ID " + saved.getId() + "] " + saved.getDescricao()
                        + (fornecedor != null ? " | Fornecedor: " + fornecedor.getNome() : ""),
                null, id, null
        );

        return toResponse(saved);
    }

    @Override
    public void remover(Long id) {
        Inventario inv = buscarEntidade(id);

        historicoService.registar(
                "Produto eliminado: [ID " + id + "] " + inv.getDescricao(),
                null, id, null
        );

        inventarioRepository.delete(inv);
    }

    @Override
    @Transactional(readOnly = true)
    public InventarioResponse buscarPorId(Long id) {
        return toResponse(buscarEntidade(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventarioResponse> listarTodos() {
        return inventarioRepository.findAll()
                .stream().map(this::toResponse).collect(Collectors.toList());
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

    @Override
    @Transactional(readOnly = true)
    public List<InventarioResponse> listarPorFornecedor(Long fornecedorId) {
        return inventarioRepository.findByFornecedorId(fornecedorId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    // ─── helpers ──────────────────────────────────────────────────────────────

    public Inventario buscarEntidade(Long id) {
        return inventarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com ID: " + id));
    }

    private Fornecedor resolverFornecedor(Long fornecedorId) {
        if (fornecedorId == null) return null;
        return fornecedorRepository.findById(fornecedorId)
                .orElseThrow(() -> new ResourceNotFoundException("Fornecedor não encontrado com ID: " + fornecedorId));
    }

    private InventarioResponse toResponse(Inventario i) {
        return InventarioResponse.builder()
                .id(i.getId())
                .descricao(i.getDescricao())
                .descricao3(i.getDescricao3())
                .unidadeBase(i.getUnidadeBase())
                .precoVenda1(i.getPrecoVenda1())
                .precoVenda2(i.getPrecoVenda2())
                .precoVenda3(i.getPrecoVenda3())
                .preco(i.getPreco())
                .quantidade(i.getQuantidade())
                .fornecedorId(i.getFornecedor() != null ? i.getFornecedor().getId() : null)
                .fornecedorNome(i.getFornecedor() != null ? i.getFornecedor().getNome() : null)
                .build();
    }
}