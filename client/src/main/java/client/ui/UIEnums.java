package client.ui;

public class UIEnums {
	
	public enum FILTROS_USUARIO {
		Login("login"),
		Nome("nomeCompleto"),
		Status("status"),
		Gerência("gerenciaAtual");
		
		private final String value;
	    FILTROS_USUARIO(String value) { this.value = value; }
	    public String getValue() { return this.value; }
	}
	
	public enum FILTROS_PLUGIN {
		Nome("nome"),
		Descrição("descricao"),
		Data("dataCriacao");
		
		private final String value;
	    FILTROS_PLUGIN(String value) { this.value = value; }
	    public String getValue() { return this.value; }
	}
	
	public enum FILTROS_FUNCIONALIDADE {
		Plugin("pluginId"),
		Nome("nome"),
		Descrição("descricao"),
		Data("dataCriacao");
		
		private final String value;
	    FILTROS_FUNCIONALIDADE(String value) { this.value = value; }
	    public String getValue() { return this.value; }
	}
	
	public enum FILTROS_PERFIL {
		Nome("nome"),
		Descrição("descricao"),
		Data("dataCriacao");
		
		private final String value;
	    FILTROS_PERFIL(String value) { this.value = value; }
	    public String getValue() { return this.value; }
	}
	
	public enum ABAS {
		Usuário, Plugin, Funcionalidade, Perfil
	}
	
	public enum FORM_CONTEXT {
		Criar, Editar, Proibido
	}

}
