# TODO List for Sprint 2 Improvements

## 1. Add HATEOAS Dependency
- [x] Add spring-hateoas dependency to pom.xml

## 2. Update Controllers for HATEOAS Level 3
- [x] UsuarioController: Add EntityModel/CollectionModel with self, collection, and related links (e.g., to despesas)
- [x] AtivoController: Add EntityModel/CollectionModel with self, collection, and related links (e.g., to transacoes-ativo)
- [x] DespesaController: Add EntityModel/CollectionModel with self, collection, and related links (e.g., to usuario, categoria)
- [x] CategoriaController: Add EntityModel/CollectionModel with self, collection
- [x] CotacaoAtivoController: Add EntityModel/CollectionModel with self, collection
- [x] CotacaoMoedaController: Add EntityModel/CollectionModel with self, collection
- [x] TransacaoAtivoController: Add EntityModel/CollectionModel with self, collection, and related links (e.g., to ativo, usuario)
- [x] CambioController: Add EntityModel for response with self link

## 3. Update Documentation
- [x] Update README.md to document evolution from Sprint 1 (added services, mappers, validations, HATEOAS)

## 4. Testing and Verification
- [ ] Run the application locally
- [ ] Test all endpoints with Postman to verify HATEOAS responses
- [ ] Update Postman collection if needed
- [ ] Export Postman collection

## 5. GitHub Management
- [ ] Commit all changes
- [ ] Push to GitHub repository
