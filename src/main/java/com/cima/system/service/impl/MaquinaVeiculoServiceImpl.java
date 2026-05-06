package com.cima.system.service.impl;

import com.cima.system.dto.request.MaquinaVeiculoRequest;
import com.cima.system.dto.response.MaquinaVeiculoResponse;
import com.cima.system.entity.Inventario;
import com.cima.system.entity.MaquinaVeiculo;
import com.cima.system.exception.BusinessException;
import com.cima.system.exception.ResourceNotFoundException;
import com.cima.system.repository.InventarioRepository;
import com.cima.system.repository.MaquinaVeiculoRepository;
import com.cima.system.service.MaquinaVeiculoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MaquinaVeiculoServiceImpl implements MaquinaVeiculoService {

    private final MaquinaVeiculoRepository repository;
    private final InventarioRepository inventarioRepository;

    @Override
    public MaquinaVeiculoResponse criar(MaquinaVeiculoRequest request) {
        if (request.getMatriculaNSerie() != null && repository.existsByMatriculaNSerie(request.getMatriculaNSerie())) {
            throw new BusinessException("Já existe uma máquina/veículo com a matrícula: " + request.getMatriculaNSerie());
        }
        MaquinaVeiculo mv = buildFromRequest(new MaquinaVeiculo(), request);
        return toResponse(repository.save(mv));
    }

    @Override
    public MaquinaVeiculoResponse atualizar(Long id, MaquinaVeiculoRequest request) {
        MaquinaVeiculo mv = buscarEntidade(id);
        buildFromRequest(mv, request);
        return toResponse(repository.save(mv));
    }

    @Override
    public void remover(Long id) {
        repository.delete(buscarEntidade(id));
    }

    @Override
    @Transactional(readOnly = true)
    public MaquinaVeiculoResponse buscarPorId(Long id) {
        return toResponse(buscarEntidade(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<MaquinaVeiculoResponse> listarTodos() {
        return repository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MaquinaVeiculoResponse> listarPorEstado(String estado) {
        return repository.findByEstado(estado).stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MaquinaVeiculoResponse> pesquisar(String modelo) {
        return repository.findByModeloContainingIgnoreCase(modelo).stream().map(this::toResponse).collect(Collectors.toList());
    }

    private MaquinaVeiculo buildFromRequest(MaquinaVeiculo mv, MaquinaVeiculoRequest r) {
        mv.setModelo(r.getModelo());
        mv.setDataAquisicao(r.getDataAquisicao());
        mv.setEstado(r.getEstado());
        mv.setMatriculaNSerie(r.getMatriculaNSerie());
        mv.setTipo(r.getTipo());
        if (r.getInventarioId() != null) {
            Inventario inv = inventarioRepository.findById(r.getInventarioId())
                    .orElseThrow(() -> new ResourceNotFoundException("Inventário não encontrado: " + r.getInventarioId()));
            mv.setInventario(inv);
        }
        return mv;
    }

    private MaquinaVeiculo buscarEntidade(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Máquina/Veículo não encontrado com ID: " + id));
    }

    private MaquinaVeiculoResponse toResponse(MaquinaVeiculo mv) {
        return MaquinaVeiculoResponse.builder()
                .id(mv.getId()).modelo(mv.getModelo())
                .dataAquisicao(mv.getDataAquisicao()).estado(mv.getEstado())
                .matriculaNSerie(mv.getMatriculaNSerie()).tipo(mv.getTipo())
                .inventarioId(mv.getInventario() != null ? mv.getInventario().getId() : null)
                .build();
    }
}
