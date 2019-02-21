package common.model;

import java.util.Date;

import common.Const;

public class Plugin extends BusinessEntity {
		
	private static final long serialVersionUID = 1676130038866855993L;
	
	private Date dataCriacao;
	
	public Plugin(Long id, String nome, String descricao, Date data) {
		super(id, nome, descricao);
		this.dataCriacao = data;
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
}
