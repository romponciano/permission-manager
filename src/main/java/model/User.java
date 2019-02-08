package model;

import java.io.Serializable;

public class User implements Serializable {
	private static final long serialVersionUID = -311656302967427533L;
	
	private Integer id;
	private String nome;
	private String login;
	private Integer status;
	private String gerenciaAtual;
	
	public User(Integer id, String nome, String login, Integer status, String gerenciaAtual) {
		this.id = id;
		this.nome = nome;
		this.login = login;
		this.status = status;
		this.gerenciaAtual = gerenciaAtual;
	}

	public Integer getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public String getLogin() {
		return login;
	}

	public Integer getStatus() {
		return status;
	}

	public String getGerenciaAtual() {
		return gerenciaAtual;
	}
	
	
}
