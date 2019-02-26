package client.ui.javafx;

import java.util.Date;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import common.model.Functionality;
import common.model.Plugin;

public class TableModelPermission extends Functionality {
	
	private static final long serialVersionUID = 3596435184590585958L;
	
	private BooleanProperty checked;
	
	public TableModelPermission(Long id, String name, String description, Date dataCriacao, Plugin plugin) {
		super(id, name, description, dataCriacao);
		this.setPlugin(plugin);
		this.checked = new SimpleBooleanProperty(false);
	}
	
	public BooleanProperty getChecked() {
		return checked;
	}
	public void setChecked(BooleanProperty checked) {
		this.checked = checked;
	}
}
