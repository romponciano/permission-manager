-- criar base de dados
CREATE TABLE Status (
	id int PRIMARY KEY AUTO_INCREMENT,
	nome varchar(255) NOT NULL
)

CREATE TABLE Usuario (
	id int PRIMARY KEY AUTO_INCREMENT,
	login varchar(4) NOT NULL,
	nomeCompleto varchar(255),
	status bool NOT NULL,
	gerenciaAtual varchar(255)
	FOREIGN KEY (status) REFERENCES Status(id)
)

CREATE TABLE Plugin (
	id int PRIMARY KEY AUTO_INCREMENT,
	nome varchar(255),
	descricao varchar(255),
	dataCriacao date
)

-- confirmar se 1 funcionalidade só pode pertencer a 1 plugin
CREATE TABLE Funcionalidade (
	id int PRIMARY KEY AUTO_INCREMENT,
	nome varchar(255),
	descricao varchar(255),
	dataCriacao date
	pluginId int NOT NULL,
	FOREIGN KEY (pluginId) REFERENCES Plugin(id)
)
