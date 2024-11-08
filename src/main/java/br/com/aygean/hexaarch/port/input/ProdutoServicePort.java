package br.com.aygean.hexaarch.port.input;

import br.com.aygean.hexaarch.core.domain.Produto;
import java.util.List;
import java.util.Optional;

public interface ProdutoServicePort {
    Produto criarProduto(Produto produto);
    Optional<Produto> obterProdutoPorId(Long id);
    List<Produto> listarProdutos();
    Produto atualizarProduto(Produto produto);
    void deletarProduto(Long id);
}