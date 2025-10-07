# Cronograma de Desenvolvimento - Duck Bill

## Visão Geral
O desenvolvimento foi realizado em sprints curtas, focando em entidades, serviços, controladores e documentação. Prazos respeitados com base em milestones semanais.

## Tarefas e Responsabilidades

| Tarefa | Responsável | Prazo | Status |
|--------|-------------|-------|--------|
| Análise de requisitos e criação de entidades JPA (Categoria, Usuario, Despesa, CotacaoMoeda, Ativo, TransacaoAtivo, CotacaoAtivo) | Lucas Borges | Semana 1 (Dia 1-3) | Concluído |
| Implementação de repositórios JPA para todas entidades | Lucas Borges | Semana 1 (Dia 4-5) | Concluído |
| Desenvolvimento de serviços (CRUD, relatórios Top 3, insights básicos) | Lucas Borges | Semana 1 (Dia 6-7) | Concluído |
| Criação de controladores REST (nível 1: CRUD básico) e DTOs/Mappers | Lucas Borges | Semana 2 (Dia 1-3) | Concluído |
| Integração com APIs externas (AwesomeAPI para câmbio, opcional Alpha Vantage) | Lucas Borges | Semana 2 (Dia 4-5) | Pendente (câmbio implementado, ações opcional) |
| Documentação (README.md, diagramas, Postman, Swagger) | Lucas Borges | Semana 2 (Dia 6) | Concluído (exceto vídeo e imagens) |
| Testes unitários/integração e verificação de persistência Oracle | Lucas Borges | Semana 2 (Dia 7) | Pendente |
| Deploy no GitHub público e export Postman | Lucas Borges | Semana 3 (Dia 1) | Pendente |

## Notas
- Todas as atividades foram realizadas por Lucas Borges.
- Evolução por sprint: Sprint 1 - Domínio e persistência; Sprint 2 - API REST e integrações; Sprint 3 - Testes e deploy.
- Diagramas: DER e Classes de Entidade coerentes com tabelas SQL fornecidas (relacionamentos ManyToOne, chaves compostas).
- Constraints: Unique keys, not null, checks (e.g., moeda length=3, tipo BUY/SELL).
