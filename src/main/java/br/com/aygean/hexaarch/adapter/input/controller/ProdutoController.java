package br.com.aygean.hexaarch.adapter.input.controller;

import br.com.aygean.hexaarch.core.domain.Produto;
import br.com.aygean.hexaarch.core.usecase.ProdutoService;
import br.com.aygean.hexaarch.adapter.input.dto.ProdutoRequest;
import br.com.aygean.hexaarch.adapter.input.dto.ProdutoResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    /**
     * Endpoint para criar um novo produto.
     *
     * @param produtoRequest Dados do produto a ser criado.
     * @return ResponseEntity contendo o ProdutoResponse e o status HTTP.
     */
    @PostMapping
    public ResponseEntity<ProdutoResponse> criarProduto(@RequestBody ProdutoRequest produtoRequest) {
        Produto produto = new Produto();
        produto.setNome(produtoRequest.nome());
        produto.setPreco(produtoRequest.preco());

        Produto produtoCriado = produtoService.criarProduto(produto);
        ProdutoResponse response = new ProdutoResponse(produtoCriado.getId(), produtoCriado.getNome(), produtoCriado.getPreco());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Endpoint para obter um produto pelo ID.
     *
     * @param id ID do produto.
     * @return ResponseEntity com ProdutoResponse e status HTTP 200, ou 404 caso o produto não seja encontrado.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponse> obterProduto(@PathVariable Long id) {
        Optional<Produto> produtoOpt = produtoService.obterProdutoPorId(id);
        return produtoOpt.map(produto -> ResponseEntity.ok(new ProdutoResponse(produto.getId(), produto.getNome(), produto.getPreco())))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    /**
     * Endpoint para listar todos os produtos.
     *
     * @return ResponseEntity com a lista de ProdutoResponse e status HTTP 200.
     */
    @GetMapping
    public ResponseEntity<List<ProdutoResponse>> listarProdutos() {
        List<ProdutoResponse> produtos = produtoService.listarProdutos().stream()
                .map(produto -> new ProdutoResponse(produto.getId(), produto.getNome(), produto.getPreco()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(produtos);
    }

    /**
     * Endpoint para atualizar um produto pelo ID.
     *
     * @param id ID do produto a ser atualizado.
     * @param produtoRequest Dados do produto a serem atualizados.
     * @return ResponseEntity com ProdutoResponse e status HTTP 200.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProdutoResponse> atualizarProduto(@PathVariable Long id, @RequestBody ProdutoRequest produtoRequest) {
        Produto produto = new Produto();
        produto.setId(id);
        produto.setNome(produtoRequest.nome());
        produto.setPreco(produtoRequest.preco());

        Produto produtoAtualizado = produtoService.atualizarProduto(produto);
        ProdutoResponse response = new ProdutoResponse(produtoAtualizado.getId(), produtoAtualizado.getNome(), produtoAtualizado.getPreco());
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint para deletar um produto pelo ID.
     *
     * @param id ID do produto a ser deletado.
     * @return ResponseEntity com status HTTP 204.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarProduto(@PathVariable Long id) {
        produtoService.deletarProduto(id);
        return ResponseEntity.noContent().build();
    }
}