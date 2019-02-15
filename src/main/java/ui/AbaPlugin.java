package ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import client.Client;
import common.Const;
import exception.ServerServiceException;
import exception.UICheckFieldException;
import model.Plugin;
import net.miginfocom.swing.MigLayout;

public class AbaPlugin extends AbaGenerica {

	private static final long serialVersionUID = -8445952309777454337L;
	
	private JLabel lblNomePlugin = new JLabel("Plugin: ");
	private JLabel lblDescricao = new JLabel("Descrição: ");
	private JLabel lblDataCriacao = new JLabel("Data de criação: ");
	private JTextField txtNomePlugin = new JTextField(15);
	private JTextField txtDescricao = new JTextField(15);

	private UtilDateModel dateModel = new UtilDateModel();
	private Properties dateProperties = new Properties();
	private JDatePanelImpl datePanel = new JDatePanelImpl(dateModel, dateProperties);
	private JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
	
	public AbaPlugin(JFrame parentFrame) {
		super(parentFrame);
		initPnlForm();
		setContextoEditar(false);
				
		// iniciando listeners
		ListSelectionListener selectItemList = new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent event) {
				int linhaSelecionada = getTblResultado().getSelectedRow();
				if (linhaSelecionada > -1) {
					Object campo =  getTblResultado().getValueAt(linhaSelecionada, 1);
					txtNomePlugin.setText(( campo != null ? campo.toString() : ""));
					campo =  getTblResultado().getValueAt(linhaSelecionada, 2);
					txtDescricao.setText(( campo != null ? campo.toString() : ""));
					String data = getTblResultado().getValueAt(linhaSelecionada, 3).toString();
					if(!data.equals("")) {
						setDataModelFromStringDate(data);
					} else {
						dateModel.setSelected(false);
					}
					setContextoEditar(true);
				};
			}
		};
		ActionListener searchClick = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				try {
					String termo = getTxtStringBusca().getText();
					if(termo != null && termo.length() > 0) {
						String atributo = getCmbParametroConsulta().getSelectedItem().toString();
						atributo = converComboChoiceToDBAtributte(atributo);
						List<Plugin> plgs = Client.getServer().searchPlugins(atributo, termo);
						popularTabelaResultado(plgs);
					} else {
						loadData();
					}
				}
				catch (ServerServiceException err) { exibirDialogError(err.getMessage()); } 
				catch (RemoteException err) { exibirDialogError(Const.ERROR_REMOTE_EXCEPT); } 
				catch (NotBoundException err) { exibirDialogError(Const.ERROR_NOTBOUND_EXCEPT); }
			}
		};
		ActionListener saveClick = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				try {
					if(checkFieldsOnCreate()) {
						Plugin plg = new Plugin();
						plg.setNome(txtNomePlugin.getText());
						plg.setDescricao(txtDescricao.getText());
						/* 	se o botão 'novo' estiver habilitado, então é pq não foi clickado e,
						consequentemente, não representa um novo item, mas sim um update. */
						if(getBtnNovo().isEnabled()) {
							int linhaSelecionada = getTblResultado().getSelectedRow();
							String id = getTblResultado().getValueAt(linhaSelecionada, 0).toString();
							plg.setId(Integer.parseInt(id));
							Client.getServer().updatePlugin(plg);
							loadData();
							setContextoEditar(false);
						/* se não, representa um create */
						} else {
							// se quiser pegar do datePicker: (Date) datePicker.getModel().getValue()
							plg.setDataCriacaoFromDate(new Date());
							Client.getServer().createPlugin(plg);
							loadData();
							getBtnNovo().setEnabled(true);
						};
					};
				}
				catch (UICheckFieldException err) { exibirDialogInfo(err.getMessage()); }
				catch (ServerServiceException err) { exibirDialogError(err.getMessage()); } 
				catch (RemoteException err) { exibirDialogError(Const.ERROR_REMOTE_EXCEPT); } 
				catch (NotBoundException err) { exibirDialogError(Const.ERROR_NOTBOUND_EXCEPT); }
			}
		};
		ActionListener removeClick = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				int linhaSelecionada = getTblResultado().getSelectedRow();
				if (linhaSelecionada > -1) {
					String id = getTblResultado().getValueAt(linhaSelecionada, 0).toString();
					try {
						String msgConfirmacao = Const.WARN_CONFIRM_DELETE;
						msgConfirmacao = msgConfirmacao.replaceFirst("\\?", "plugin id: " + id + "e suas funcionalidades");
						if(exibirDialogConfirmation(msgConfirmacao)) {
							Client.getServer().deletePlugin(Integer.parseInt(id));
							loadData();
						}
					}
					catch (ServerServiceException err) { exibirDialogError(err.getMessage()); } 
					catch (RemoteException err) { exibirDialogError(Const.ERROR_REMOTE_EXCEPT); } 
					catch (NotBoundException err) { exibirDialogError(Const.ERROR_NOTBOUND_EXCEPT); }
				};
			}
		};
		initListeners(selectItemList, saveClick, removeClick, searchClick);
	}
	
	private void initPnlForm() {
		JPanel pnlForm = new JPanel(new MigLayout("","[right][grow]",""));
		pnlForm.add(lblNomePlugin);
		pnlForm.add(txtNomePlugin, "wrap, growx");
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
		txtNomePlugin.setText("");
		txtDescricao.setText("");
		setDataModelFromStringDate(Const.DATA_FORMAT.format(new Date()));
	}
	
	@Override
	public void setContextoEditar(boolean setar) {
		super.setContextoEditar(setar);
		if(!setar) {
			txtNomePlugin.setText("");
			txtDescricao.setText("");
			dateModel.setSelected(false);
		}
		txtNomePlugin.setEditable(setar);
		txtDescricao.setEditable(setar);
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
	public Vector<String> gerarHeader() {
		Vector<String> header = new Vector<String>();
		header.add("ID");
		header.add("NOME");
		header.add("DESCRIÇÃO");
		header.add("DATA DE CRIAÇÃO");
		return header;
	}
	
	@Override
	public void loadData() throws RemoteException, ServerServiceException, NotBoundException {
		setContextoEditar(false);
		List<Plugin> plugins = Client.getServer().getPlugins();
		popularTabelaResultado(plugins);
		
	}

	@Override
	public boolean checkFieldsOnCreate() throws UICheckFieldException {
		String campo;
		campo = this.txtNomePlugin.getText();
		if(campo == null || campo.length() <= 0) {
			throw new UICheckFieldException(Const.INFO_EMPTY_FIELD.replace("?", "nome"));
		}
		return true;
	}	
	
	@Override
	public String converComboChoiceToDBAtributte(String cmbChoice) {
		if(cmbChoice.equals(UIEnums.FILTROS_PLUGIN.Nome.toString())) return UIEnums.FILTROS_PLUGIN.Nome.getValue();
		if(cmbChoice.equals(UIEnums.FILTROS_PLUGIN.Descrição.toString())) return UIEnums.FILTROS_PLUGIN.Descrição.getValue();
		if(cmbChoice.equals(UIEnums.FILTROS_PLUGIN.Data.toString())) return UIEnums.FILTROS_PLUGIN.Data.getValue();
		return "";
	}
	
	/**
	 * Método para popular tabela de resultados de busca com lista de plugins
	 * @param plugins - Lista contendo os plugins a serem apresentados na tabela
	 * @param tipo - tipo para gerar Header da tabela
	 */
	public void popularTabelaResultado(List<Plugin> plugins) {
		Vector<Vector<Object>> dadosFinal = new Vector<Vector<Object>>();
		Vector<Object> linha;
		for(Plugin plg : plugins) {
			linha = new Vector<Object>();
			linha.add(plg.getId());
			linha.add(plg.getNome());
			linha.add(plg.getDescricao());
			linha.add(plg.getDataCriacaoToString()); 
			dadosFinal.add(linha);
		};
		this.getTblResultado().setModel(new DefaultTableModel(dadosFinal, gerarHeader()));
	}
	
	private void setDataModelFromStringDate(String data) {
		String ano = data.substring(0, data.indexOf("-"));
		String mes = data.substring(data.indexOf("-")+1, data.lastIndexOf("-"));
		String dia = data.substring(data.lastIndexOf("-")+1, data.length());
		dateModel.setDate(Integer.valueOf(ano), Integer.valueOf(mes)-1, Integer.valueOf(dia));
		dateModel.setSelected(true);
	}
}
