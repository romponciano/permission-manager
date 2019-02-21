package common.model;

import java.util.List;

public class User extends BusinessEntity {
	
	private static final long serialVersionUID = -311656302967427533L;
	
	private String login;
	private Status status;
	private String gerenciaAtual;
	private List<Perfil> perfis;

	public User(Long id, String name) {	
		super(id, name, null);
	}

	public String getLogin() {
		return login;
	}

	public Status getStatus() {
		return status;
	}

	public String getGerenciaAtual() {
		return gerenciaAtual;
	}
	
	public List<Perfil> getPerfis() {
		return perfis;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public void setGerenciaAtual(String gerenciaAtual) {
		this.gerenciaAtual = gerenciaAtual;
	}

	public void setPerfis(List<Perfil> perfis) {
		this.perfis = perfis;
	}
	
	
}
