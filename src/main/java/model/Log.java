package model;

import java.io.Serializable;

public class Log implements Serializable {
	
	private static final long serialVersionUID = 707430661651842116L;
	
	private int id;
	private String tipo;
	private String mensagem;
	
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

	public Log(int id, String tipo, String mensagem) {
		this.id = id;
		this.tipo = tipo;
		this.mensagem = mensagem;
	}
}
