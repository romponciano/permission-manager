package client.ui.swing;

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

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import client.Client;
import client.exceptions.UICheckFieldException;
import client.ui.UIEnums;
import client.ui.UIEnums.FORM_CONTEXT;
import common.Const;
import common.exceptions.ServerServiceException;
import common.model.BusinessEntity;
import common.model.Functionality;
import common.model.Plugin;
import net.miginfocom.swing.MigLayout;

public class AbaFuncionalidade extends AbaGenerica {

	private static final long serialVersionUID = -8445952309777454337L;
	
	private ComboBoxWithItems cmbPlugin = new ComboBoxWithItems();
	private JTextField txtNomeFunc = new JTextField(15);
	private JTextField txtDescricao = new JTextField(15);
	private UtilDateModel dateModel = new UtilDateModel();
	private Properties dateProperties = new Properties();
	private JDatePanelImpl datePanel = new JDatePanelImpl(dateModel, dateProperties);
	private JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
	
	public AbaFuncionalidade(JFrame parentFrame) {
		super(parentFrame);
		initPnlForm();
	}
	
	@Override
	public void initPnlForm() {
		JPanel pnlForm = new JPanel(new MigLayout("","[right][grow]",""));
		pnlForm.add(new JLabel("Plugin: "));
		pnlForm.add(cmbPlugin, "wrap, growx");
		pnlForm.add(new JLabel("Nome: "));
		pnlForm.add(txtNomeFunc, "wrap, growx");
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
		out.add(UIEnums.FILTROS_FUNCIONALIDADE.Plugin.toString());
		out.add(UIEnums.FILTROS_FUNCIONALIDADE.Nome.toString());
		out.add(UIEnums.FILTROS_FUNCIONALIDADE.Descrição.toString());
		out.add(UIEnums.FILTROS_FUNCIONALIDADE.Data.toString());
		return out;
	}
	
	@Override
	public Vector<String> gerarHeaderTabelaResultado() {
		Vector<String> header = new Vector<String>();
		header.add("ID");
		header.add("NOME");
		header.add("DESCRIÇÃO");
		header.add("DATA DE CRIAÇÃO");
		header.add("PLUGIN");
		return header;
	}
	
	@Override
	public void loadData() throws RemoteException, ServerServiceException, NotBoundException {
		atualizarCacheTodasFuncBanco();
		popularTabelaResultado(Client.getServer().getFunctionalities());
		cmbPlugin.popularFromBusinessEntity(Client.getServer().getPlugins());
		setContext(FORM_CONTEXT.Proibido);
	}

	@Override
	public boolean checkFieldsOnCreate() throws UICheckFieldException {
		String campo = this.txtNomeFunc.getText();
		if(campo == null || campo.length() <= 0) throw new UICheckFieldException(Const.INFO_EMPTY_FIELD.replace("?", "nome"));
		if(this.cmbPlugin.getSelectedItem() == null) throw new UICheckFieldException(Const.INFO_EMPTY_FIELD.replace("?", "plugin"));
		return true;
	}	
	
	@Override
	public String converComboChoiceToDBAtributte(String cmbChoice) {
		if(cmbChoice.equals(UIEnums.FILTROS_FUNCIONALIDADE.Nome.toString())) return UIEnums.FILTROS_FUNCIONALIDADE.Nome.getValue();
		if(cmbChoice.equals(UIEnums.FILTROS_FUNCIONALIDADE.Descrição.toString())) return UIEnums.FILTROS_FUNCIONALIDADE.Descrição.getValue();
		if(cmbChoice.equals(UIEnums.FILTROS_FUNCIONALIDADE.Data.toString())) return UIEnums.FILTROS_FUNCIONALIDADE.Data.getValue();
		if(cmbChoice.equals(UIEnums.FILTROS_FUNCIONALIDADE.Plugin.toString())) return UIEnums.FILTROS_FUNCIONALIDADE.Plugin.getValue();
		return "";
	}

	@Override
	public List<? extends BusinessEntity> realizarBusca(String atributo, String termo) throws RemoteException, ServerServiceException, NotBoundException {
		return Client.getServer().searchFunctionalities(atributo, termo);
	}

	@Override
	public void realizarDelete(Long id) throws RemoteException, ServerServiceException, NotBoundException {
		Client.getServer().deleteFunctionality(id);
	}
	
	@Override
	public void realizarCreate(BusinessEntity objToSave) throws RemoteException, ServerServiceException, NotBoundException {
		Client.getServer().createFunctionality((Functionality)objToSave);
		atualizarCacheTodasFuncBanco();
	}

	@Override
	public void realizarUpdate(BusinessEntity objToSave) throws RemoteException, ServerServiceException, NotBoundException {
		Client.getServer().updateFunctionality((Functionality)objToSave);
	}

	@Override
	public BusinessEntity createObjToBeSaved() {
		Long pluginId = cmbPlugin.getIdFromSelectedItem();
		String name = txtNomeFunc.getText();
		String desc = txtDescricao.getText();
		Functionality func = new Functionality(null, name, desc, null);
		func.setPlugin(new Plugin(pluginId, null, null, null));
		return func;
	}

	@Override
	public void clearForm() {
		txtNomeFunc.setText("");
		txtDescricao.setText("");
		setDataModelFromStringDate(dateModel, Const.DATA_FORMAT.format(new Date()));
	}

	@Override
	public void fillFormToEdit(int selectedRowToEdit) throws RemoteException, ServerServiceException, NotBoundException {
		Object campo =  getTblResultado().getValueAt(selectedRowToEdit, 1);
		txtNomeFunc.setText(( campo != null ? campo.toString() : ""));
		campo =  getTblResultado().getValueAt(selectedRowToEdit, 2);
		txtDescricao.setText(( campo != null ? campo.toString() : ""));
		String data = getTblResultado().getValueAt(selectedRowToEdit, 3).toString();
		if(!data.equals("")) setDataModelFromStringDate(dateModel, data);
		else dateModel.setSelected(false);
		// como pluginId é estrangeira e obrigatória, então não checar se é null
		BusinessEntity pluginSelecionado = (BusinessEntity) getTblResultado().getValueAt(selectedRowToEdit, 4);
		cmbPlugin.setSelectedItemById(pluginSelecionado.getId());
	}

	@Override
	public void setEnabledForm(boolean setar) {
		cmbPlugin.setEnabled(setar);
		txtNomeFunc.setEditable(setar);
		txtDescricao.setEditable(setar);	
	}

	@Override
	public Vector<Object> generateJTableLine(Object obj) {
		Functionality func = (Functionality)obj;
		Vector<Object> linha = new Vector<Object>();
		linha.add(func.getId());
		linha.add(func.getName());
		linha.add(func.getDescription());
		linha.add(func.getDataCriacaoToString());
		linha.add(func.getPlugin());
		return linha;
	}
}
