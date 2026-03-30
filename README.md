# Duck Bill

Aplicação Spring Boot para controle de despesas pessoais e investimentos, com frontend Thymeleaf, Spring Security e Flyway.

## Integrantes
- Bruno Carlos Soares RM 559250 - Responsável pelos testes funcionais e validação dos endpoints.
- Lucas Borges de Souza RM 560027 - Desenvolvimento completo da aplicação Spring Boot, controllers e serviços.
- Pedro Henrique Rodrigues RM 560393 - Criação da documentação, diagramas e README do projeto.

## Repositório
- GitHub: `https://github.com/Lucas-Borges27/duckbill-JAVA`

## Pré-requisitos
- Java 17+
- Preferencialmente usar `./mvnw`
- Acesso ao Oracle FIAP em `oracle.fiap.com.br:1521/orcl`
- Se necessário, rede/VPN com resolução do host `oracle.fiap.com.br`

## Como rodar

### Execução
1. Clone o repositório.
2. Exporte as variáveis:
   `DB_USER`
   `DB_PASSWORD`
3. Execute: `./mvnw spring-boot:run`
4. O Flyway criará/versionará o schema e aplicará as migrations.
5. A aplicação usa a URL fixa `jdbc:oracle:thin:@oracle.fiap.com.br:1521:orcl`.

### Testes
- Execute: `./mvnw test`
- Os testes usam a mesma configuração Oracle da aplicação e dependem de `DB_USER` e `DB_PASSWORD`.
- Se o host Oracle não estiver acessível na rede da máquina, o teste de contexto irá falhar.

## Acesso
- Web: `http://localhost:8080/login`

### Credenciais seed
- Admin: `admin@duckbill.com` / `admin123`
- User: `user@duckbill.com` / `user123`

### Rotas web principais
- `/login`
- `/app/dashboard`
- `/app/despesas/nova`
- `/app/transacoes/nova`
- `/admin/categorias`
- `/acesso-negado`

## Diagramas
### DER
![Diagrama ER](docs/images/DER.png)

### Diagrama de Classes
![Diagrama de Classes](docs/images/D_Classes.png)

