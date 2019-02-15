package ui;

import java.io.Serializable;
import java.util.List;

import javax.swing.JComboBox;

public class ComboBoxWithItems extends JComboBox<ComboBoxItem> implements Serializable {
	
	private static final long serialVersionUID = -6497993382058846123L;

    public ComboBoxWithItems() { }
    
    private ComboBoxItem getSelectedComboBoxItem() {
    	ComboBoxItem item = (ComboBoxItem) this.getSelectedItem();
    	return item;
    }
    
    /**
	 * Método para selecionar item na combobox pelo id do objeto
	 * @param cmb - combobox com a lista dos items
	 * @param id - id do item a ser selecionado
	 */
    public void setSelectedItemById(int id) {
    	for(int i=0; i < this.getItemCount(); i++) {
			int tempId = this.getItemAt(i).getId();
			if(tempId == id) this.setSelectedIndex(i);
		}
    }
    
    /**
	 * Método para preencher combobox a partir de uma lista de String.
	 * @param JComboBox<String> - combobox que terá a lista add
	 * @param List<String> - lista com os valores a serem add
	 */
	public void popularFromStringList(List<String> valores) {
		this.removeAllItems();
		for(int i=0; i < valores.size(); i++) {
			this.insertItemAt(new ComboBoxItem(valores.get(i)), i);
		}
	}
	
	/**
	 * Método para pegar o id de um item selecionado em combobox 
	 * que foi gerada com o método popularComboBoxFromStringList
	 * Ex: 23-Plugin-X213, vai retornar 23 pois é o ID deste plugin
	 * @param item - String com valor do item selecionado
	 * @return id no bd do item selecionado
	 */
	public int getIdFromSelectedItem() {
		return getSelectedComboBoxItem().getId();
	}
}
