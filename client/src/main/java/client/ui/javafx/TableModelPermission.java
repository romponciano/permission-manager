package client.ui.javafx;

import java.util.Date;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import common.model.Functionality;
import common.model.Plugin;

/**
 * Classe usada para popular tabela de permissões que é
 * apresentada na aba Perfil. Esta tabela é responsável por 
 * permitir que o usuário set/unset as funcionalidades que 
 * um perfil terá.
 * @author romuloponciano
 *
 */
public class TableModelPermission extends Functionality {
	
	private static final long serialVersionUID = 3596435184590585958L;
	
	private BooleanProperty checked;
	
	public TableModelPermission(Long id, String name, String description, Date dataCriacao, Plugin plugin) {
		super(id, name, description, dataCriacao);
		this.setPlugin(plugin);
		this.checked = new SimpleBooleanProperty(false);
	}
	
	public TableModelPermission(Functionality func, Boolean value) {
		super(func.getId(), func.getName(), func.getDescription(), func.getDataCriacao());
		this.setPlugin(func.getPlugin());
		this.checked = new SimpleBooleanProperty(value);
	}
	
	
	public BooleanProperty getChecked() {
        return checked;
    }
	
	/**
	 *  <b>Não</b> editar pois este método, pois ele é usado pelo 
	 *  cellfactory para construir a coluna de checkbox e setar 
	 *  seus valores corretamente.
	 * @return
	 */
	public BooleanProperty checkedProperty() {
        return checked;
    }

    public void setChecked(boolean active) {
        checked.set(active);
    }
}
