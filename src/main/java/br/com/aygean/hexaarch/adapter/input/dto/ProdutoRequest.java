package br.com.aygean.hexaarch.adapter.input.dto;

import java.math.BigDecimal;

public record ProdutoRequest(String nome, BigDecimal preco) {
}