## Vídeo
- URL : [https://youtu.be/3hpxwZli2kY?si=s6yuTLSgUr45mRD_](https://youtu.be/3hpxwZli2kY?si=s6yuTLSgUr45mRD_)

## Endpoints principais
- Usuários: POST/GET /api/v1/usuarios, GET /api/v1/usuarios/{id}
- Categorias: POST/GET /api/v1/categorias
- Despesas: POST/GET /api/v1/despesas, GET /api/v1/despesas/top3, GET /api/v1/despesas/insights
- Ativos: POST/GET /api/v1/ativos, GET /api/v1/ativos/{id}, PUT /api/v1/ativos/{id}
- Transações Ativo: POST/GET /api/v1/transacoes-ativo, GET /api/v1/transacoes-ativo/{id}, PUT /api/v1/transacoes-ativo/{id}
- Cotações de Ativo: POST/GET /api/v1/cotacoes-ativo, GET /api/v1/cotacoes-ativo/{ativoId}/{dataRef}
- Cotações de Moeda: GET /api/v1/cotacoes-moeda, GET /api/v1/cotacoes-moeda/{moeda}/{dataRef}
- Câmbio (serviço utilitário): GET /api/v1/cambio

## Postman
Para testar os endpoints da API, importe a coleção do Postman localizada em `docs/postman/duckBill-postman.json`. A coleção inclui exemplos de requisições para todos os endpoints principais.

## Autenticação da API
As rotas `/api/**` são protegidas por sessão do Spring Security. Para testar com `curl`, faça login antes e reuse o cookie:

```bash
curl -c cookies.txt -X POST http://localhost:8080/login \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "username=user@duckbill.com&password=user123"
```

Depois use o cookie salvo:

```bash
curl -b cookies.txt "http://localhost:8080/api/v1/despesas?usuarioId=1&mes=2026-03"
```

## Exemplos de uso
Os exemplos abaixo assumem uma sessão autenticada válida via `cookies.txt`. Para rotas administrativas, faça login com `admin@duckbill.com`.

### 1. Criar usuário
```bash
curl -b cookies.txt -X POST http://localhost:8080/api/v1/usuarios \
  -H "Content-Type: application/json" \
  -d '{"nome":"João Silva","email":"joao@example.com","senha":"senha123","role":"ROLE_USER"}'
```

### 2. Criar categoria
```bash
curl -b cookies.txt -X POST http://localhost:8080/api/v1/categorias \
  -H "Content-Type: application/json" \
  -d '{"nome":"Alimentacao"}'
```

### 3. Criar despesa
```bash
curl -b cookies.txt -X POST http://localhost:8080/api/v1/despesas \
  -H "Content-Type: application/json" \
  -d '{"usuarioId":1,"categoriaId":1,"valor":50.00,"moeda":"BRL","dataCompra":"2023-10-01","descricao":"Jantar"}'
```

### 4. Listar despesas do mês
```bash
curl -b cookies.txt -X GET "http://localhost:8080/api/v1/despesas?usuarioId=1&mes=2023-10"
```

### 5. Top 3 categorias por gasto
```bash
curl -b cookies.txt -X GET "http://localhost:8080/api/v1/despesas/top3?usuarioId=1&mes=2023-10"
```

### 6. Insights básicos
```bash
curl -b cookies.txt -X GET "http://localhost:8080/api/v1/despesas/insights?usuarioId=1&mes=2023-10"
```

### 7. Converter moeda
```bash
curl -b cookies.txt -X GET "http://localhost:8080/api/v1/cambio?from=USD&to=BRL&valor=100"
```

### 8. Criar ativo
```bash
curl -b cookies.txt -X POST http://localhost:8080/api/v1/ativos \
  -H "Content-Type: application/json" \
  -d '{"ticker":"PETR4.SA","tipo":"STOCK","moedaBase":"BRL"}'
```

### 9. Criar transação de ativo
```bash
curl -b cookies.txt -X POST http://localhost:8080/api/v1/transacoes-ativo \
  -H "Content-Type: application/json" \
  -d '{"usuarioId":1,"ativoId":1,"tipo":"BUY","qtd":10.0,"preco":25.50,"dataNegocio":"2023-10-01"}'
```

### 10. Buscar cotação de moeda
```bash
curl -b cookies.txt -X GET "http://localhost:8080/api/v1/cotacoes-moeda/USD/2023-10-01"
```

## Evolução do Projeto

### Sprint 1 (Maturity Level 1 - Recursos)
- Implementação das entidades básicas (Usuario, Categoria, Despesa, Ativo, TransacaoAtivo, CotacaoAtivo, CotacaoMoeda).
- CRUD básico para todas as entidades.
- Persistência com JPA/Hibernate e Oracle Database.
- Validações básicas com Bean Validation.
- Testes funcionais com Postman.

### Sprint 2 (Maturity Level 3 - Hypermedia Controls)
- Adição de HATEOAS (Hypermedia as the Engine of Application State) para navegação RESTful.
- Implementação de links self, collection e relacionados em todas as respostas da API.
- Dependência spring-boot-starter-hateoas adicionada.
- Controllers atualizados para retornar EntityModel e CollectionModel com links dinâmicos.
- Exemplo: Um usuário agora inclui links para suas despesas, e uma despesa inclui links para usuário e categoria.
- Melhorias na arquitetura: separação em camadas (Controller, Service, Repository, Mapper, DTO).
- Validações aprimoradas e tratamento de erros global.
- Coleção Postman atualizada para refletir as mudanças.

### Sprint 3 (Java Advanced - Web + Security + Flyway)
- Frontend web com Thymeleaf e telas funcionais.
- Autenticação por formulário (Spring Security) e senha BCrypt.
- Perfis `ROLE_USER` e `ROLE_ADMIN` com autorização por rotas.
- Fluxo A: Dashboard mensal com total, top 3 e insights (regra no service).
- Fluxo B: Investimentos com formulário, histórico de transações e resumo consolidado da carteira.
- Fluxo C: Admin bloqueia exclusão de categoria com despesas vinculadas.
- Migrações Flyway V1 (schema), V2 (seed) e V3 (ajuste de identities).

## Roteiro do vídeo
Consulte `docs/roteiro-video.md`.

## Material para avaliação oral e vídeo
- README com instalação, execução e acesso.
- Diagramas em `docs/images`.
- Roteiro em `docs/roteiro-video.md`.
- Coleção Postman em `docs/postman/duckBill-postman.json`.
- Credenciais seed para perfis USER e ADMIN.

## Configuração centralizada
Toda a configuração da aplicação está em `src/main/resources/application.properties`.
