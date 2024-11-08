package br.com.aygean.hexaarch.core.usecase;

import br.com.aygean.hexaarch.core.domain.Produto;
import br.com.aygean.hexaarch.port.output.ProdutoRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {

    private final ProdutoRepositoryPort produtoRepository;

    // Injeta a dependência ProdutoRepositoryPort, permitindo o uso do repositório para operações de persistência
    public ProdutoService(ProdutoRepositoryPort produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    // Método para criar um novo produto, salva o produto no repositório
    public Produto criarProduto(Produto produto) {
        // Aqui poderiam ser feitas validações ou cálculos de negócios antes de salvar o produto
        return produtoRepository.save(produto);
    }

    // Método para obter um produto pelo seu ID
    public Optional<Produto> obterProdutoPorId(Long id) {
        // Verifica no repositório se o produto existe com o ID fornecido
        return produtoRepository.findById(id);
    }

    // Método para listar todos os produtos
    public List<Produto> listarProdutos() {
        // Retorna todos os produtos no repositório
        return produtoRepository.findAll();
    }

    // Método para atualizar um produto existente
    public Produto atualizarProduto(Produto produto) {
        // Aqui poderiam ser feitas verificações, como se o produto existe antes de atualizar
        return produtoRepository.save(produto);
    }

    // Método para deletar um produto pelo seu ID
    public void deletarProduto(Long id) {
        // Remove o produto do repositório com base no ID
        produtoRepository.deleteById(id);
    }
}