package model;

import java.io.Serializable;

public abstract class BusinessEntity implements Serializable {

	private final Long id;
	private final String name;
	private final String description;
	
	public BusinessEntity(Long id, String name, String description) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}
	
	@Override
	public String toString() {
		return name + " - " + description;
	}
	
}
