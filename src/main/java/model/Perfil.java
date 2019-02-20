package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import common.Const;

public class Perfil extends BusinessEntity {
	private static final long serialVersionUID = -2136312367285871901L;
	
	private Date dataCriacao;
	private List<Functionality> permissoes;
	
	public Perfil(Long id, String name, String description, Date dataCriacao) { 
		super(id, name, description);
		this.dataCriacao = dataCriacao;
		permissoes = new ArrayList<Functionality>();
	}
	
	public Date getDataCriacao() {
		return dataCriacao;
	}
	public String getDataCriacaoToString() {
		if(dataCriacao == null) return "";
		return Const.DATA_FORMAT.format(dataCriacao);
	}
	public void setDataCriacao(Date data) {
		this.dataCriacao = data;
	}
	
	public List<Functionality> getPermissoes() {
		return permissoes;
	}
	public void setPermissoes(List<Functionality> permissoes) {
		this.permissoes = permissoes;
	}
	
}
