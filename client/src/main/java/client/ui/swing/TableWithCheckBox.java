package client.ui.swing;

import java.io.Serializable;

import javax.swing.JTable;

public class TableWithCheckBox extends JTable implements Serializable {

	private static final long serialVersionUID = -3773735955762757821L;
	
    /*@Override
    public Class getColumnClass(int column) {
    return getValueAt(0, column).getClass();
    }*/
    @SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
    public Class getColumnClass(int column) {
        if(column == 5) return Boolean.class;
        return String.class;
    };
      
}