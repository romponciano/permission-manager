package ui;

import java.awt.Color;
import java.io.Serializable;
import java.util.List;

import javax.swing.JComboBox;

import model.BusinessEntity;

public class ComboBoxWithItems extends JComboBox<BusinessEntity> implements Serializable {
	
	private static final long serialVersionUID = -6497993382058846123L;

    public ComboBoxWithItems() { 
    	this.setBackground(Color.WHITE);
    }
    
    public BusinessEntity getSelectedComboBoxItem() {
    	return (BusinessEntity) this.getSelectedItem();
    }
    
    /**
	 * Método para selecionar item na combobox pelo id do objeto
	 * @param cmb - combobox com a lista dos items
	 * @param id - id do item a ser selecionado
	 */
    public void setSelectedItemById(Long id) {
    	for(int i=0; i < this.getItemCount(); i++) {
			Long tempId = this.getItemAt(i).getId();
			if(tempId.equals(id)) this.setSelectedIndex(i);
		}
    }
    
    /**
	 * Método para preencher combobox a partir de uma lista de String.
	 * @param JComboBox<String> - combobox que terá a lista add
	 * @param List<String> - lista com os valores a serem add
	 */
	public void popularFromBusinessEntity(List<? extends BusinessEntity> valores) {
		this.removeAllItems();
		int index = 0;
		for (BusinessEntity valor : valores) {
			insertItemAt(valor, index++);
		}
	}
	
	/**
	 * Método para pegar o id de um item selecionado em combobox 
	 * que foi gerada com o método popularComboBoxFromStringList
	 * Ex: 23-Plugin-X213, vai retornar 23 pois é o ID deste plugin
	 * @param item - String com valor do item selecionado
	 * @return id no bd do item selecionado
	 */
	public Long getIdFromSelectedItem() {
		return getSelectedComboBoxItem().getId();
	}
}
