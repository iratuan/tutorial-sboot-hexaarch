package br.com.aygean.hexaarch.adapter.output.persistence.repository;

import br.com.aygean.hexaarch.adapter.output.persistence.mapper.ProdutoMapper;
import br.com.aygean.hexaarch.core.domain.Produto;
import br.com.aygean.hexaarch.port.output.ProdutoRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementação de ProdutoRepositoryPort que utiliza ProdutoJpaRepository e ProdutoMapper
 * para realizar operações de persistência, convertendo entre as entidades de domínio (Produto)
 * e de persistência (ProdutoEntity).
 */
@Repository
public class ProdutoRepositoryImpl implements ProdutoRepositoryPort {

    private final ProdutoJpaRepository produtoJpaRepository;
    private final ProdutoMapper produtoMapper;

    /**
     * Construtor para injeção de dependências do repositório JPA e do mapeador de entidades.
     *
     * @param produtoJpaRepository Repositório JPA para operações de persistência.
     * @param produtoMapper Mapper para conversão entre Produto e ProdutoEntity.
     */
    public ProdutoRepositoryImpl(ProdutoJpaRepository produtoJpaRepository, ProdutoMapper produtoMapper) {
        this.produtoJpaRepository = produtoJpaRepository;
        this.produtoMapper = produtoMapper;
    }

    /**
     * Salva um produto no repositório, convertendo-o para ProdutoEntity.
     *
     * @param produto Produto a ser salvo.
     * @return Produto salvo após conversão de ProdutoEntity.
     */
    @Override
    public Produto save(Produto produto) {
        var produtoEntity = produtoMapper.toEntity(produto); // Converte Produto para ProdutoEntity
        var savedEntity = produtoJpaRepository.save(produtoEntity); // Salva a entidade no repositório JPA
        return produtoMapper.toDomain(savedEntity); // Converte a entidade salva de volta para Produto
    }

    /**
     * Busca um produto pelo ID, convertendo de ProdutoEntity para Produto.
     *
     * @param id ID do produto a ser encontrado.
     * @return Optional contendo o Produto, caso encontrado, ou Optional vazio.
     */
    @Override
    public Optional<Produto> findById(Long id) {
        return produtoJpaRepository.findById(id)
                .map(produtoMapper::toDomain); // Converte ProdutoEntity para Produto, se presente
    }

    /**
     * Recupera todos os produtos do repositório, convertendo cada ProdutoEntity para Produto.
     *
     * @return Lista de todos os produtos no repositório.
     */
    @Override
    public List<Produto> findAll() {
        return produtoJpaRepository.findAll().stream() // Busca todos os ProdutoEntity
                .map(produtoMapper::toDomain) // Converte cada ProdutoEntity para Produto
                .collect(Collectors.toList());
    }

    /**
     * Exclui um produto do repositório pelo ID.
     *
     * @param id ID do produto a ser excluído.
     */
    @Override
    public void deleteById(Long id) {
        produtoJpaRepository.deleteById(id); // Exclui o ProdutoEntity pelo ID
    }
}
