![Arquitetura Limpa](/images/f01_capa.png "Capa")

# Tutorial de Arquitetura Hexagonal com Spring Boot - CRUD de Produto

- Autor: Iratuã Júnior
- Data: 08/11/2024
- Versão do springboot: 3.3.5
- Versão jdk: 17
- [Respositorio no github](git remote add origin https://github.com/iratuan/tutorial-sboot-hexaarch.git)

## Introdução

### 1. **Introdução ao Projeto e Arquitetura Hexagonal**
A **arquitetura hexagonal**, também conhecida como **Portas e Adaptadores**, é um estilo arquitetural que facilita a separação de responsabilidades e promove um alto nível de desacoplamento entre as camadas de um sistema. Concebida por **Alistair Cockburn**, essa abordagem visa manter a lógica de negócios independente de detalhes técnicos, como frameworks, bancos de dados, ou APIs externas, permitindo que a aplicação seja adaptável e extensível.

### Propósito

A arquitetura hexagonal organiza o sistema em duas principais divisões: o **núcleo da aplicação** e o **mundo externo**. No centro, temos a lógica de negócios, enquanto o mundo externo é composto por **adaptadores** que conectam a aplicação ao banco de dados, APIs, interfaces de usuário, entre outros.

O núcleo da aplicação se comunica com o mundo externo através de **portas** (interfaces) e **adaptadores** (implementações dessas portas). Essa divisão em camadas oferece:

1. **Separação de responsabilidades**: Cada camada é responsável por uma função específica, como lógica de negócios, persistência, e comunicação com o usuário.
2. **Desacoplamento entre camadas**: O núcleo de negócio não depende de frameworks ou tecnologias específicas. Ele interage com o mundo externo através de portas, que podem ser facilmente substituídas por adaptadores diferentes.

### Benefícios

1. **Flexibilidade para mudanças**: Adaptadores podem ser trocados ou modificados sem afetar a lógica de negócios. Por exemplo, trocar de banco de dados ou framework de interface pode ser feito com mínima ou nenhuma alteração no núcleo da aplicação.
2. **Testabilidade**: A lógica de negócios, estando isolada, pode ser testada sem a necessidade de configurar um banco de dados ou API externa, melhorando a qualidade do software.
3. **Facilidade de manutenção**: Como as responsabilidades são bem distribuídas, o código tende a ser mais modular e fácil de manter e evoluir.

### Objetivos do Tutorial

Neste tutorial, vamos desenvolver uma API RESTful para gerenciar produtos em um sistema de e-commerce usando a **arquitetura hexagonal**. O objetivo principal é mostrar como implementar um CRUD (Create, Read, Update, Delete) para a entidade `Produto` seguindo os princípios da arquitetura hexagonal, que ajuda a organizar e desacoplar as camadas da aplicação.

#### O que Será Implementado

Nosso projeto será composto por uma estrutura modular dividida em camadas bem definidas, cada uma com responsabilidades específicas. A ideia é demonstrar como a arquitetura hexagonal permite que a lógica de negócios seja independente das particularidades de infraestrutura, como frameworks e tecnologias de persistência.

#### Funcionalidades

O CRUD da entidade `Produto` será implementado com as seguintes funcionalidades:

1. **Criar Produto**: Endpoint para adicionar um novo produto ao sistema, recebendo os dados do produto como nome e preço.
2. **Consultar Produto por ID**: Endpoint para consultar detalhes de um produto específico.
3. **Listar Produtos**: Endpoint para listar todos os produtos cadastrados no sistema.
4. **Atualizar Produto**: Endpoint para atualizar os dados de um produto existente.
5. **Excluir Produto**: Endpoint para remover um produto do sistema.

#### Estrutura do Projeto

O projeto será estruturado de forma que:

- A camada de **core** contenha a lógica de negócios e o domínio da aplicação, sem dependências externas.
- A camada de **input** (entrada) lidere a comunicação com o usuário através de endpoints REST, utilizando DTOs para o transporte de dados.
- A camada de **output** (saída) cuide da persistência dos dados e faça a interação com o banco de dados.

### Resultado Final

Ao final deste tutorial, teremos uma API completa para gerenciar produtos, implementada de forma modular e extensível, onde cada camada estará desacoplada das demais. O código será organizado para facilitar a adição de novas funcionalidades e adaptações tecnológicas, aproveitando os benefícios da arquitetura hexagonal.

---

### 2. **Configuração do Projeto e Dependências**
- **Spring Initializr**: Instruções para configurar um projeto no Spring Initializr com as dependências necessárias (Spring Web, Spring Data JPA, Lombok, etc.).

Configure as seguintes dependências:
- **Spring Data JPA**: para persistência de dados no banco.
- **Spring Web**: para criar os endpoints REST.
- **PostgreSQL**: driver para se conectar ao banco de dados.
- **Lombok**: para eliminar código repetitivo.
- **Spring Boot DevTools**: para recarregamento automático no desenvolvimento.

Abaixo, um exemplo do bloco de dependências do seu arquivo `pom.xml` do seu projeto.

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-devtools</artifactId>
        <scope>runtime</scope>
        <optional>true</optional>
    </dependency>
    <dependency>
        <groupId>org.postgresql</groupId>
        <artifactId>postgresql</artifactId>
        <scope>runtime</scope>
    </dependency>
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

- **Estrutura de Pastas**: Explique como organizar as pastas do projeto, mantendo a divisão entre `core`, `adapter`, `port`, `config`, e outros diretórios.

Abaixo, um resumo dos pacotes que iremos utilizar e sua macrofunção dentro de uma arquitetura hexagonal
Organizaremos o projeto em pacotes para seguir a arquitetura hexagonal:

| **Pacote**                   | **Propósito**                                                                                                                         |
|------------------------------|---------------------------------------------------------------------------------------------------------------------------------------|
| **core**                     | Contém a lógica de negócios e as regras principais da aplicação.                                                                      |
| **core.domain**              | Define a entidade de domínio `Produto`, encapsulando a lógica de negócios.                                                            |
| **core.usecase**             | Contém os casos de uso que representam a lógica de negócios para operações como criação, atualização e exclusão de produtos.          |
| **port.input**               | Define as interfaces para entrada de dados na aplicação (ex.: operações de CRUD para Produto).                                        |
| **port.output**              | Define as interfaces para saída de dados (ex.: persistência de dados em um banco PostgreSQL).                                         |
| **adapter.input.controller** | Implementa as interfaces de entrada para processar solicitações da API (ex.: controllers REST para expor os endpoints de CRUD).       |
| **adapter.output.repository**| Implementa as interfaces de saída que se comunicam com o banco de dados, realizando operações de CRUD.                                |
| **config**                   | Contém classes de configuração da aplicação, como banco de dados, ajudando a manter o código organizado e centralizado.               |

---

### 3. **Criação da Entidade do Domínio e UseCase do domínio `Produto`**
#### Classe `Produto`: Entidade de Domínio Independente de Frameworks

A classe `Produto` representa a nossa entidade central no contexto de um sistema de e-commerce. Como estamos seguindo os princípios da **arquitetura hexagonal**, a entidade de domínio `Produto` é implementada na **camada de núcleo (core)** do projeto. Esse núcleo é independente de frameworks externos, o que permite que a lógica de negócios seja reutilizável e adaptável a diferentes tecnologias e implementações sem precisar modificar a própria lógica de domínio.

#### Definindo Atributos e Construtores

A classe `Produto` contém os atributos essenciais para definir um produto: `id`, `nome` e `preco`. Cada atributo representa uma característica fundamental de um produto, permitindo que o sistema armazene e gerencie informações básicas como a identificação, descrição e valor de cada item.

Para facilitar a criação e manipulação de instâncias de `Produto`, a classe inclui construtores que permitem:

1. **Construtor Padrão**: Necessário para frameworks e ferramentas de serialização.
2. **Construtor com Argumentos**: Permite a criação de uma instância de `Produto` com todos os seus atributos definidos, facilitando a inicialização direta e a legibilidade do código.

A ausência de dependências externas, como anotações de frameworks, garante que a entidade `Produto` permaneça completamente independente, respeitando o princípio de **desacoplamento** da arquitetura hexagonal.

#### Código Completo da Classe `Produto`

Abaixo está o código da classe `Produto` com todos os atributos, construtores e métodos de acesso necessários:

```java
package br.com.aygean.hexaarch.core.domain;

import java.math.BigDecimal;

public class Produto {

    private Long id;
    private String nome;
    private BigDecimal preco;

    // Construtores
    public Produto() {
    }

    public Produto(Long id, String nome, BigDecimal preco) {
        this.id = id;
        this.nome = nome;
        this.preco = preco;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }
}

```

#### Explicação dos Componentes da Classe

- **Atributos (`id`, `nome`, `preco`)**: Definem as propriedades principais da entidade `Produto`, necessárias para identificar e descrever o produto.
- **Construtor Padrão**: Necessário para o uso de frameworks e bibliotecas de serialização e desserialização.
- **Construtor Completo**: Facilita a criação de uma instância `Produto` com valores pré-definidos para todos os atributos, promovendo praticidade no código.
- **Getters e Setters**: Métodos de acesso que seguem o princípio de encapsulamento, permitindo acessar e modificar os valores dos atributos de forma controlada.

#### Resumo

A classe `Produto` está configurada para ser uma entidade de domínio que permanece isolada de detalhes de implementação, como frameworks de persistência ou dependências externas. Esse design garante que a lógica de negócio permaneça coesa e flexível, sendo o primeiro passo para implementar o CRUD seguindo os princípios da arquitetura hexagonal.

---

### 4. **Criação das Interfaces de Portas**

#### ProdutoRepositoryPort

A **interface `ProdutoRepositoryPort`** é uma das principais interfaces no design de uma aplicação que adota a **arquitetura hexagonal**. Ela define o **contrato** para as operações de persistência da entidade `Produto`, o que permite que a camada de domínio não dependa diretamente da implementação específica de um repositório.

Essa interface, conhecida como um **"port"** (ou porta), representa o ponto de entrada para as operações de persistência e possibilita o uso de diferentes tecnologias e abordagens para acessar e manipular os dados de `Produto`. Isso significa que podemos mudar a implementação do repositório (por exemplo, de um banco de dados relacional para um banco NoSQL) sem modificar a lógica de negócio.

#### Código da Interface `ProdutoRepositoryPort`

```java
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
```

#### Explicação dos Métodos:

- **`save(Produto produto)`**: Salva ou atualiza um produto no repositório.
- **`findById(Long id)`**: Recupera um produto específico com base no ID fornecido.
- **`findAll()`**: Retorna todos os produtos disponíveis no repositório.
- **`deleteById(Long id)`**: Remove um produto com o ID especificado.

#### ProdutoServicePort

A **interface `ProdutoServicePort`** é outro port importante na nossa arquitetura, servindo como um contrato que define os métodos de serviço que a camada de controle (controller) deve utilizar para interagir com a entidade `Produto`. Essa interface facilita o **desacoplamento** entre a camada de serviço e o controlador, tornando a aplicação mais flexível para modificações e implementações de novos serviços.

Através do `ProdutoServicePort`, a camada de apresentação (input) acessa as operações CRUD definidas para `Produto`, sem precisar conhecer os detalhes de implementação da camada de persistência. Isso torna a aplicação mais modular e alinhada aos princípios da **arquitetura hexagonal**, permitindo a substituição de serviços ou modificações na lógica de negócio sem impacto na camada de apresentação.

#### Código da Interface `ProdutoServicePort`

```java
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
```

#### Explicação dos Métodos:

- **`criarProduto(Produto produto)`**: Cria um novo produto com base nos dados fornecidos.
- **`obterProdutoPorId(Long id)`**: Retorna um produto específico com base no ID.
- **`listarProdutos()`**: Retorna uma lista de todos os produtos.
- **`atualizarProduto(Produto produto)`**: Atualiza os dados de um produto existente.
- **`deletarProduto(Long id)`**: Exclui o produto com o ID fornecido.

#### Resumo

As interfaces `ProdutoRepositoryPort` e `ProdutoServicePort` são fundamentais para a arquitetura hexagonal do projeto. Elas estabelecem contratos claros para a comunicação entre camadas, garantindo que a lógica de negócio permaneça independente dos detalhes de implementação da camada de persistência e do controlador. Com esses ports, conseguimos uma aplicação modular, flexível e mais fácil de manter, além de facilitar mudanças tecnológicas sem impacto no domínio da aplicação.

---

### 5. **Camada de Adaptação (Adapters)**
### Entidade de Persistência (`ProdutoEntity`)

A classe `ProdutoEntity` é uma **representação da entidade Produto** que será utilizada exclusivamente para a persistência de dados. Ela é criada no pacote de persistência e configurada com as **anotações JPA** para mapeamento com o banco de dados. Essa entidade herda a estrutura básica de atributos de `Produto` e adiciona o comportamento específico necessário para interagir com o banco de dados.

#### Implementação da Classe `ProdutoEntity`

```java
package br.com.aygean.hexaarch.adapter.output.persistence.entity;

import br.com.aygean.hexaarch.core.domain.Produto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProdutoEntity extends Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private BigDecimal preco;
}
```

#### Explicação do Código

- **Anotação `@Entity`**: Indica que esta classe é uma entidade gerenciada pelo JPA e será mapeada para uma tabela no banco de dados.
- **Anotação `@Id` e `@GeneratedValue`**: Define `id` como a chave primária da entidade e configura a geração automática de valores para esse campo (estratégia `IDENTITY`).
- **Herança de `Produto`**: `ProdutoEntity` herda os atributos de `Produto` para aproveitar a estrutura de domínio, mas ainda mantém suas anotações e lógica de mapeamento na própria classe para que a camada de domínio permaneça desacoplada.

#### Repositório JPA (`ProdutoJpaRepository`)

Para realizar operações no banco de dados com o Spring Data JPA, criamos a interface `ProdutoJpaRepository`, que estende `JpaRepository` e herda os métodos padrão para operações CRUD. Ao estender `JpaRepository`, o Spring Data JPA gerencia as operações de persistência automaticamente.

#### Implementação do `ProdutoJpaRepository`

```java
package br.com.aygean.hexaarch.adapter.output.persistence.repository;


import br.com.aygean.hexaarch.adapter.output.persistence.entity.ProdutoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdutoJpaRepository extends JpaRepository<ProdutoEntity, Long> {
}
```

#### Explicação do Código

- **Extensão de `JpaRepository`**: `ProdutoJpaRepository` herda os métodos CRUD padrão, como `save`, `findById`, `findAll`, e `deleteById`, que permitem realizar operações no banco de dados sem a necessidade de implementação adicional.
- **Anotação `@Repository`**: Indica que esta interface faz parte da camada de persistência, permitindo que o Spring a gerencie como um bean de repositório.

#### Implementação do Repositório (`ProdutoRepositoryImpl`)

A classe `ProdutoRepositoryImpl` implementa a interface `ProdutoRepositoryPort`, que define as operações do repositório de domínio para `Produto`. Esta classe atua como um adaptador, utilizando o `ProdutoJpaRepository` para realizar as operações de persistência e o `ProdutoMapper` para converter entre `Produto` (domínio) e `ProdutoEntity` (persistência).

#### Implementação do `ProdutoRepositoryImpl`

```java
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

```

#### Explicação do Código

- **`ProdutoRepositoryImpl` implementa `ProdutoRepositoryPort`**: `ProdutoRepositoryImpl` segue o contrato definido em `ProdutoRepositoryPort`, atendendo aos requisitos de persistência definidos pela camada de domínio.
- **Uso do `ProdutoJpaRepository`**: `ProdutoJpaRepository` é utilizado para realizar as operações de persistência reais no banco de dados, como `save`, `findById`, `findAll`, e `deleteById`.
- **Conversão com `ProdutoMapper`**:
  - O método `save` converte o objeto `Produto` em `ProdutoEntity` antes de persistir e converte o `ProdutoEntity` salvo de volta para `Produto`.
  - O método `findById` usa o mapper para converter a entidade encontrada em `Produto`, retornando um `Optional<Produto>`.
  - O método `findAll` converte a lista de `ProdutoEntity` em `Produto`, usando o `ProdutoMapper` para cada item e retornando a lista como uma coleção de `Produto`.
- **Desacoplamento**: `ProdutoRepositoryImpl` atua como um adaptador que traduz as operações de persistência para o domínio, mantendo o código de domínio desacoplado das implementações de infraestrutura.

#### Resumo

- **`ProdutoEntity`**: Representa a entidade de persistência configurada com anotações JPA para mapeamento com o banco de dados.
- **`ProdutoJpaRepository`**: Interface que estende `JpaRepository` para obter métodos CRUD básicos do Spring Data JPA.
- **`ProdutoRepositoryImpl`**: Implementa `ProdutoRepositoryPort`, usando `ProdutoJpaRepository` para realizar as operações de persistência e `ProdutoMapper` para conversão entre `Produto` e `ProdutoEntity`.

Esse conjunto permite que a camada de domínio manipule objetos `Produto` independentemente da infraestrutura de persistência, mantendo uma arquitetura limpa e flexível.

---

### 6. **Mapeamento de Entidades**
### Necessidade de um `ProdutoMapper`

Em uma arquitetura hexagonal, é importante manter uma clara separação entre as **camadas de domínio** e **persistência**. No contexto de nosso projeto, `Produto` representa o objeto de domínio, utilizado para manipular informações no nível de negócio, enquanto `ProdutoEntity` é a entidade configurada com anotações JPA para interação com o banco de dados.

Como essas classes têm finalidades diferentes, precisamos de uma **ferramenta para converter** entre elas, garantindo que o código de domínio não dependa diretamente da infraestrutura de persistência.

#### Vantagens do `ProdutoMapper`:

1. **Desacoplamento**: `ProdutoMapper` permite que o código de domínio e o código de persistência permaneçam independentes. Com isso, alterações na estrutura de `ProdutoEntity` (camada de persistência) não afetam `Produto` (camada de domínio) e vice-versa.
2. **Flexibilidade**: Facilita a adaptação para diferentes bancos de dados ou APIs. Podemos trocar `ProdutoEntity` ou modificar o esquema do banco sem impacto direto na camada de domínio.
3. **Manutenção**: Concentra a lógica de conversão em um único local, o que facilita ajustes futuros nas conversões e aumenta a clareza do código.

### Implementação do `ProdutoMapper`

Abaixo está o código para a implementação do `ProdutoMapper`, que define os métodos `toEntity` e `toDomain` para converter entre `Produto` e `ProdutoEntity`.

```java
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
```

### Explicação do Código

1. **Método `toEntity`**:
  - Converte um objeto `Produto` em `ProdutoEntity` para persistência no banco de dados.
  - Recebe um `Produto` como parâmetro e utiliza seus dados para instanciar um novo `ProdutoEntity` com o mesmo ID, nome e preço.

2. **Método `toDomain`**:
  - Converte um objeto `ProdutoEntity` em `Produto`, permitindo que a camada de domínio trabalhe com um objeto independente da infraestrutura de persistência.
  - Recebe um `ProdutoEntity` como parâmetro e cria uma nova instância de `Produto` com os dados de ID, nome e preço.

### Resumo

Com o `ProdutoMapper`, conseguimos separar as responsabilidades das camadas, deixando a persistência e o domínio desacoplados. Isso facilita a manutenção, traz flexibilidade para alterações na estrutura de dados e permite que o código de domínio se concentre nas regras de negócio, sem precisar lidar com detalhes de persistência.
---

### 7. **Implementação da Camada de Serviço**

Na arquitetura hexagonal, a camada de serviço é onde centralizamos a **lógica de negócios** da aplicação. Em nosso projeto, a classe `ProdutoService` representa essa camada para o caso de uso de produtos, definindo os métodos principais para manipulação de produtos, como criação, consulta, atualização e exclusão.

O `ProdutoService` utiliza a interface `ProdutoRepositoryPort` para realizar operações de persistência de dados, o que permite que ele permaneça **independente de detalhes de infraestrutura** (como JPA ou outro framework de persistência). Dessa forma, o `ProdutoService` pode focar exclusivamente na lógica de negócio, enquanto o `ProdutoRepositoryPort` cuida da persistência.

### Implementação da Classe `ProdutoService`

Abaixo está o código para a implementação da classe `ProdutoService`, onde a lógica de negócios é definida para as operações CRUD:

```java
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
```

#### Explicação dos Métodos

1. **Método `criarProduto`**:
  - Recebe um objeto `Produto` e usa o `produtoRepository` para salvar o produto no banco de dados.
  - Esse método pode conter lógica de negócios, como validações ou manipulação de dados, antes de persistir o produto.
  - Retorna o produto recém-criado, facilitando a verificação do sucesso da operação.

2. **Método `obterProdutoPorId`**:
  - Realiza uma consulta pelo ID do produto no repositório, retornando um `Optional<Produto>` que indica se o produto foi encontrado ou não.
  - Isso ajuda a gerenciar respostas nulas de forma segura, evitando exceções de valor inexistente.

3. **Método `listarProdutos`**:
  - Recupera e retorna todos os produtos armazenados no banco de dados.
  - Esse método pode ser usado para exibir a lista completa de produtos ao usuário, sem a necessidade de filtragem.

4. **Método `atualizarProduto`**:
  - Recebe um `Produto` e o salva no repositório, substituindo o existente (caso o ID seja o mesmo).
  - Pode incluir validações e verificações, como confirmar se o produto existe antes de atualizar.
  - Retorna o produto atualizado para que a aplicação possa usá-lo ou exibi-lo.

5. **Método `deletarProduto`**:
  - Recebe o ID de um produto e usa o repositório para deletá-lo do banco de dados.
  - Centralizar a lógica de exclusão permite uma eventual adição de verificações, como impedir a exclusão de produtos em pedidos ativos.

#### Centralização da Lógica de Negócio

A classe `ProdutoService` centraliza toda a lógica de negócios referente a produtos. Isso é fundamental para garantir que a camada de aplicação (como controladores) permaneça simples e se concentre apenas em receber e retornar dados. Esse isolamento da lógica de negócios torna o sistema:

- **Fácil de manter**: As regras de negócio estão todas em um único lugar.
- **Fácil de modificar e testar**: Alterações e testes de lógica de negócio podem ser feitos na camada de serviço, sem impacto direto nas demais camadas.
- **Flexível e extensível**: Como o `ProdutoService` se comunica apenas com a `ProdutoRepositoryPort`, ele é independente de detalhes de implementação do repositório, facilitando futuras substituições ou mudanças.

#### Resumo

A classe `ProdutoService` é um componente essencial da nossa aplicação, onde toda a lógica de manipulação de produtos é implementada. Utilizando o `ProdutoRepositoryPort`, ela mantém o sistema flexível e desacoplado, alinhado aos princípios da arquitetura hexagonal, e facilita a manutenção e expansão do projeto.
---

### 8. **Configuração de Beans**

Na arquitetura hexagonal, a configuração explícita de **beans** permite maior controle e clareza sobre as dependências utilizadas no projeto. A classe `AppConfig` é responsável por definir e gerenciar as dependências que serão utilizadas em toda a aplicação, utilizando o Spring para injetá-las automaticamente. Esse arquivo de configuração ajuda a garantir que as interfaces corretas sejam conectadas às suas implementações, permitindo que as camadas de domínio e de infraestrutura permaneçam desacopladas.

No nosso projeto, `AppConfig` é utilizado para **configurar os beans do serviço** (`ProdutoService`), do **repositório** (`ProdutoRepositoryPort`), e do **mapeador** (`ProdutoMapper`), essencial para converter entre as representações de domínio e persistência.

#### Código de `AppConfig` com Explicação

```java
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

    /**
     * Configura o bean para o serviço de Produto, injetando a interface ProdutoRepositoryPort.
     * O `ProdutoService` é a camada de negócios para operações de Produto.
     */
    @Bean
    public ProdutoService produtoService(ProdutoRepositoryPort produtoRepositoryPort) {
        return new ProdutoService(produtoRepositoryPort);
    }

    /**
     * Configura o bean do repositório de produtos, que implementa ProdutoRepositoryPort,
     * permitindo que `ProdutoService` permaneça desacoplado da implementação específica de repositório.
     */
    @Bean
    public ProdutoRepositoryPort produtoRepositoryPort(ProdutoJpaRepository produtoRepository, ProdutoMapper produtoMapper) {
        return new ProdutoRepositoryImpl(produtoRepository, produtoMapper);
    }

    /**
     * Configura o bean do mapeador, que converte entre as entidades de domínio (`Produto`)
     * e de persistência (`ProdutoEntity`), facilitando a separação de camadas.
     */
    @Bean
    public ProdutoMapper produtoMapper() {
        return new ProdutoMapper();
    }
}
```

#### Explicação dos Beans Configurados

1. **Bean `produtoService`**:
  - O `ProdutoService` é a classe de serviço principal que gerencia as operações de negócio para a entidade `Produto`.
  - Esse bean recebe uma instância de `ProdutoRepositoryPort`, permitindo que a lógica de negócios utilize as operações de persistência sem acoplar-se diretamente ao repositório de infraestrutura.
  - A injeção de `ProdutoRepositoryPort` como dependência facilita testes unitários e mantém o `ProdutoService` independente da camada de persistência específica.

   ```java
   @Bean
   public ProdutoService produtoService(ProdutoRepositoryPort produtoRepositoryPort) {
       return new ProdutoService(produtoRepositoryPort);
   }
   ```

2. **Bean `produtoRepositoryPort`**:
  - Este bean representa a implementação do repositório de produtos. O `ProdutoRepositoryImpl` implementa a interface `ProdutoRepositoryPort`, que define os métodos de persistência que podem ser usados pela camada de serviço.
  - Como `ProdutoRepositoryImpl` depende de `ProdutoJpaRepository` e `ProdutoMapper`, esses também são injetados, permitindo que a classe de repositório execute operações no banco de dados e converta as entidades conforme necessário.
  - Ao configurar o bean para `ProdutoRepositoryPort`, garantimos que o `ProdutoService` dependa apenas de uma interface, mantendo o desacoplamento entre camadas.

   ```java
   @Bean
   public ProdutoRepositoryPort produtoRepositoryPort(ProdutoJpaRepository produtoRepository, ProdutoMapper produtoMapper) {
       return new ProdutoRepositoryImpl(produtoRepository, produtoMapper);
   }
   ```

3. **Bean `produtoMapper`**:
  - O `ProdutoMapper` é um mapeador que converte objetos entre `Produto` (a entidade de domínio) e `ProdutoEntity` (a entidade de persistência).
  - Configurar o `ProdutoMapper` como um bean facilita sua reutilização em qualquer parte da aplicação que precise dessas conversões.
  - Esse mapeador ajuda a manter a separação de responsabilidades, garantindo que a camada de domínio não dependa diretamente da camada de persistência.

   ```java
   @Bean
   public ProdutoMapper produtoMapper() {
       return new ProdutoMapper();
   }
   ```

#### Importância de `AppConfig` para o Projeto

A classe `AppConfig` é essencial para a **configuração centralizada** de dependências, permitindo que a aplicação Spring Boot mantenha um **baixo acoplamento** entre as camadas. Essa abordagem oferece as seguintes vantagens:

- **Facilidade de Manutenção**: As dependências podem ser modificadas ou substituídas com facilidade, bastando alterar `AppConfig` para aplicar as mudanças em toda a aplicação.
- **Desacoplamento e Flexibilidade**: Ao configurar interfaces como `ProdutoRepositoryPort` em vez de dependências concretas, garantimos que o código de domínio permaneça desacoplado dos detalhes de infraestrutura.
- **Facilidade de Testes**: A separação das dependências permite a utilização de mocks ou stubs durante os testes unitários, sem interferir no código principal.

Dessa forma, `AppConfig` permite que o projeto siga os princípios da arquitetura hexagonal, garantindo que cada componente dependa apenas de abstrações, facilitando a evolução e a escalabilidade do sistema.

---

### 9. **Controlador REST (`ProdutoController`)**

O `ProdutoController` é a camada de entrada (input) na nossa arquitetura hexagonal. Ele expõe a API RESTful que permite que clientes (frontends, outros serviços ou consumidores de API) interajam com o sistema para realizar operações de **CRUD** (criação, leitura, atualização e exclusão) na entidade `Produto`. Este controlador é responsável por receber as requisições HTTP, processá-las e retornar as respostas apropriadas.

#### Como o `ProdutoController` Funciona

- O `ProdutoController` utiliza a interface `ProdutoServicePort` para acessar os métodos de negócio definidos na camada de serviço (`ProdutoService`), mantendo o controlador desacoplado da implementação exata da lógica de negócios.
- Através da interface `ProdutoServicePort`, o controlador pode realizar operações como:
  - **Criação de produtos** (`POST /api/produtos`)
  - **Consulta de um produto específico** (`GET /api/produtos/{id}`)
  - **Listagem de todos os produtos** (`GET /api/produtos`)
  - **Atualização de um produto** (`PUT /api/produtos`)
  - **Exclusão de um produto** (`DELETE /api/produtos/{id}`)

#### Código do `ProdutoController`

```java
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
```

### Explicação dos Endpoints e Métodos

Aqui está uma lista simples explicando cada método no `ProdutoController`:

1. **`criarProduto`**:
    - Método `POST` para criar um novo produto.
    - Recebe um `ProdutoRequest` no corpo da requisição.
    - Cria um novo `Produto`, salva-o, e retorna um `ProdutoResponse` com status `201 CREATED`.

2. **`obterProduto`**:
    - Método `GET` para obter um produto pelo ID.
    - Recebe o `id` do produto como parâmetro de URL.
    - Retorna um `ProdutoResponse` com status `200 OK` se o produto for encontrado, ou `404 NOT FOUND` se não for.

3. **`listarProdutos`**:
    - Método `GET` para listar todos os produtos.
    - Não recebe parâmetros.
    - Retorna uma lista de `ProdutoResponse` com status `200 OK`.

4. **`atualizarProduto`**:
    - Método `PUT` para atualizar um produto existente pelo ID.
    - Recebe o `id` como parâmetro de URL e um `ProdutoRequest` no corpo da requisição.
    - Atualiza os dados do produto e retorna um `ProdutoResponse` com status `200 OK`.

5. **`deletarProduto`**:
    - Método `DELETE` para excluir um produto pelo ID.
    - Recebe o `id` do produto como parâmetro de URL.
    - Exclui o produto e retorna um status `204 NO CONTENT`.

Cada método usa `ResponseEntity` para encapsular a resposta, o que permite controlar o código de status HTTP retornado.

#### Como o `ProdutoController` Utiliza o `ProdutoServicePort`

O `ProdutoController` interage com a camada de serviço (definida por `ProdutoServicePort`), que oferece uma **interface de comunicação** com os métodos de negócio da aplicação. Utilizar `ProdutoServicePort` em vez de diretamente uma implementação específica facilita o **desacoplamento** e permite **testes mais eficientes**, pois podemos substituir a implementação real por mocks ou stubs quando necessário. 

#### DTOs no formato Requests e Responses

O controller faz uso de DTOs para entradas e saídas de dados. Dessa forma, iremos criar dois `record` para representação dos dados.

```java
package br.com.aygean.hexaarch.adapter.input.dto;

import java.math.BigDecimal;

public record ProdutoRequest(String nome, BigDecimal preco) {
}
```
e
```java
package br.com.aygean.hexaarch.adapter.input.dto;

import java.math.BigDecimal;

public record ProdutoResponse(Long id, String nome, BigDecimal preco) {
}

```


Iremos incluir também o ControllerAdvice, que tem a função de tratar as exceptions do projeto.

```java
package br.com.aygean.hexaarch.adapter.input.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Manipula IllegalArgumentException e retorna uma resposta apropriada.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Invalid Argument");
        body.put("message", ex.getMessage());
        body.put("path", request.getDescription(false).replace("uri=", ""));

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    /**
     * Manipula Exception genérica e retorna uma resposta apropriada.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericException(Exception ex, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        body.put("error", "Internal Server Error");
        body.put("message", ex.getMessage());
        body.put("path", request.getDescription(false).replace("uri=", ""));

        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
```
---


### 10. **Testando a API com cURL ou Postman**

Para testar a API `ProdutoController`, podemos usar o `cURL` no terminal ou configurar o Postman para realizar requisições HTTP para os endpoints. Abaixo estão exemplos para testar cada operação CRUD (Create, Read, Update, Delete) usando dados fictícios.

---

#### 1. Criar Produto (`POST /api/produtos`)

**cURL**:
```bash
curl -X POST http://localhost:8080/api/produtos \
-H "Content-Type: application/json" \
-d '{
  "nome": "Notebook Gamer",
  "preco": 5500.00
}'
```

**Postman**:
1. Selecione o método `POST`.
2. Insira a URL: `http://localhost:8080/api/produtos`.
3. Vá até a aba `Body`, selecione `raw` e `JSON` como tipo de conteúdo.
4. Insira o JSON no corpo:
   ```json
   {
     "nome": "Notebook Gamer",
     "preco": 5500.00
   }
   ```
5. Clique em `Send` para enviar a requisição.

**Resposta Esperada (201 Created)**:
```json
{
  "id": 1,
  "nome": "Notebook Gamer",
  "preco": 5500.00
}
```

---

#### 2. Obter Produto pelo ID (`GET /api/produtos/{id}`)

**cURL**:
```bash
curl -X GET http://localhost:8080/api/produtos/1
```

**Postman**:
1. Selecione o método `GET`.
2. Insira a URL: `http://localhost:8080/api/produtos/1`.
3. Clique em `Send` para enviar a requisição.

**Resposta Esperada (200 OK)**:
```json
{
  "id": 1,
  "nome": "Notebook Gamer",
  "preco": 5500.00
}
```

Caso o produto não exista, a resposta será `404 Not Found`.

---

#### 3. Listar Todos os Produtos (`GET /api/produtos`)

**cURL**:
```bash
curl -X GET http://localhost:8080/api/produtos
```

**Postman**:
1. Selecione o método `GET`.
2. Insira a URL: `http://localhost:8080/api/produtos`.
3. Clique em `Send` para enviar a requisição.

**Resposta Esperada (200 OK)**:
```json
[
  {
    "id": 1,
    "nome": "Notebook Gamer",
    "preco": 5500.00
  },
  {
    "id": 2,
    "nome": "Monitor 4K",
    "preco": 1200.00
  }
]
```

---

#### 4. Atualizar Produto (`PUT /api/produtos/{id}`)

**cURL**:
```bash
curl -X PUT http://localhost:8080/api/produtos/1 \
-H "Content-Type: application/json" \
-d '{
  "nome": "Notebook Gamer - Atualizado",
  "preco": 5800.00
}'
```

**Postman**:
1. Selecione o método `PUT`.
2. Insira a URL: `http://localhost:8080/api/produtos/1`.
3. Vá até a aba `Body`, selecione `raw` e `JSON`.
4. Insira o JSON atualizado:
   ```json
   {
     "nome": "Notebook Gamer - Atualizado",
     "preco": 5800.00
   }
   ```
5. Clique em `Send` para enviar a requisição.

**Resposta Esperada (200 OK)**:
```json
{
  "id": 1,
  "nome": "Notebook Gamer - Atualizado",
  "preco": 5800.00
}
```

---

#### 5. Deletar Produto (`DELETE /api/produtos/{id}`)

**cURL**:
```bash
curl -X DELETE http://localhost:8080/api/produtos/1
```

**Postman**:
1. Selecione o método `DELETE`.
2. Insira a URL: `http://localhost:8080/api/produtos/1`.
3. Clique em `Send` para enviar a requisição.

**Resposta Esperada (204 No Content)**:
- A resposta não terá corpo, indicando que o produto foi excluído com sucesso.

---

Esses exemplos de `cURL` e instruções para o Postman permitem testar todos os endpoints expostos pela API, validando o funcionamento das operações CRUD para a entidade `Produto`.
---

### 11. **Conclusão e Benefícios da Arquitetura Hexagonal**

Ao final deste tutorial, implementamos um sistema de CRUD para a entidade `Produto` usando a arquitetura hexagonal. Este projeto demonstrou como organizar o código em camadas separadas de domínio, aplicação e infraestrutura, tornando o sistema mais modular e desacoplado. Vamos explorar agora os principais benefícios dessa abordagem e sugestões para melhorias futuras.

#### Benefícios da Arquitetura Hexagonal

1. **Flexibilidade e Facilidade de Substituição**:
    - A arquitetura hexagonal permite que cada camada (ou "lado do hexágono") seja desenvolvida e mantida de forma independente. Isso significa que a camada de persistência, por exemplo, pode ser substituída de um banco de dados SQL para NoSQL sem afetar o restante da aplicação.
    - A independência de infraestrutura é uma vantagem chave, pois possibilita testar a aplicação sem a necessidade de serviços externos (como banco de dados ou sistemas de mensageria).

2. **Facilidade de Testes**:
    - Como as interfaces e as classes de domínio estão separadas da camada de infraestrutura, é fácil realizar testes unitários e de integração. Podemos criar testes para cada camada independentemente, usando implementações mock ou stubs para simular a infraestrutura.
    - A arquitetura hexagonal, portanto, incentiva a criação de uma suíte de testes robusta, essencial para manter a qualidade do código e a confiabilidade do sistema.

3. **Desacoplamento e Manutenibilidade**:
    - O desacoplamento das camadas permite que a lógica de negócio seja isolada de detalhes técnicos, como banco de dados ou frameworks de mensageria. Assim, as regras de negócio da aplicação podem ser alteradas sem a necessidade de modificar componentes externos.
    - Com uma organização mais clara e modular, a manutenção do sistema se torna mais eficiente. Novas funcionalidades podem ser adicionadas sem gerar grandes impactos no código existente.

4. **Evolução do Sistema**:
    - Esta arquitetura é altamente extensível. A adição de novos adaptadores, como APIs externas, mensageria ou módulos de segurança, pode ser feita sem modificar as regras de negócio. Por exemplo, podemos adicionar uma interface de API para enviar relatórios a um serviço de análise sem impactar o domínio ou a camada de persistência.
    - Além disso, a arquitetura hexagonal permite que o sistema evolua com novas tecnologias e frameworks, garantindo que a base de código permaneça moderna e fácil de adaptar a novas demandas.

#### Sugestões para Melhorias e Extensões

1. **Adicionar Segurança**:
    - Implementar segurança na API REST é uma das extensões mais comuns e pode ser feita adicionando uma camada de autenticação e autorização. O Spring Security, por exemplo, oferece uma forma de proteger endpoints e controlar o acesso a recursos com JWT (JSON Web Tokens) ou OAuth.

2. **Persistência de Outras Entidades**:
    - Expandir o sistema para incluir outras entidades relacionadas ao `Produto`, como `Categoria` ou `Pedido`, pode enriquecer o modelo de domínio e oferecer mais funcionalidades. Cada nova entidade pode seguir o mesmo padrão de portas e adaptadores, facilitando o crescimento da aplicação.

3. **Adicionar Validações e Tratamento de Exceções Mais Robusto**:
    - Embora tenhamos implementado um `ControllerAdvice` básico, adicionar validações mais complexas e mensagens de erro detalhadas pode melhorar a usabilidade da API. Além disso, o tratamento de exceções personalizadas ajudará a tornar a API mais robusta e amigável para quem a utiliza.

4. **Implementar uma Camada de Mensageria**:
    - Integrar a aplicação a um sistema de mensageria como RabbitMQ ou Kafka seria útil para cenários que envolvem processamento assíncrono, como notificações de estoque ou integração com sistemas externos.

5. **Documentação e API Versioning**:
    - Documentar a API com uma ferramenta como o Swagger/OpenAPI permite que outros desenvolvedores conheçam os endpoints disponíveis e saibam como usá-los. Implementar versionamento na API facilita a evolução da aplicação sem impactar versões antigas que clientes ou serviços ainda podem estar utilizando.

A arquitetura hexagonal é uma escolha valiosa para sistemas que precisam ser mantidos e evoluídos ao longo do tempo. Ela oferece uma organização modular, permitindo que a aplicação cresça com segurança, seja testada com facilidade e se mantenha flexível para as futuras mudanças do mercado e das tecnologias. Com as melhorias sugeridas, o sistema poderá atender ainda mais às demandas de uma aplicação robusta e moderna.

---

### Configurando o Docker e Application Properties

1. **Docker Compose**: Configure o arquivo `docker-compose.yml` para iniciar o PostgreSQL.

```yaml
version: '3.8'

services:
  postgres:
    image: postgres:15  # Utilize a versão que preferir
    container_name: postgres_container
    environment:
      POSTGRES_USER: postgres        # Substitua por seu usuário
      POSTGRES_PASSWORD: postgres  # Substitua pela sua senha
      POSTGRES_DB: hexaarch       # Substitua pelo nome do seu banco de dados
    volumes:
      - ./data:/var/lib/postgresql/data  # Monta a pasta `data` para persistir os dados
    ports:
      - "5432:5432"

```

2. **Application Properties**: Configure o arquivo `application.properties` para o PostgreSQL.

```properties
spring.application.name=hexaarch

# Configuração do DataSource
spring.datasource.url=jdbc:postgresql://localhost:5432/apirest
spring.datasource.username=postgres
spring.datasource.password=postgres

# Configurações adicionais para o PostgreSQL
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect

# Configuração para mostrar as queries SQL no console
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# Configuração para gerenciamento de schema (opcional)
spring.jpa.hibernate.ddl-auto=create
```
#### Acessando o container postgres

````text
docker ps  # Lista os containers em execução
docker exec -it <container_name_or_id> bash  # Acessa o terminal do container
psql -U postgres  # Acessa o prompt do PostgreSQL
CREATE DATABASE nome_do_banco;  # Cria o banco de dados
\q  # Sai do prompt do PostgreSQL
exit  # Sai do container
````

> Não esqueça de substituir pelo nome do seu banco de dados

### Diagrama UML simplificado

```plaintext
                                   +-------------------------------+
                                   |        ProdutoController      |
                                   |-------------------------------|
                                   | + criarProduto(request):      |
                                   |    ResponseEntity<ProdutoRes> |
                                   | + obterProduto(id):           |
                                   |    ResponseEntity<ProdutoRes> |
                                   | + listarProdutos():           |
                                   |    ResponseEntity<List<...>>  |
                                   | + atualizarProduto(id, req):  |
                                   |    ResponseEntity<ProdutoRes> |
                                   | + deletarProduto(id):         |
                                   |    ResponseEntity<Void>       |
                                   +-------------------------------+
                                               |
                                               v
                                   +----------------------------+
                                   |      ProdutoService        |
                                   |----------------------------|
                                   | + criarProduto(produto):   |
                                   |   Produto                  |
                                   | + obterProdutoPorId(id):   |
                                   |   Optional<Produto>        |
                                   | + listarProdutos():        |
                                   |   List<Produto>            |
                                   | + atualizarProduto(prod):  |
                                   |   Produto                  |
                                   | + deletarProduto(id):      |
                                   |   void                     |
                                   +----------------------------+
                                               |
                                               v
                               +----------------------------------+
                               |      ProdutoRepositoryPort       |
                               |----------------------------------|
                               | <<interface>>                    |
                               | + save(produto): Produto         |
                               | + findById(id): Optional<Produto>|
                               | + findAll(): List<Produto>       |
                               | + deleteById(id): void           |
                               +----------------------------------+
                                               |
                                               v
                               +--------------------------------------+
                               |        ProdutoRepositoryImpl         |
                               |--------------------------------------|
                               | + produtoJpaRepository: JpaRepo<>    |
                               | + produtoMapper: ProdutoMapper       |
                               |--------------------------------------|
                               | + save(produto): Produto             |
                               | + findById(id): Optional<Produto>    |
                               | + findAll(): List<Produto>           |
                               | + deleteById(id): void               |
                               +--------------------------------------+
                                               |
                                               v
                               +--------------------------------------+
                               |      ProdutoJpaRepository            |
                               |--------------------------------------|
                               | <<interface>> extends JpaRepository  |
                               +--------------------------------------+

                                                ^
                                                |
                              +-----------------+-----------------+
                              |                                   |
              +---------------------------+            +--------------------------+
              |       ProdutoMapper       |            |      ProdutoEntity       |
              |---------------------------|            |--------------------------|
              | + toEntity(produto):      |            | + id: Long               |
              |   ProdutoEntity           |            | + nome: String           |
              | + toDomain(entity):       |            | + preco: BigDecimal      |
              |   Produto                 |            +--------------------------+
              +---------------------------+            | + getters and setters    |
                                                       +--------------------------+
```

#### Explicação da Estrutura

1. **Camada de Apresentação**:
    - **ProdutoController**: Classe de controle da camada REST que expõe os endpoints para manipular `Produto`. Cada método (como `criarProduto` e `listarProdutos`) utiliza `ProdutoService` para operações de negócio.

2. **Camada de Serviço (Core)**:
    - **ProdutoService**: Centraliza a lógica de negócio, chamando a interface `ProdutoRepositoryPort` para persistir dados. Define métodos como `criarProduto`, `obterProdutoPorId`, `listarProdutos`, entre outros.

3. **Camada de Portas e Adaptadores**:
    - **ProdutoRepositoryPort**: Interface que define as operações de persistência para `Produto`, sendo implementada pela `ProdutoRepositoryImpl`.
    - **ProdutoRepositoryImpl**: Implementação de `ProdutoRepositoryPort` que utiliza `ProdutoJpaRepository` e `ProdutoMapper` para interagir com a camada de persistência.

4. **Camada de Persistência**:
    - **ProdutoJpaRepository**: Interface que estende `JpaRepository`, fornecendo métodos CRUD nativos para `ProdutoEntity`.
    - **ProdutoMapper**: Classe que converte entre `Produto` (domínio) e `ProdutoEntity` (persistência), mantendo a separação das camadas.
    - **ProdutoEntity**: Classe que representa a entidade `Produto` no banco de dados, mapeada com anotações JPA.

### Conexões

- As setas indicam a direção das dependências, com o controlador chamando o serviço, o serviço chamando a porta de repositório, e assim por diante.

