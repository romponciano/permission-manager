package model;

import java.io.Serializable;

public class Permission implements Serializable {

	private static final long serialVersionUID = 1889917694425161774L;
	
	private int id;
	private Perfil perfil;
	private Functionality func;
	
	public Permission() {
		this.perfil = new Perfil();
		this.func = new Functionality();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Functionality getFunc() {
		return func;
	}

	public void setFunc(Functionality func) {
		this.func = func;
	}

	public Perfil getPerfil() {
		return perfil;
	}

	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	};
	
}
