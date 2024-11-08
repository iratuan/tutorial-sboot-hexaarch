package br.com.aygean.hexaarch.port.output;

import br.com.aygean.hexaarch.core.domain.Produto;
import java.util.List;
import java.util.Optional;

public interface ProdutoRepositoryPort {
    Produto save(Produto produto);
    Optional<Produto> findById(Long id);
    List<Produto> findAll();
    void deleteById(Long id);
}
