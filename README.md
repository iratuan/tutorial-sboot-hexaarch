![Arquitetura Limpa](/images/f01_capa.png "Capa")

# Tutorial de arquitetura hexagonal utilizando spring boot

- Autor: Iratuã Júnior
- Data: 07/11/2024
- Versão do springboot: 3.3.5
- Versão jdk: 17
- [Respositorio no github](https://github.com/iratuan/tutorial-clean-arch)

## Sobre o tutorial

## Parte 1 - Configurando o projeto

## Parte 2 - Criando a estrutura de pacotes e explicando a motivação

## Parte 3 - Criando o core da aplicação

## Parte 4 - Criando a infra

## Parte 5 - Criando os adapters

## Parte 6 - Testando a aplicação

_____________________
## Material extra

### docker-compose.yml

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


### Acessando o container postgres

````dockerfile
docker ps  # Lista os containers em execução
docker exec -it <container_name_or_id> bash  # Acessa o terminal do container
psql -U postgres  # Acessa o prompt do PostgreSQL
CREATE DATABASE nome_do_banco;  # Cria o banco de dados
\q  # Sai do prompt do PostgreSQL
exit  # Sai do container

````

> Não esqueça de substituir pelo nome do seu banco de dados