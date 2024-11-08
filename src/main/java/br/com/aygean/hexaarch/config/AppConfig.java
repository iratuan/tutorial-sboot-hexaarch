package br.com.aygean.hexaarch.config;

import br.com.aygean.hexaarch.adapter.output.persistence.mapper.ProdutoMapper;
import br.com.aygean.hexaarch.adapter.output.persistence.repository.ProdutoJpaRepository;
import br.com.aygean.hexaarch.adapter.output.persistence.repository.ProdutoRepositoryImpl;
import br.com.aygean.hexaarch.core.usecase.ProdutoService;
import br.com.aygean.hexaarch.port.output.ProdutoRepositoryPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public ProdutoService produtoService(ProdutoRepositoryPort produtoRepositoryPort) {
        return new ProdutoService(produtoRepositoryPort);
    }

    @Bean
    public ProdutoRepositoryPort produtoRepositoryPort(ProdutoJpaRepository produtoRepository, ProdutoMapper produtoMapper) {
        return new ProdutoRepositoryImpl(produtoRepository, produtoMapper);
    }

    @Bean
    public ProdutoMapper produtoMapper() {
        return new ProdutoMapper();
    }
}
