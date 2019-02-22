-- executar container com oracle 18
-- https://github.com/fuzziebrain/docker-oracle-xe

-- criando usuário
create user ROMULOPONCIANO identified by root;

-- garantindo privilégios
grant DBA to ROMULOPONCIANO;

