package client.ui.swing;

import java.io.Serializable;

import javax.swing.JTable;

/**
 * Classe criada para utilizar a 6ª coluna da tabela
 * como CheckBox. 
 * <b>Caso a coluna mude, é necessário mudar o valor
 * na checagem do if. A contagem da coluna inicia-se em 0</b> 
 * @author romuloponciano
 *
 */
public class TableWithCheckBox extends JTable implements Serializable {

	private static final long serialVersionUID = -3773735955762757821L;
	
    @SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
    public Class getColumnClass(int column) {
        if(column == 5) return Boolean.class;
        return String.class;
    };
      
}