package ui;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import client.Client;
import common.Const;
import exception.ServerServiceException;
import exception.UICheckFieldException;
import model.BusinessEntity;
import model.Plugin;
import net.miginfocom.swing.MigLayout;
import ui.UIEnums.FORM_CONTEXT;

public class AbaPlugin extends AbaGenerica {

	private static final long serialVersionUID = -8445952309777454337L;
	
	private JTextField txtNomePlugin = new JTextField(15);
	private JTextField txtDescricao = new JTextField(15);
	private UtilDateModel dateModel = new UtilDateModel();
	private Properties dateProperties = new Properties();
	private JDatePanelImpl datePanel = new JDatePanelImpl(dateModel, dateProperties);
	private JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
	
	public AbaPlugin(JFrame parentFrame) {
		super(parentFrame);
		initPnlForm();
	}
	
	@Override
	public void initPnlForm() {
		JPanel pnlForm = new JPanel(new MigLayout("","[right][grow]",""));
		pnlForm.add(new JLabel("Nome: "));
		pnlForm.add(txtNomePlugin, "wrap, growx");
		pnlForm.add(new JLabel("Descrição: "));
		pnlForm.add(txtDescricao, "wrap, growx");
		pnlForm.add(new JLabel("Data de criação: "));
		datePicker.getComponent(1).setEnabled(false); // setar datePicker disabled
		pnlForm.add(datePicker, "wrap, growx");
		registerForm(pnlForm);
	}
	
	@Override
	public Vector<String> createItemsCmbConsulta() {
		Vector<String> out = new Vector<String>();
		out.add(UIEnums.FILTROS_PLUGIN.Nome.toString());
		out.add(UIEnums.FILTROS_PLUGIN.Descrição.toString());
		out.add(UIEnums.FILTROS_PLUGIN.Data.toString());
		return out;
	}
	
	@Override
	public Vector<String> gerarHeaderTabelaResultado() {
		Vector<String> header = new Vector<String>();
		header.add("ID");
		header.add("NOME");
		header.add("DESCRIÇÃO");
		header.add("DATA DE CRIAÇÃO");
		return header;
	}
	
	@Override
	public void loadData() throws RemoteException, ServerServiceException, NotBoundException {
		popularTabelaResultado(Client.getServer().getPlugins());
		setContext(FORM_CONTEXT.Proibido);
	}

	@Override
	public boolean checkFieldsOnCreate() throws UICheckFieldException {
		String campo = this.txtNomePlugin.getText();
		if(campo == null || campo.length() <= 0) throw new UICheckFieldException(Const.INFO_EMPTY_FIELD.replace("?", "nome"));
		return true;
	}	
	
	@Override
	public String converComboChoiceToDBAtributte(String cmbChoice) {
		if(cmbChoice.equals(UIEnums.FILTROS_PLUGIN.Nome.toString())) return UIEnums.FILTROS_PLUGIN.Nome.getValue();
		if(cmbChoice.equals(UIEnums.FILTROS_PLUGIN.Descrição.toString())) return UIEnums.FILTROS_PLUGIN.Descrição.getValue();
		if(cmbChoice.equals(UIEnums.FILTROS_PLUGIN.Data.toString())) return UIEnums.FILTROS_PLUGIN.Data.getValue();
		return "";
	}
	
	@Override
	public void popularTabelaResultado(List<? extends BusinessEntity> objs) {
		Vector<Vector<Object>> dadosFinal = new Vector<Vector<Object>>();
		for(Object obj: objs) {
			Plugin plg = (Plugin)obj;
			Vector<Object> linha = new Vector<Object>();
			linha.add(plg.getId());
			linha.add(plg.getName());
			linha.add(plg.getDescription());
			linha.add(plg.getDataCriacaoToString()); 
			dadosFinal.add(linha);
		};
		this.getTblResultado().setModel(new DefaultTableModel(dadosFinal, gerarHeaderTabelaResultado()));
		setJTableColumnInsivible(this.getTblResultado(), 0);
	}

	@Override
	public List<? extends BusinessEntity> realizarBusca(String atributo, String termo) throws RemoteException, ServerServiceException, NotBoundException {
		return Client.getServer().searchPlugins(atributo, termo);
	}

	@Override
	public void realizarDelete(Long id) throws RemoteException, ServerServiceException, NotBoundException {
		Client.getServer().deletePlugin(id);
	}
	
	@Override
	public void realizarCreate(BusinessEntity objToSave) throws RemoteException, ServerServiceException, NotBoundException {
		Client.getServer().createPlugin((Plugin)objToSave);
	}

	@Override
	public void realizarUpdate(BusinessEntity objToSave) throws RemoteException, ServerServiceException, NotBoundException {
		Client.getServer().updatePlugin((Plugin)objToSave);
	}

	@Override
	public BusinessEntity createObjToBeSaved() {
		String name = txtNomePlugin.getText();
		String desc = txtDescricao.getText();
		Plugin plg = new Plugin(null, name, desc, null);
		return plg;
	}

	@Override
	public void clearForm() {
		txtNomePlugin.setText("");
		txtDescricao.setText("");
		setDataModelFromStringDate(dateModel, Const.DATA_FORMAT.format(new Date()));
	}

	@Override
	public void fillFormToEdit(int selectedRowToEdit) throws RemoteException, ServerServiceException, NotBoundException {
		Object campo =  getTblResultado().getValueAt(selectedRowToEdit, 1);
		txtNomePlugin.setText(( campo != null ? campo.toString() : ""));
		campo =  getTblResultado().getValueAt(selectedRowToEdit, 2);
		txtDescricao.setText(( campo != null ? campo.toString() : ""));
		String data = getTblResultado().getValueAt(selectedRowToEdit, 3).toString();
		if(!data.equals("")) setDataModelFromStringDate(dateModel, data);
		else dateModel.setSelected(false); 
	}

	@Override
	public void setEnabledForm(boolean setar) {
		txtNomePlugin.setEnabled(setar);
		txtDescricao.setEnabled(setar);
	}
}
