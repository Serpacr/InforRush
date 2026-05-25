# 🚀 InfoRush

Portal web de curadoria e publicação de conteúdo sobre animes — Projeto Final de DevOps.

## 🛠️ Tecnologias

| Camada | Tecnologia |
|--------|-----------|
| Frontend | React 18 + Vite |
| Backend | Java 17 + Spring Boot 3.2 |
| Banco | PostgreSQL 16 |
| Auth | Spring Security + JWT |
| Docs | Swagger / OpenAPI 3 |
| Containers | Docker + Docker Compose |
| CI/CD | Jenkins |
| Qualidade | SonarQube |
| Versionamento | Git + GitFlow |

## 🚀 Como executar

```bash
# 1. Clone o repositório
git clone https://github.com/Serpacr/InfoRush.git
cd InfoRush

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
├── backend/       # Spring Boot + JPA + Security + JWT + Swagger
├── frontend/      # React + Vite
├── docker-compose.yml
├── Jenkinsfile
└── sonar-project.properties
```

## 🔀 GitFlow

```
main      → produção
develop   → integração
feature/* → novas features
release/* → preparação de versão
```

## 👥 Integrantes

| Nome | RA |
|------|----|
| Caio Serpa | 2024110220047 |
