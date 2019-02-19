package model;

import java.util.Date;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import common.Const;

public class Perfil implements Serializable {
	private static final long serialVersionUID = -2136312367285871901L;
	
	private int id;
	private String nome;
	private String descricao;
	private Calendar dataCriacao;
	private List<Permission> permissoes;
	
	public Perfil() { 
		permissoes = new ArrayList<Permission>();
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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

	public List<Permission> getPermissoes() {
		return permissoes;
	}

	public void setPermissoes(List<Permission> permissoes) {
		this.permissoes = permissoes;
	}
	
}
