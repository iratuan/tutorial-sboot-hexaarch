package br.com.aygean.hexaarch.adapter.output.persistence.repository;

import br.com.aygean.hexaarch.adapter.output.persistence.mapper.ProdutoMapper;
import br.com.aygean.hexaarch.core.domain.Produto;
import br.com.aygean.hexaarch.port.output.ProdutoRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class ProdutoRepositoryImpl implements ProdutoRepositoryPort {

    private final ProdutoJpaRepository produtoJpaRepository;
    private final ProdutoMapper produtoMapper;

    public ProdutoRepositoryImpl(ProdutoJpaRepository produtoJpaRepository, ProdutoMapper produtoMapper) {
        this.produtoJpaRepository = produtoJpaRepository;
        this.produtoMapper = produtoMapper;
    }

    @Override
    public Produto save(Produto produto) {
        var produtoEntity = produtoMapper.toEntity(produto);
        var savedEntity = produtoJpaRepository.save(produtoEntity);
        return produtoMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Produto> findById(Long id) {
        return produtoJpaRepository.findById(id)
                .map(produtoMapper::toDomain);
    }

    @Override
    public List<Produto> findAll() {
        return produtoJpaRepository.findAll().stream()
                .map(produtoMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        produtoJpaRepository.deleteById(id);
    }
}
