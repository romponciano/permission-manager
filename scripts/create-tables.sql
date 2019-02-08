-- criar base de dados
CREATE TABLE STATUS (
	id 	INT PRIMARY KEY,
	nome 	VARCHAR(255) NOT NULL
);

CREATE TABLE USUARIO (
	id 		INT PRIMARY KEY,
	login 		VARCHAR(4) NOT NULL,
	nomeCompleto	VARCHAR(255),
	status		INT NOT NULL,
	gerenciaAtual 	VARCHAR(255),
	FOREIGN KEY (status) REFERENCES STATUS(id)
);

CREATE TABLE PLUGIN (
	id 		INT PRIMARY KEY,
	nome 		VARCHAR(255),
	descricao 	VARCHAR(255),
	dataCriacao 	DATE
);

-- confirmar se 1 funcionalidade só pode pertencer a 1 plugin
CREATE TABLE Funcionalidade (
	id 		INT PRIMARY KEY,
	nome 		VARCHAR(255),
	descricao 	VARCHAR(255),
	dataCriacao 	DATE,
	pluginId 	INT NOT NULL,
	FOREIGN KEY (pluginId) REFERENCES PLUGIN(id)
);

CREATE TABLE LOG(
	id 		INT PRIMARY KEY, 
	tipo 		VARCHAR(255), 
	mensagem	VARCHAR(255),
	causa		VARCHAR(255),
);

