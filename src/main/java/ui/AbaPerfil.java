package ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
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
import model.Functionality;
import model.Perfil;
import model.Permission;
import net.miginfocom.swing.MigLayout;

public class AbaPerfil extends AbaGenerica {

	private static final long serialVersionUID = -6213919187419684978L;
	
	private JLabel lblNomePerfil = new JLabel("Nome: ");
	private JLabel lblDescricao = new JLabel("Descrição: ");
	private JLabel lblDataCriacao = new JLabel("Data de criação: ");
	private JTextField txtNomePerfil = new JTextField(15);
	private JTextField txtDescricao = new JTextField(15);

	private UtilDateModel dateModel = new UtilDateModel();
	private Properties dateProperties = new Properties();
	private JDatePanelImpl datePanel = new JDatePanelImpl(dateModel, dateProperties);
	private JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
	
	private TableWithCheckBox tblFuncs = new TableWithCheckBox();
	private JScrollPane tblFuncsScroll;
	
	public AbaPerfil(JFrame parentFrame) {
		super(parentFrame);
		tblFuncs.setAutoCreateRowSorter(true);
		this.tblFuncsScroll = new JScrollPane(tblFuncs);
		initPnlForm();
		setContextoEditar(false);
				
		// iniciando listeners
		ListSelectionListener selectItemList = new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent event) {
				int linhaSelecionada = getTblResultado().getSelectedRow();
				if (linhaSelecionada > -1) {
					Object campo =  getTblResultado().getValueAt(linhaSelecionada, 1);
					txtNomePerfil.setText(( campo != null ? campo.toString() : ""));
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
						List<Perfil> funs = Client.getServer().searchPerfils(atributo, termo);
						popularTabelaResultado(funs);
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
						Perfil perf = new Perfil();
						perf.setNome(txtNomePerfil.getText());
						perf.setDescricao(txtDescricao.getText());
						/* 	se o botão 'remover' estiver habilitado, então é pq não 
						 * 	não representa um novo item, mas sim um update. */
						if(getBtnRemover().isEnabled()) {
							int linhaSelecionada = getTblResultado().getSelectedRow();
							int perfilId = Integer.parseInt(getTblResultado().getValueAt(linhaSelecionada, 0).toString());
							/* 	essa abordagem de apagar todas as permissões do perfil e depois criar as permissões concedidas
							*	foi adotada pois é isso que acontece na prática. Atualizar permissões é remover e/ou add, então
							*	basta remover todas as permissões que o perfil tinha e criar todas que estão marcadas no momento. */
							Client.getServer().deletePermissionsByPerfilId(perfilId);
							salvarPermissoesConcedidas(perfilId);
							perf.setId(perfilId);
							Client.getServer().updatePerfil(perf);
						/* se não, representa um create */
						} else {							
							perf.setDataCriacaoFromDate(new Date());
							perf = Client.getServer().createPerfil(perf);
							salvarPermissoesConcedidas(perf.getId());
						};
						loadData();
						setContextoEditar(false);
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
					int perfilId = Integer.parseInt(getTblResultado().getValueAt(linhaSelecionada, 0).toString());
					try {
						String msgConfirmacao = Const.WARN_CONFIRM_DELETE;
						msgConfirmacao = msgConfirmacao.replaceFirst("\\?", "perfionalidade id: " + perfilId);
						if(exibirDialogConfirmation(msgConfirmacao)) {
							Client.getServer().deletePermissionsByPerfilId(perfilId);
							Client.getServer().deletePerfil(perfilId);
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
	
	/**
	 * Método para salvar as permissões que foram concedidas utilizando os checkbox
	 * @param perfilId - id do perfil ao qual as permissões estão associadas
	 * @throws RemoteException
	 * @throws ServerServiceException
	 * @throws NotBoundException
	 */
	private void salvarPermissoesConcedidas(int perfilId) throws RemoteException, ServerServiceException, NotBoundException {
		for(int i=0; i < tblFuncs.getRowCount(); i++) {
			Boolean valor = Boolean.valueOf(tblFuncs.getValueAt(i, 5).toString());
			if(valor) {
				Permission p = new Permission();
				p.getFunc().setId(Integer.parseInt(tblFuncs.getValueAt(i, 0).toString()));
				p.getPerfil().setId(perfilId);
				Client.getServer().createPermission(p);
			}
		}
	}
	
	/**
	 * Método para popular tabela de resultados de busca com lista de perfs
	 * @param perfs - Lista contendo os perfs a serem apresentados na tabela
	 * @param tipo - tipo para gerar Header da tabela
	 */
	private void popularTabelaResultado(List<Perfil> perfs) {
		Vector<Vector<Object>> dadosFinal = new Vector<Vector<Object>>();
		Vector<Object> linha;
		for(Perfil perf : perfs) {
			linha = new Vector<Object>();
			linha.add(perf.getId());
			linha.add(perf.getNome());
			linha.add(perf.getDescricao());
			linha.add(perf.getDataCriacaoToString());
			dadosFinal.add(linha);
		};
		this.getTblResultado().setModel(new DefaultTableModel(dadosFinal, gerarHeader()));
	}
	
	/**
	 * Método para popular tabela de permissões
	 * @param perfs - Lista contendo as permissões a serem apresentados na tabela
	 * @param tipo - tipo para gerar Header da tabela
	 */
	private void popularTabelaPermissoes(List<Permission> perms, List<Functionality> allFuncs) {
		Vector<Vector<Object>> dadosFinal = new Vector<Vector<Object>>();
		Vector<Object> linha;
		for(Functionality func : allFuncs) {
			linha = new Vector<Object>();
			linha.add(func.getId());
			linha.add(func.getNome());
			linha.add(func.getDescricao());
			linha.add(func.getDataCriacaoToString());
			linha.add(func.getPlugin().toString());
			linha.add(hasPermission(func.getId(), perms));
			dadosFinal.add(linha);
		};
		this.tblFuncs.setModel(new DefaultTableModel(dadosFinal, gerarHeaderPermissao()));
	}
	
	/**
	 * Método para checar se determinada funcionalidade esta na lista de permissões.
	 * @param funcId - id da funcionalidade a ser verificada
	 * @param allFuncsWithPermission - lista com as permissões concedidas
	 * @return true se funcionalidade for permitida, false caso contrário
	 */
	private boolean hasPermission(int funcId, List<Permission> allFuncsWithPermission) {
		for(Permission perm : allFuncsWithPermission) {
			if(funcId == perm.getFunc().getId()) return true;
		}
		return false;
	}
	
	/**
	 * Método para gerar HEADER da tbl de permissões
	 * @return - header com as colunas que devem aparecer
	 */
	private Vector<String> gerarHeaderPermissao() {
		Vector<String> header = new Vector<String>();
		header.add("ID");
		header.add("NOME");
		header.add("DESCRIÇÃO");
		header.add("DATA DE CRIAÇÃO");
		header.add("PLUGIN");
		header.add("PERMITIDO");
		return header;
	}
	
	/**
	 * Método para setar tabela de permissões quando for para editar um perfil
	 * @param setar - true irá ativar edição da tabela
	 * @param perfilId - id do perfil selecionado (só necessário se setar == true) ou null
	 */
	private void setEditarTblPermissoes(boolean setar, Integer perfilId) {
		List<Functionality> allFuncs = new ArrayList<Functionality>();
		List<Permission> perms = new ArrayList<Permission>();
		try {
			allFuncs = Client.getServer().getFunctionalities();
			if(setar) {
				perms = Client.getServer().searchPermissionsByPerfilId(perfilId);
			}
		} catch (Exception err) { }
		finally { popularTabelaPermissoes(perms, allFuncs); }
	}
	
	/**
	 * Método para setar data em model a partir de uma string
	 * @param data - data em formato string YYYY-MM-DD
	 */
	private void setDataModelFromStringDate(String data) {
		String ano = data.substring(0, data.indexOf("-"));
		String mes = data.substring(data.indexOf("-")+1, data.lastIndexOf("-"));
		String dia = data.substring(data.lastIndexOf("-")+1, data.length());
		dateModel.setDate(Integer.valueOf(ano), Integer.valueOf(mes)-1, Integer.valueOf(dia));
		dateModel.setSelected(true);
	}
	
	private void initPnlForm() {
		JPanel pnlForm = new JPanel(new MigLayout("","[right][grow]",""));
		pnlForm.add(lblNomePerfil);
		pnlForm.add(txtNomePerfil, "wrap, growx");
		pnlForm.add(lblDescricao);
		pnlForm.add(txtDescricao, "wrap, growx");
		pnlForm.add(lblDataCriacao);
		datePicker.getComponent(1).setEnabled(false); // setar datePicker disabled
		pnlForm.add(datePicker, "wrap, growx");
		pnlForm.add(tblFuncsScroll, "wrap, spanx, growx");
		registerForm(pnlForm);
	}
	
	@Override
	public void setContextoCriar(boolean setar) {
		super.setContextoCriar(setar);
		txtNomePerfil.setText("");
		txtDescricao.setText("");
		setDataModelFromStringDate(Const.DATA_FORMAT.format(new Date()));
		setEditarTblPermissoes(false, null);
		txtNomePerfil.setEditable(setar);
		txtDescricao.setEditable(setar);
		tblFuncs.setEnabled(setar);
	}
	
	@Override
	public void setContextoEditar(boolean setar) {
		super.setContextoEditar(setar);
		if(!setar) {
			txtNomePerfil.setText("");
			txtDescricao.setText("");
			dateModel.setSelected(false);
			setEditarTblPermissoes(false, null);
		} else {
			String perfilId = this.getTblResultado().getValueAt(this.getTblResultado().getSelectedRow(), 0).toString();
			setEditarTblPermissoes(true, Integer.parseInt(perfilId));
		}
		txtNomePerfil.setEditable(setar);
		txtDescricao.setEditable(setar);
		tblFuncs.setEnabled(setar);
	}
	
	@Override
	public Vector<String> createItemsCmbConsulta() {
		Vector<String> out = new Vector<String>();
		out.add(UIEnums.FILTROS_PERFIL.Nome.toString());
		out.add(UIEnums.FILTROS_PERFIL.Descrição.toString());
		out.add(UIEnums.FILTROS_PERFIL.Data.toString());
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
		List<Perfil> perfs = Client.getServer().getPerfils();
		popularTabelaResultado(perfs);
	}

	@Override
	public boolean checkFieldsOnCreate() throws UICheckFieldException {
		String campo = this.txtNomePerfil.getText();
		if(campo == null || campo.length() <= 0) {
			throw new UICheckFieldException(Const.INFO_EMPTY_FIELD.replace("?", "nome"));
		}
		return true;
	}	
	
	@Override
	public String converComboChoiceToDBAtributte(String cmbChoice) {
		if(cmbChoice.equals(UIEnums.FILTROS_PERFIL.Nome.toString())) return UIEnums.FILTROS_PERFIL.Nome.getValue();
		if(cmbChoice.equals(UIEnums.FILTROS_PERFIL.Descrição.toString())) return UIEnums.FILTROS_PERFIL.Descrição.getValue();
		if(cmbChoice.equals(UIEnums.FILTROS_PERFIL.Data.toString())) return UIEnums.FILTROS_PERFIL.Data.getValue();
		return "";
	}
}
