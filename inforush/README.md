# 🚀 InfoRush

Portal web de curadoria e publicação de conteúdo sobre animes — Projeto Final de DevOps.

## 🛠️ Tecnologias

| Camada | Tecnologia |
|--------|-----------|
| Frontend | React 18 + Vite |
| Backend | Java 21 + Spring Boot 3.2 |
| Banco | PostgreSQL 16 |
| Auth | Spring Security + JWT |
| Docs | Swagger / OpenAPI 3 |
| Containers | Docker + Docker Compose |
| CI/CD | GitHub Actions |
| Qualidade | SonarCloud |
| Versionamento | Git + GitFlow |

## 🚀 Como executar

```bash
# 1. Clone o repositório
git clone https://github.com/Serpacr/InforRush.git
cd InforRush/inforush

# 2. Suba todos os serviços
docker compose up --build
```

## 🌐 Acessos

| Serviço | URL |
|---------|-----|
| Frontend | http://localhost:5173 |
| Backend API | http://localhost:8080 |
| Swagger UI | http://localhost:8080/swagger-ui.html |

## 👤 Conta demo

```
E-mail: admin@inforush.com
Senha:  admin123
```

## 📋 Comandos úteis

```bash
docker compose up --build      # Sobe tudo
docker compose down            # Para tudo
docker compose logs -f backend # Ver logs do backend
docker compose down -v         # Para e apaga o banco
```

## 📁 Estrutura

```
InfoRush/
├── inforush/
│   ├── backend/       # Spring Boot + JPA + Security + JWT + Swagger
│   ├── frontend/      # React + Vite
│   └── docker-compose.yml
├── .github/workflows/ # GitHub Actions CI
└── sonar-project.properties
```

## 🔀 GitFlow

```
main      → produção
develop   → integração
feature/* → novas features
fix/*     → correções
```

## 🔄 Pipeline (GitHub Actions)

A cada push em `main` ou `develop`, a pipeline executa:

1. **Backend** — `mvn clean verify` (testes + cobertura JaCoCo)
2. **Frontend** — `npm ci` + `npm run build`
3. **SonarCloud** — análise de qualidade e quality gate
4. **Docker** — `docker compose build`

Arquivo: `.github/workflows/ci.yml`

### SonarCloud

- Projeto: [Serpacr_InforRush](https://sonarcloud.io/project/overview?id=Serpacr_InforRush)
- Secret necessário no GitHub: `SONAR_TOKEN`

## 👥 Integrantes

| Nome | RA |
|------|----|
| Caio Serpa | 2024110220047 |
