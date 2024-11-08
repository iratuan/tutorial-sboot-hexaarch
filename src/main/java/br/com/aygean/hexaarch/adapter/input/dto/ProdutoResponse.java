package br.com.aygean.hexaarch.adapter.input.dto;

import java.math.BigDecimal;

public record ProdutoResponse(Long id, String nome, BigDecimal preco) {
}
