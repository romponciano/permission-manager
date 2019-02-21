package common.model;

import java.util.Date;

import common.Const;

public class Functionality extends BusinessEntity {
	
	private static final long serialVersionUID = -6487065132272259938L;
	
	private Date dataCriacao;
	private Plugin plugin;
	
	public Functionality(Long id, String nome, String descricao, Date data) {
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
	public Plugin getPlugin() {
		return plugin;
	}
	public void setPlugin(Plugin plugin) {
		this.plugin = plugin;
	}
	
}
