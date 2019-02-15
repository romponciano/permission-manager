package model;

import java.io.Serializable;

public class Status implements Serializable {

	private static final long serialVersionUID = -3875636949537998820L;
	
	private int id;
	private String nome;
	
	public Status() {}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	@Override
	public String toString() {
		return this.id + "-" + this.nome;
	}
}
