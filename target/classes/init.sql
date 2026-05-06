-- =========================================================
-- CIMA – Sistema de Gestão Integrado de Manutenção Mecânica
-- Script de criação da base de dados MySQL
-- =========================================================

CREATE DATABASE IF NOT EXISTS cima_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE cima_db;

-- O Hibernate (spring.jpa.hibernate.ddl-auto=update) cria as
-- tabelas automaticamente. Este script cria apenas a BD e
-- pode ser usado para recriar do zero em ambiente de produção.

-- Para ambiente de produção, altere ddl-auto para 'validate'
-- e utilize Flyway ou Liquibase para migrations.
