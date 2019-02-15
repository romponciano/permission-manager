package model;

import java.io.Serializable;

public class User implements Serializable {
	private static final long serialVersionUID = -311656302967427533L;
	
	private Integer id;
	private String nome;
	private String login;
	private Status status;
	private String gerenciaAtual;

	public User() {	
		this.status = new Status();
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

	public Status getStatus() {
		return status;
	}

	public String getGerenciaAtual() {
		return gerenciaAtual;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public void setGerenciaAtual(String gerenciaAtual) {
		this.gerenciaAtual = gerenciaAtual;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	
	
}
