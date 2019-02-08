package model;

import java.sql.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Functionality {
	private int id;
	private String nome;
	private String descricao;
	private Calendar dataCriacao;
	
	public Functionality(int id, String nome, String descricao, Date dataCriacao) {
		this.id = id;
		this.nome = nome;
		this.descricao = descricao;
		this.dataCriacao = new GregorianCalendar();
		this.dataCriacao.setTime(dataCriacao);
	}
	
	public int getId() {
		return id;
	}
	public String getNome() {
		return nome;
	}
	public String getDescricao() {
		return descricao;
	}
	public Calendar getDataCriacao() {
		return dataCriacao;
	}
	
	
}
