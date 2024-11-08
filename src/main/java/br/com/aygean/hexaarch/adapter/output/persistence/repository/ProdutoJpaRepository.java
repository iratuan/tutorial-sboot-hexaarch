package br.com.aygean.hexaarch.adapter.output.persistence.repository;


import br.com.aygean.hexaarch.adapter.output.persistence.entity.ProdutoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdutoJpaRepository extends JpaRepository<ProdutoEntity, Long> {
}
