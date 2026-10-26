
CREATE DATABASE SistemaChamados;
GO

USE SistemaChamados;
GO

-- 1. Usuários e seus perfis
CREATE TABLE Usuarios (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    nome NVARCHAR(150) NOT NULL,
    email NVARCHAR(200) NOT NULL UNIQUE,
    senha_hash NVARCHAR(255) NOT NULL,
    perfil VARCHAR(20) NOT NULL,
    ativo BIT NOT NULL DEFAULT 1,
    data_criacao DATETIME2 NOT NULL DEFAULT SYSDATETIME(),

    CONSTRAINT CK_Usuarios_Perfil
        CHECK (perfil IN ('CIDADAO', 'SABESP', 'PREFEITURA', 'ADMIN'))
);
GO

-- 2. Tipos de ocorrência
CREATE TABLE TiposChamado (
    id INT IDENTITY(1,1) PRIMARY KEY,
    nome NVARCHAR(100) NOT NULL UNIQUE,
    descricao NVARCHAR(300),
    ativo BIT NOT NULL DEFAULT 1
);
GO

-- 3. Chamados registrados
CREATE TABLE Chamados (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    tipo_id INT NOT NULL,
    endereco NVARCHAR(300) NOT NULL,
    numero NVARCHAR(20),
    bairro NVARCHAR(100),
    cidade NVARCHAR(100) NOT NULL,
    cep VARCHAR(10),
    descricao NVARCHAR(MAX),
    data_hora DATETIME2 NOT NULL DEFAULT SYSDATETIME(),
    status VARCHAR(30) NOT NULL DEFAULT 'ABERTO',
    imagem_url NVARCHAR(1000),

    CONSTRAINT FK_Chamados_Usuarios
        FOREIGN KEY (usuario_id) REFERENCES Usuarios(id),

    CONSTRAINT FK_Chamados_Tipos
        FOREIGN KEY (tipo_id) REFERENCES TiposChamado(id),

    CONSTRAINT CK_Chamados_Status
        CHECK (status IN (
            'ABERTO',
            'EM_ANALISE',
            'EM_ATENDIMENTO',
            'RESOLVIDO',
            'CANCELADO'
        ))
);
GO

-- 4. Comentários dos atendimentos
CREATE TABLE Comentarios (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    chamado_id BIGINT NOT NULL,
    usuario_id BIGINT NOT NULL,
    comentario NVARCHAR(2000) NOT NULL,
    data_hora DATETIME2 NOT NULL DEFAULT SYSDATETIME(),

    CONSTRAINT FK_Comentarios_Chamados
        FOREIGN KEY (chamado_id) REFERENCES Chamados(id),

    CONSTRAINT FK_Comentarios_Usuarios
        FOREIGN KEY (usuario_id) REFERENCES Usuarios(id)
);
GO

-- 5. Auditoria das alterações de status
CREATE TABLE HistoricoStatus (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    chamado_id BIGINT NOT NULL,
    usuario_id BIGINT NOT NULL,
    status_anterior VARCHAR(30),
    status_novo VARCHAR(30) NOT NULL,
    data_hora DATETIME2 NOT NULL DEFAULT SYSDATETIME(),
    observacao NVARCHAR(1000),

    CONSTRAINT FK_Historico_Chamados
        FOREIGN KEY (chamado_id) REFERENCES Chamados(id),

    CONSTRAINT FK_Historico_Usuarios
        FOREIGN KEY (usuario_id) REFERENCES Usuarios(id)
);
GO

-- Tipos iniciais de ocorrência
INSERT INTO TiposChamado (nome, descricao)
VALUES
    ('VAZAMENTO', 'Vazamento de água'),
    ('FALTA_DAGUA', 'Interrupção no abastecimento'),
    ('ESGOTO', 'Problemas na rede de esgoto'),
    ('BURACO', 'Buraco ou dano no pavimento'),
    ('OUTRO', 'Outras ocorrências');
GO

-- Índices para facilitar as consultas e os filtros
CREATE INDEX IX_Chamados_Status
    ON Chamados(status);

CREATE INDEX IX_Chamados_DataHora
    ON Chamados(data_hora);

CREATE INDEX IX_Chamados_Tipo
    ON Chamados(tipo_id);

CREATE INDEX IX_Chamados_CidadeBairro
    ON Chamados(cidade, bairro);
GO



select * from TiposChamado;