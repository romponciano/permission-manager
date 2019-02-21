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
import model.Functionality;
import model.Plugin;
import net.miginfocom.swing.MigLayout;

public class AbaFuncionalidade extends AbaGenerica {

	private static final long serialVersionUID = -8445952309777454337L;
	
	private JLabel lblNomePlugin = new JLabel("Plugin: ");
	private JLabel lblNomeFunc = new JLabel("Nome: ");
	private JLabel lblDescricao = new JLabel("Descrição: ");
	private JLabel lblDataCriacao = new JLabel("Data de criação: ");
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
		setContextoEditar(false);
	}
	
	@Override
	public void initPnlForm() {
		JPanel pnlForm = new JPanel(new MigLayout("","[right][grow]",""));
		pnlForm.add(lblNomePlugin);
		pnlForm.add(cmbPlugin, "wrap, growx");
		pnlForm.add(lblNomeFunc);
		pnlForm.add(txtNomeFunc, "wrap, growx");
		pnlForm.add(lblDescricao);
		pnlForm.add(txtDescricao, "wrap, growx");
		pnlForm.add(lblDataCriacao);
		datePicker.getComponent(1).setEnabled(false); // setar datePicker disabled
		
		pnlForm.add(datePicker, "wrap, growx");
		registerForm(pnlForm);
	}
	
	@Override
	public void setContextoCriar(boolean setar) {
		super.setContextoCriar(setar);
		cmbPlugin.setEnabled(setar);
		txtNomeFunc.setText("");
		txtDescricao.setText("");
		setDataModelFromStringDate(dateModel, Const.DATA_FORMAT.format(new Date()));
		cmbPlugin.setEnabled(setar);
		txtNomeFunc.setEditable(setar);
		txtDescricao.setEditable(setar);
	}
	
	@Override
	public void setContextoEditar(boolean setar) {
		super.setContextoEditar(setar);
		if(!setar) {
			txtNomeFunc.setText("");
			txtDescricao.setText("");
		}
		cmbPlugin.setEnabled(setar);
		txtNomeFunc.setEditable(setar);
		txtDescricao.setEditable(setar);
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
		setContextoEditar(false);
		List<? extends BusinessEntity> funcs = Client.getServer().getFunctionalities();
		popularTabelaResultado(funcs);
		List<? extends BusinessEntity> opcoes = Client.getServer().getPlugins();
		atualizarCacheTodasFuncBanco();
		cmbPlugin.popularFromBusinessEntity(opcoes);
	}

	@Override
	public boolean checkFieldsOnCreate() throws UICheckFieldException {
		String campo = this.txtNomeFunc.getText();
		if(campo == null || campo.length() <= 0) {
			throw new UICheckFieldException(Const.INFO_EMPTY_FIELD.replace("?", "nome"));
		}
		if(this.cmbPlugin.getSelectedItem() == null) {
			throw new UICheckFieldException(Const.INFO_EMPTY_FIELD.replace("?", "plugin"));
		}
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
	public void popularTabelaResultado(List<? extends BusinessEntity> objs) {
		Vector<Vector<Object>> dadosFinal = new Vector<Vector<Object>>();
		for(Object obj: objs) {
			Functionality func = (Functionality)obj;
			Vector<Object> linha = new Vector<Object>();
			linha.add(func.getId());
			linha.add(func.getName());
			linha.add(func.getDescription());
			linha.add(func.getDataCriacaoToString());
			linha.add(func.getPlugin());
			dadosFinal.add(linha);
		};
		this.getTblResultado().setModel(new DefaultTableModel(dadosFinal, gerarHeaderTabelaResultado()));
		setJTableColumnInsivible(this.getTblResultado(), 0);
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
	public void setFormEdicao(int linhaSelecionada) {
		// como pluginId é estrangeira e obrigatória, então não checar se é null
		BusinessEntity pluginSelecionado = (BusinessEntity) getTblResultado().getValueAt(linhaSelecionada, 4);
		cmbPlugin.setSelectedItemById(pluginSelecionado.getId());
		Object campo =  getTblResultado().getValueAt(linhaSelecionada, 1);
		txtNomeFunc.setText(( campo != null ? campo.toString() : ""));
		campo =  getTblResultado().getValueAt(linhaSelecionada, 2);
		txtDescricao.setText(( campo != null ? campo.toString() : ""));
		String data = getTblResultado().getValueAt(linhaSelecionada, 3).toString();
		if(!data.equals("")) setDataModelFromStringDate(dateModel, data);
		else dateModel.setSelected(false);
	}
}
