# Gestão de Tarefas (Java 17 + Angular 18)

Projeto completo do desafio técnico: **Gestão de Tarefas** (Task) com:
- Backend Spring Boot + JPA + SQL Server
- Frontend Angular 18 + Material
- Bônus inclusos: Paginação + ordenação, Swagger/OpenAPI, Interceptors (erro + loading + auth), JWT multi-usuário (User 1:N Tasks)

## Requisitos
- Java 17
- Maven 3.9+
- Node 18+ (recomendado) e Angular CLI 18
- Docker (opcional, para SQL Server)

## Subir banco (SQL Server via Docker)
Na raiz do Projeto:
```bash
docker compose up -d
```

## Backend
```bash
cd backend
mvn clean test
mvn spring-boot:run
```

Swagger:
- http://localhost:8080/swagger-ui.html

### Credenciais seed (bônus exigido)
- user: `admin`
- pass: `123456`

### Endpoints
- POST `/api/auth/register`
- POST `/api/auth/login`
- CRUD Tasks (protegido):
  - POST `/api/tasks`
  - GET `/api/tasks?page=0&size=10&status=TODO`
  - GET `/api/tasks/{id}`
  - PUT `/api/tasks/{id}`
  - DELETE `/api/tasks/{id}`

## Frontend
```bash
cd frontend
npm install
npm start
```

Abrir:
- http://localhost:4200

Login: `admin / 123456`

## Decisões técnicas
- **DTOs** para validação e contrato HTTP
- **Erro padronizado** `ApiError` com timestamp/status/error/message/path
- **JWT stateless**: token no header `Authorization: Bearer <token>`
- **Multi-usuário**: tarefas sempre filtradas pelo usuário logado; acesso indevido retorna 404 (não vaza existência)
- **Paginação/ordenação**: `Page` com `Sort createdAt DESC`
- **Interceptors Angular**: auth + loading + erro (401 redireciona login)
