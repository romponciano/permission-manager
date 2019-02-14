package model;

import java.io.Serializable;
import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import common.Const;

public class Plugin implements Serializable {
	
	private static final long serialVersionUID = -7860126305371589573L;
	
	private int id;
	private String nome;
	private String descricao;
	private Calendar dataCriacao;
	
	public Plugin(int id, String nome, String descricao, Calendar data) {
		this.id = id;
		this.nome = nome;
		this.descricao = descricao;
		this.dataCriacao = data;
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
	public Calendar getDataCriacao() {
		return dataCriacao;
	}
	public String getDataCriacaoToString() {
		if(dataCriacao == null) return "";
		return Const.DATA_FORMAT.format(dataCriacao.getTime());
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public void setDataCriacao(Calendar data) {
		this.dataCriacao = data;
	}
	public void setDataCriacaoFromDate(Date data) {
		if(data != null) {
			this.dataCriacao = new GregorianCalendar();
			this.dataCriacao.setTime(data);
		}
	}
	public void setId(int id) {
		this.id = id;
	}
}
