package client.ui.swing;

import java.util.Vector;

import javax.swing.table.DefaultTableModel;

public class TableModelWithoutEdition extends DefaultTableModel {

	private static final long serialVersionUID = -6931335427247490317L;

	public TableModelWithoutEdition(Vector<Vector<Object>> dadosFinal, Vector<String> gerarHeaderTabelaResultado) {
		super(dadosFinal, gerarHeaderTabelaResultado);
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return false;
	}
}
