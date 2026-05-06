-- ============================================================
-- CIMA – Sistema de Gestão Integrado
-- Script de Criação do Banco de Dados MySQL
-- ============================================================

CREATE DATABASE IF NOT EXISTS cima_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE cima_db;

-- ============================================================
-- Tabela: perfil (RF01)
-- ============================================================
CREATE TABLE IF NOT EXISTS perfil (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome        VARCHAR(100) NOT NULL UNIQUE,
    descricao   TEXT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- Tabela: utilizador (RF02)
-- ============================================================
CREATE TABLE IF NOT EXISTS utilizador (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome            VARCHAR(100) NOT NULL UNIQUE,
    email           VARCHAR(150) NOT NULL UNIQUE,
    senha           VARCHAR(255) NOT NULL,
    telefone        VARCHAR(20),
    endereco        VARCHAR(255),
    ativo           TINYINT(1) NOT NULL DEFAULT 1,
    id_perfil       BIGINT NOT NULL,
    criado_em       DATETIME,
    atualizado_em   DATETIME,
    CONSTRAINT fk_utilizador_perfil FOREIGN KEY (id_perfil) REFERENCES perfil(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- Tabela: inventario (RF03)
-- ============================================================
CREATE TABLE IF NOT EXISTS inventario (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    codigo          VARCHAR(50) NOT NULL UNIQUE,
    descricao       VARCHAR(255) NOT NULL,
    descricao3      TEXT,
    unidade_base    VARCHAR(20),
    preco_venda1    DECIMAL(15,2),
    preco_venda2    DECIMAL(15,2),
    preco_venda3    DECIMAL(15,2),
    preco           DECIMAL(15,2),
    quantidade      INT NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- Tabela: fornecedor (RF04)
-- ============================================================
CREATE TABLE IF NOT EXISTS fornecedor (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome        VARCHAR(200) NOT NULL,
    nif         VARCHAR(20) UNIQUE,
    telefone    VARCHAR(20),
    email       VARCHAR(150),
    categoria   VARCHAR(100),
    endereco    VARCHAR(255)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- Tabela: fornecimento (Relação Fornecedor <-> Inventário)
-- ============================================================
CREATE TABLE IF NOT EXISTS fornecimento (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_fornecedor   BIGINT NOT NULL,
    id_inventario   BIGINT NOT NULL,
    condicoes       VARCHAR(255),
    preco           VARCHAR(100),
    CONSTRAINT fk_fornecimento_fornecedor  FOREIGN KEY (id_fornecedor) REFERENCES fornecedor(id),
    CONSTRAINT fk_fornecimento_inventario  FOREIGN KEY (id_inventario) REFERENCES inventario(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- Tabela: movimento_stock (RF05)
-- ============================================================
CREATE TABLE IF NOT EXISTS movimento_stock (
    id_movimento    BIGINT AUTO_INCREMENT PRIMARY KEY,
    tipo_movimento  ENUM('ENTRADA','SAIDA','TRANSFERENCIA','AJUSTE') NOT NULL,
    data_movimento  DATETIME,
    documento_ref   VARCHAR(100),
    preco           DECIMAL(15,2),
    quantidade      INT NOT NULL,
    id_inventario   BIGINT NOT NULL,
    CONSTRAINT fk_movimento_inventario FOREIGN KEY (id_inventario) REFERENCES inventario(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- Tabela: tipo_manutencao
-- ============================================================
CREATE TABLE IF NOT EXISTS tipo_manutencao (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome_tipo   VARCHAR(100) NOT NULL UNIQUE,
    descricao   TEXT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- Tabela: maquina_veiculo
-- ============================================================
CREATE TABLE IF NOT EXISTS maquina_veiculo (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    modelo              VARCHAR(150) NOT NULL,
    data_aquisicao      DATE,
    estado              VARCHAR(50),
    matricula_n_serie   VARCHAR(50) UNIQUE,
    tipo                VARCHAR(100),
    inventario_id       BIGINT,
    CONSTRAINT fk_maquina_inventario FOREIGN KEY (inventario_id) REFERENCES inventario(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- Tabela: manutencao (RF06, RF07)
-- ============================================================
CREATE TABLE IF NOT EXISTS manutencao (
    id                      BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_tipo                 VARCHAR(50) NOT NULL,
    data_agendada           DATE,
    data_execucao           DATE,
    id_inventario           BIGINT,
    descricao               TEXT,
    estado                  ENUM('PENDENTE','EM_CURSO','CONCLUIDA','CANCELADA') DEFAULT 'PENDENTE',
    custo                   DECIMAL(15,2),
    id_utilizador           BIGINT,
    tipo_manutencao_id      BIGINT,
    maquina_veiculo_id      BIGINT,
    inventario_id           BIGINT,
    CONSTRAINT fk_manutencao_utilizador     FOREIGN KEY (id_utilizador)      REFERENCES utilizador(id),
    CONSTRAINT fk_manutencao_tipo           FOREIGN KEY (tipo_manutencao_id) REFERENCES tipo_manutencao(id),
    CONSTRAINT fk_manutencao_maquina        FOREIGN KEY (maquina_veiculo_id) REFERENCES maquina_veiculo(id),
    CONSTRAINT fk_manutencao_inventario     FOREIGN KEY (inventario_id)      REFERENCES inventario(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- Tabela: tarefa (RF08)
-- ============================================================
CREATE TABLE IF NOT EXISTS tarefa (
    id_tarefa       BIGINT AUTO_INCREMENT PRIMARY KEY,
    status          ENUM('ABERTA','EM_PROGRESSO','CONCLUIDA','CANCELADA') NOT NULL DEFAULT 'ABERTA',
    descricao       TEXT,
    id_utilizador   BIGINT,
    id_manutencao   BIGINT NOT NULL,
    CONSTRAINT fk_tarefa_utilizador     FOREIGN KEY (id_utilizador) REFERENCES utilizador(id),
    CONSTRAINT fk_tarefa_manutencao     FOREIGN KEY (id_manutencao) REFERENCES manutencao(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- Tabela: historico (RF09)
-- ============================================================
CREATE TABLE IF NOT EXISTS historico (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    descricao       VARCHAR(255) NOT NULL,
    data_execucao   DATETIME,
    id_utilizador   BIGINT,
    id_inventario   BIGINT,
    id_manutencao   BIGINT,
    CONSTRAINT fk_historico_utilizador  FOREIGN KEY (id_utilizador) REFERENCES utilizador(id),
    CONSTRAINT fk_historico_inventario  FOREIGN KEY (id_inventario) REFERENCES inventario(id),
    CONSTRAINT fk_historico_manutencao  FOREIGN KEY (id_manutencao) REFERENCES manutencao(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- Dados iniciais
-- ============================================================
INSERT IGNORE INTO perfil (nome, descricao) VALUES
    ('ADMINISTRADOR',      'Acesso total ao sistema'),
    ('GERENTE_STOCK',      'Gestão de inventário e fornecedores'),
    ('GERENTE_MANUTENCAO', 'Gestão de manutenções e agendamentos'),
    ('TECNICO',            'Execução e acompanhamento de tarefas de manutenção');

INSERT IGNORE INTO tipo_manutencao (nome_tipo, descricao) VALUES
    ('PREVENTIVA',  'Manutenção planeada para prevenir avarias'),
    ('CORRETIVA',   'Manutenção realizada após a ocorrência de avaria'),
    ('PREDITIVA',   'Manutenção baseada em monitorização de condição');
