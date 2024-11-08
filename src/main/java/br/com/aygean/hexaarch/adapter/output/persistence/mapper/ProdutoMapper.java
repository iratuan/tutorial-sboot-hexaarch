package br.com.aygean.hexaarch.adapter.output.persistence.mapper;

import br.com.aygean.hexaarch.adapter.output.persistence.entity.ProdutoEntity;
import br.com.aygean.hexaarch.core.domain.Produto;

public class ProdutoMapper {

    public static ProdutoEntity toEntity(Produto produto) {
        return new ProdutoEntity(produto.getId(), produto.getNome(), produto.getPreco());
    }

    public Produto toDomain(ProdutoEntity produtoEntity) {
        return new Produto(produtoEntity.getId(), produtoEntity.getNome(), produtoEntity.getPreco());
    }
}