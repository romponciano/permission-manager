# Gerenciador de permissões para Plug-Ins
1. About
2. How to run
3. License

## 1. About

Seu trabalho será desenvolver uma aplicação em Java com interface gráfica Swing, baseada em uma arquitetura "cliente/servidor" usando RMI. Essa aplicação deve ser capaz de cadastrar, editar e consultar os dados dos plug-Ins, suas funcionalidades e permissões dos usuários. Além disso, é desejável a criação de um conjunto mínimo de testes unitários.

1) Usar Oracle XE (http://www.oracle.com/) como SGBD do seu projeto;

2) Modelar uma solução de banco para armazenar os dados dos usuários, plug-ins, funcionalidades e permissões. 

Inserir através de um script ".sql" os dados da entidade "Usuário":
2.1) Login (4 caracteres);
2.2) Nome completo;
2.3) Status (ativo ou inativo);
2.4) Gerência Atual (não é necessário criar uma tabela de dicionário para essa informação);

3) Com base no projeto "exam.zip" (em anexo), desenvolver o módulo de gerenciamento de permissões para atribuir e remover funcionalidades de plug-ins aos usuários cadastrados. 

Obs.: criar o diretório "scripts" no seu projeto e adicionar os scripts de criação do modelo físico de banco de dados e o script de cadastro dos usuários.

Esse módulo deve possuir os seguintes recursos:

3.1) Consulta, cadastro, edição e exclusão de plug-ins. A entidade Plug-In tem os seguintes atributos:
3.1.1) Nome;
3.1.2) Descrição;
3.1.3) Data de criação;

3.2) Consulta, cadastro, edição e exclusão de funcionalidades dos plug-ins. A entidade Funcionalidade tem os seguintes atributos:
3.2.1) Nome;
3.2.2) Descrição;
3.2.3) Data de criação;

3.3) Consulta de usuários e suas informações. Deve ser possível filtrar e/ou ordenar usuários por:
3.3.1) Login;
3.3.2) Nome completo;
3.3.3) Status;
3.3.4) Gerência Atual;
3.3.5) Plug-Ins;
3.3.6) Funcionalidades;

3.4) Atribuir e remover funcionalidades de plug-ins aos usuários cadastrados.

## 2. How to run

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites
1. JDK 8 161 (https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
```
java version "1.8.0_161"
Java(TM) SE Runtime Environment (build 1.8.0_161-b12)
Java HotSpot(TM) 64-Bit Server VM (build 25.161-b12, mixed mode)
```

2. Apache Maven 3.5.0 (https://maven.apache.org/docs/3.5.0/release-notes.html)

3. Install an Oracle Database (https://www.oracle.com/technetwork/database/database-technologies/express-edition/downloads/index.html) with minVersion >= 12g. 
If you prefer, you could use some docker image with oracle, like https://github.com/fuzziebrain/docker-oracle-xe

4. After install and execute Oracle Database properly, run the scripts inside scripts folder. It will create all tables.

### Getting started

1. Download or clone this repo.

2. Open common/Const.java and change 
```
public static final String DB_URL = "jdbc:oracle:thin:@localhost:32118/XE"; to your DB_URL
public static final String USER = "ROMULOPONCIANO"; to the user created in you DB
public static final String PASS = "root"; to the password of your user
```
Save it.

3. Inside root folder, execute 
```
mvn clean package install
```
And you should get this output in terminal:
```
Reactor Summary:
[INFO] 
[INFO] root ............................................... SUCCESS 
[INFO] common ............................................. SUCCESS 
[INFO] server ............................................. SUCCESS 
[INFO] client ............................................. SUCCESS 
```
This means that all modules (common, server and client) have been compiled and built successfully and in the corret order.

4. Run the server's jar file
```
java -jar server/target/server.....-with-dependencies.jar
```
If you see 
```
Server Ready!
```
Then you are good to go!

5. Run the client's jar file
```
java -jar client/target/client.....-with-dependencies.jar
```

You should see an interface and done! 
