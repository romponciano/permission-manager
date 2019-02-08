package model;

import java.io.Serializable;

public class Log implements Serializable {
	
	private static final long serialVersionUID = 707430661651842116L;
	
	private int id;
	private String tipo;
	private String mensagem;
	private String causa;
	
	public static enum TIPOS_LOG {
			SUCESSO,
			ERRO,
			INFO,
			DANGER
	}

	public int getId() {
		return id;
	}

	public String getTipo() {
		return tipo;
	}

	public String getMessage() {
		return mensagem;
	}
	
	public String getCausa() {
		return causa;
	}

	public Log(int id, String tipo, String mensagem, String causa) {
		this.id = id;
		this.tipo = tipo;
		this.mensagem = mensagem;
		this.causa = causa;
	}
}
