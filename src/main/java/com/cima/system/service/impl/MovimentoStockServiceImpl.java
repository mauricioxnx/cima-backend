package com.cima.system.service.impl;

import com.cima.system.dto.request.MovimentoStockRequest;
import com.cima.system.dto.response.MovimentoStockResponse;
import com.cima.system.entity.Inventario;
import com.cima.system.entity.MovimentoStock;
import com.cima.system.enums.TipoMovimento;
import com.cima.system.exception.BusinessException;
import com.cima.system.exception.ResourceNotFoundException;
import com.cima.system.repository.MovimentoStockRepository;
import com.cima.system.service.HistoricoService;
import com.cima.system.service.MovimentoStockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MovimentoStockServiceImpl implements MovimentoStockService {

    private final MovimentoStockRepository movimentoRepository;
    private final InventarioServiceImpl inventarioService;
    private final HistoricoService historicoService; // ✅

    @Override
    public MovimentoStockResponse registar(MovimentoStockRequest request) {
        Inventario inv = inventarioService.buscarEntidade(request.getInventarioId());

        if (request.getTipoMovimento() == TipoMovimento.ENTRADA) {
            inv.setQuantidade(inv.getQuantidade() + request.getQuantidade());
        } else if (request.getTipoMovimento() == TipoMovimento.SAIDA) {
            if (inv.getQuantidade() < request.getQuantidade()) {
                throw new BusinessException("Stock insuficiente. Disponível: " + inv.getQuantidade());
            }
            inv.setQuantidade(inv.getQuantidade() - request.getQuantidade());
        }

        MovimentoStock mov = MovimentoStock.builder()
                .tipoMovimento(request.getTipoMovimento())
                .documentoRef(request.getDocumentoRef())
                .preco(request.getPreco())
                .quantidade(request.getQuantidade())
                .inventario(inv)
                .build();

        MovimentoStock saved = movimentoRepository.save(mov);

        // ✅
        historicoService.registar(
                "Movimento de stock: " + request.getTipoMovimento() +
                        " | Produto: [" + inv.getCodigo() + "] " + inv.getDescricao() +
                        " | Qtd: " + request.getQuantidade(),
                null,
                inv.getId(),
                null
        );

        return toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public MovimentoStockResponse buscarPorId(Long id) {
        return toResponse(movimentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movimento não encontrado com ID: " + id)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovimentoStockResponse> listarTodos() {
        return movimentoRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovimentoStockResponse> listarPorInventario(Long inventarioId) {
        return movimentoRepository.findByInventarioIdOrderByDataDesc(inventarioId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovimentoStockResponse> listarPorTipo(TipoMovimento tipo) {
        return movimentoRepository.findByTipoMovimento(tipo)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovimentoStockResponse> listarPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        return movimentoRepository.findByDataMovimentoBetween(inicio, fim)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    private MovimentoStockResponse toResponse(MovimentoStock m) {
        return MovimentoStockResponse.builder()
                .idMovimento(m.getIdMovimento())
                .tipoMovimento(m.getTipoMovimento())
                .dataMovimento(m.getDataMovimento())
                .documentoRef(m.getDocumentoRef())
                .preco(m.getPreco())
                .quantidade(m.getQuantidade())
                .inventarioId(m.getInventario().getId())
                .inventarioCodigo(m.getInventario().getCodigo())
                .inventarioDescricao(m.getInventario().getDescricao())
                .build();
    }
}