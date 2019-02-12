package model;

import java.sql.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Plugin {
	private int id;
	private String nome;
	private String descricao;
	private String dataCriacao;
	
	public Plugin(int id, String nome, String descricao, String dataCriacao) {
		this.id = id;
		this.nome = nome;
		this.descricao = descricao;
		//this.dataCriacao = new GregorianCalendar();
		//this.dataCriacao.setTime(dataCriacao);
		this.dataCriacao = dataCriacao;
	}
	public Plugin() {	}
	
	public int getId() {
		return id;
	}
	public String getNome() {
		return nome;
	}
	public String getDescricao() {
		return descricao;
	}
	public String getDataCriacao() {
		return dataCriacao;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public void setDataCriacao(String data) {
		this.dataCriacao = data;
	}
	
	
}
