# SkillUp

## 3.1) Nome da aplicação
SkillUp Tracker é uma API Spring Boot (Java 21) para cadastro de usuários, cursos e acompanhamento de progresso em trilhas de aprendizagem. A aplicação utiliza JPA/Hibernate com Oracle Database e foi ajustada para refletir integralmente o modelo de dados `SKILLUP_USUARIO`, `SKILLUP_CURSO` e `SKILLUP_PROGRESSO`.

## 3.2) Nome completo e breve apresentação dos integrantes do Grupo (atividade da qual ficou responsável no projeto)
- Bruno Carlos Soares RM 559250 - Responsável pelos testes funcionais e validação dos endpoints.
- Lucas Borges de Souza RM 560027 - Desenvolvimento completo da aplicação Spring Boot, controllers e serviços.
- Pedro Henrique Rodrigues RM 560393 - Criação da documentação, diagramas e README do projeto.

## 3.3) Instrução de como rodar a aplicação
1. Clone o repositório.
2. Configure o banco Oracle em `src/main/resources/application.properties` (url, username, password).
3. Execute: `mvn spring-boot:run`
Após iniciar a aplicação, acesse a interface do Swagger UI para explorar e testar os endpoints da API:
   - URL : [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

## 3.4) Imagem dos diagramas
### Diagrama ER (Entidade-Relacionamento)
![Diagrama ER](docs/images/DER.png)

### Diagrama de Classes
![Diagrama de Classes](docs/images/D_Classes.png)

## 3.5) Link para vídeo apresentando a Proposta Tecnológica, o público-alvo da aplicação e os problemas que a aplicação se propõe a solucionar
- URL : [https://youtu.be/3hpxwZli2kY?si=s6yuTLSgUr45mRD_](https://youtu.be/3hpxwZli2kY?si=s6yuTLSgUr45mRD_)

## 3.6) Listagem de todos os endpoints (documentação da API)
- Autenticação: `POST /api/v1/auth/login` (gera JWT para ser usado no header `Authorization: Bearer <token>`)
- Usuários: `POST/GET /api/v1/usuarios`, `GET /api/v1/usuarios/{id}`, `PUT /api/v1/usuarios/{id}`, `DELETE /api/v1/usuarios/{id}`
- Cursos: `POST/GET /api/v1/cursos`, `GET /api/v1/cursos/{id}`, `PUT /api/v1/cursos/{id}`, `DELETE /api/v1/cursos/{id}`
- Progresso: `POST/GET /api/v1/progressos`, `GET /api/v1/progressos/{id}`, `PUT /api/v1/progressos/{id}`, `DELETE /api/v1/progressos/{id}`
  - Filtros disponíveis em `/api/v1/progressos`: `usuarioId`, `cursoId` ou `status`. Paginação e ordenação seguem `?page=0&size=10&sort=campo,asc`.

### Autenticação JWT
1. Crie um usuário (`POST /api/v1/usuarios`) – aberto.
2. Autentique-se em `POST /api/v1/auth/login` informando `email` e `senha`.
3. Envie o token retornado em todos os demais endpoints protegidos através do header `Authorization: Bearer <token>`.
4. Tokens expiram conforme `jwt.expiration-ms` (padrão 60 min) configurado em `application.properties` ou variável de ambiente `JWT_EXPIRATION_MS`.

### Paginação, ordenação e filtros
Todos os recursos de leitura (`/usuarios`, `/cursos`, `/progressos`) aceitam os parâmetros Spring padrão:
`page`, `size`, `sort`. Exemplos:
- `/api/v1/cursos?categoria=Backend&size=5&sort=nome,asc`
- `/api/v1/usuarios?areaInteresse=Data&page=1`
- `/api/v1/progressos?usuarioId=1&sort=dataInicio,desc`

Os links HATEOAS retornados pelo backend já incluem os parâmetros de navegação (`self`, `next`, `prev`).

## Testes com Postman
Para testar os endpoints da API, importe a coleção do Postman localizada em `docs/postman/skillup-postman.json`. A coleção inclui exemplos de requisições para criar usuários, cadastrar cursos e registrar progresso utilizando as tabelas SKILLUP\_*.

## Testes Possíveis (via curl ou Postman)
### 0. Autenticar e obter token
```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"ana@example.com","senha":"senha123"}'
```
Resposta: `{"token":"<jwt>","type":"Bearer","expiresInMs":3600000}`

Use o token nos exemplos abaixo.

### 1. Criar Usuário (público)
```bash
curl -X POST http://localhost:8080/api/v1/usuarios \
  -H "Content-Type: application/json" \
  -d '{"nome":"Ana Souza","email":"ana@example.com","senha":"senha123","areaInteresse":"Data"}'
```

### 2. Criar Curso
```bash
curl -X POST http://localhost:8080/api/v1/cursos \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <TOKEN>" \
  -d '{"nome":"Fundamentos de Java","categoria":"Backend","cargaHoraria":40,"dificuldade":"INTERMEDIARIO","descricao":"Curso rápido de Java."}'
```

### 3. Registrar Progresso
```bash
curl -X POST http://localhost:8080/api/v1/progressos \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <TOKEN>" \
  -d '{"usuarioId":1,"cursoId":1,"status":"EM_ANDAMENTO","porcentagem":25.5}'
```

### 4. Atualizar Progresso
```bash
curl -X PUT http://localhost:8080/api/v1/progressos/1 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <TOKEN>" \
  -d '{"status":"CONCLUIDO","porcentagem":100,"dataFim":"2024-11-10"}'
```

### 5. Listar Progresso por Usuário
```bash
curl -H "Authorization: Bearer <TOKEN>" \
  -X GET "http://localhost:8080/api/v1/progressos?usuarioId=1&page=0&size=5&sort=dataInicio,desc"
```

## Deploy em nuvem

### Containerização
1. Gere o artefato: `./mvnw clean package -DskipTests`
2. Monte a imagem definida no `Dockerfile`:
   ```bash
   docker build -t ghcr.io/<usuario>/skillup-api:latest .
   ```
3. Configure variáveis sensíveis via ambiente na nuvem:
   - `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD`
   - `JWT_SECRET`, `JWT_EXPIRATION_MS`
   - `SERVER_PORT` (quando o provedor exigir porta diferente)

### Exemplo de deploy (Render/ Railway / Azure Container Apps)
1. Suba a imagem para um registry (Docker Hub, GHCR, etc).
2. Crie o serviço na nuvem apontando para a imagem.
3. Defina as variáveis citadas e o volume/secret do banco.
4. Exponha a porta 8080 (ou mapeie para a porta padrão do provedor).

O README continua servindo como guia local enquanto o pipeline de CI/CD pode executar `docker build/push` automaticamente. Para ambientes que não suportam Docker, basta empacotar o JAR e usar serviços como Azure App Service ou AWS Elastic Beanstalk apontando para o pacote gerado.

## Evolução do Projeto

### Sprint 1 (Maturity Level 1 - Recursos)
- Modelagem e CRUD das entidades `Usuario`, `Curso` e `Progresso`, aderentes ao script SKILLUP.
- Persistência com JPA/Hibernate e Oracle Database.
- Validações com Bean Validation e tratamento básico de erros.
- Documentação com Swagger/OpenAPI.

### Sprint 2 (Maturity Level 3 - Hypermedia Controls)
- Respostas HATEOAS em todos os recursos (links para coleções, detalhes e relacionamentos).
- Serviços específicos para encapsular regras (ex.: validação de datas e percentuais de progresso).
- Filtros por usuário, curso ou status no recurso de progresso.
- Coleção Postman e README atualizados para refletir o novo domínio SkillUp.

## Cronograma
- Semana 1: Modelagem das tabelas SKILLUP, criação das entidades e repositórios.
- Semana 2: Implementação dos controllers/services com validações e filtros.
- Semana 3: HATEOAS, testes funcionais, documentação final e ajustes.

## Gestão e Configuração
- Script de criação das tabelas em `docs/sql/CREATE_TABLE.sql`.
- Configuração via `application.properties` (Oracle).
- Postman collection em `docs/postman/skillup-postman.json`.
