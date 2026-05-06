# CIMA – Sistema de Gestão Integrado
### Optimização da Manutenção Mecânica | C.I.M.A – Construção Civil

---

## Tecnologias
- **Java 17** + **Spring Boot 3.2**
- **Spring Security** + **JWT** (autenticação stateless)
- **Spring Data JPA** + **MySQL 8**
- **Swagger / OpenAPI 3** (documentação interativa)
- **Lombok** + **MapStruct**
- **Maven**

---

## Pré-requisitos
- Java 17+
- Maven 3.8+
- MySQL 8.0+

---

## Configuração

### 1. Criar a base de dados
```sql
CREATE DATABASE cima_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```
Ou executa o script completo:
```bash
mysql -u root -p < src/main/resources/schema.sql
```

### 2. Configurar `application.properties`
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/cima_db?useSSL=false&serverTimezone=UTC
spring.datasource.username=SEU_USUARIO
spring.datasource.password=SUA_SENHA
jwt.secret=CIMASecretKeyForJWTTokenGenerationMustBeAtLeast256BitsLong2024
```

### 3. Compilar e arrancar
```bash
mvn clean install
mvn spring-boot:run
```

---

## Utilizador padrão (criado automaticamente)
| Campo | Valor |
|-------|-------|
| Email | `admin@cima.ao` |
| Senha | `Admin@2024` |
| Perfil | `ADMINISTRADOR` |

---

## Documentação da API (Swagger)
Após arrancar o servidor acede a:
```
http://localhost:8080/api/swagger-ui.html
```

---

## Endpoints principais

### Autenticação
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/api/auth/login` | Login e obtenção do token JWT |

### RF01 – Perfis
| Método | Endpoint | Permissão |
|--------|----------|-----------|
| GET | `/api/perfis` | ADMINISTRADOR |
| POST | `/api/perfis` | ADMINISTRADOR |
| PUT | `/api/perfis/{id}` | ADMINISTRADOR |
| DELETE | `/api/perfis/{id}` | ADMINISTRADOR |

### RF02 – Utilizadores
| Método | Endpoint | Permissão |
|--------|----------|-----------|
| GET | `/api/utilizadores` | ADMINISTRADOR |
| POST | `/api/utilizadores` | ADMINISTRADOR |
| PUT | `/api/utilizadores/{id}` | ADMINISTRADOR |
| DELETE | `/api/utilizadores/{id}` | ADMINISTRADOR |

### RF03 – Inventário
| Método | Endpoint | Permissão |
|--------|----------|-----------|
| GET | `/api/inventario` | ADMINISTRADOR, GERENTE_STOCK |
| POST | `/api/inventario` | ADMINISTRADOR, GERENTE_STOCK |
| PUT | `/api/inventario/{id}` | ADMINISTRADOR, GERENTE_STOCK |
| DELETE | `/api/inventario/{id}` | ADMINISTRADOR |

### RF04 – Fornecedores
| Método | Endpoint | Permissão |
|--------|----------|-----------|
| GET | `/api/fornecedores` | ADMINISTRADOR, GERENTE_STOCK |
| POST | `/api/fornecedores` | ADMINISTRADOR, GERENTE_STOCK |
| PUT | `/api/fornecedores/{id}` | ADMINISTRADOR, GERENTE_STOCK |
| DELETE | `/api/fornecedores/{id}` | ADMINISTRADOR |

### RF05 – Movimentos de Stock
| Método | Endpoint | Permissão |
|--------|----------|-----------|
| GET | `/api/movimentos-stock` | ADMINISTRADOR, GERENTE_STOCK |
| POST | `/api/movimentos-stock` | ADMINISTRADOR, GERENTE_STOCK |

### RF06/RF07 – Manutenções
| Método | Endpoint | Permissão |
|--------|----------|-----------|
| GET | `/api/manutencoes` | ADMINISTRADOR, GERENTE_MANUTENCAO |
| POST | `/api/manutencoes` | ADMINISTRADOR, GERENTE_MANUTENCAO |
| PATCH | `/api/manutencoes/{id}/estado` | ADMINISTRADOR, GERENTE_MANUTENCAO |
| GET | `/api/manutencoes/atrasadas` | ADMINISTRADOR, GERENTE_MANUTENCAO |
| GET | `/api/manutencoes/proximas` | ADMINISTRADOR, GERENTE_MANUTENCAO |

### RF08 – Tarefas
| Método | Endpoint | Permissão |
|--------|----------|-----------|
| GET | `/api/tarefas/manutencao/{id}` | ADMINISTRADOR, GERENTE_MANUTENCAO, TECNICO |
| POST | `/api/tarefas` | ADMINISTRADOR, GERENTE_MANUTENCAO |
| PATCH | `/api/tarefas/{id}/estado` | ADMINISTRADOR, GERENTE_MANUTENCAO, TECNICO |

### RF09 – Histórico
| Método | Endpoint | Permissão |
|--------|----------|-----------|
| GET | `/api/historico` | ADMINISTRADOR, GERENTE_STOCK, GERENTE_MANUTENCAO |
| GET | `/api/historico/periodo` | ADMINISTRADOR |

### RF10 – Relatórios
| Método | Endpoint | Permissão |
|--------|----------|-----------|
| GET | `/api/relatorios/dashboard` | ADMINISTRADOR, GERENTES |
| GET | `/api/relatorios/inventario` | ADMINISTRADOR, GERENTE_STOCK |
| GET | `/api/relatorios/manutencoes?inicio=&fim=` | ADMINISTRADOR, GERENTE_MANUTENCAO |
| GET | `/api/relatorios/movimentos-stock?inicio=&fim=` | ADMINISTRADOR, GERENTE_STOCK |
| GET | `/api/relatorios/utilizadores` | ADMINISTRADOR |

---

## Perfis disponíveis
| Perfil | Acesso |
|--------|--------|
| `ADMINISTRADOR` | Acesso total |
| `GERENTE_STOCK` | Inventário, Fornecedores, Stock, Relatórios |
| `GERENTE_MANUTENCAO` | Manutenções, Tarefas, Máquinas, Relatórios |
| `TECNICO` | Visualização e execução de tarefas |

---

## Estrutura do Projeto
```
src/
└── main/
    ├── java/com/cima/system/
    │   ├── config/          # SecurityConfig, OpenApiConfig, DataInitializer
    │   ├── controller/      # 12 REST Controllers
    │   ├── dto/
    │   │   ├── request/     # DTOs de entrada (com validação)
    │   │   └── response/    # DTOs de saída
    │   ├── entity/          # 10 Entidades JPA
    │   ├── enums/           # EstadoManutencao, EstadoTarefa, TipoMovimento, ...
    │   ├── exception/       # GlobalExceptionHandler, ResourceNotFoundException, ...
    │   ├── repository/      # 10 JPA Repositories
    │   ├── security/        # JwtUtil, JwtAuthenticationFilter, UserDetailsServiceImpl
    │   └── service/
    │       ├── *Service.java        # Interfaces
    │       └── impl/*ServiceImpl.java  # Implementações
    └── resources/
        ├── application.properties
        └── schema.sql
```
