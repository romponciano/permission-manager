package ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import client.Client;
import common.Const;
import exception.ServerServiceException;
import exception.UICheckFieldException;
import model.Status;
import model.User;
import net.miginfocom.swing.MigLayout;

public class AbaUsuario extends AbaGenerica {

	private static final long serialVersionUID = -8445952309777454337L;

	final private JLabel lblNomeUsuario = new JLabel("Nome: ");
	final private JLabel lblLogin = new JLabel("Login: ");
	final private JLabel lblStatus = new JLabel("Status: ");
	final private JLabel lblGerencia = new JLabel("Gerência Atual: ");
	final private JTextField txtNomeUsuario = new JTextField(15);
	final private JTextField txtLogin = new JTextField(15);
	final private JTextField txtGerencia = new JTextField(15);
	final private ComboBoxWithItems cmbStatus = new ComboBoxWithItems();

	public AbaUsuario(JFrame parentFrame) {
		super(parentFrame);
		initPnlForm();
		setContextoEditar(false);
		
		// iniciando listeners
		ListSelectionListener selectItemTable = new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent event) {
				int linhaSelecionada = getTblResultado().getSelectedRow();
				if (linhaSelecionada > -1) {
					setContextoEditar(true);
					Object campo =  getTblResultado().getValueAt(linhaSelecionada, 1);
					txtNomeUsuario.setText(( campo != null ? (String)campo : ""));
					campo = getTblResultado().getValueAt(linhaSelecionada, 2);
					txtLogin.setText(( campo != null ? (String)campo : ""));
					campo = getTblResultado().getValueAt(linhaSelecionada, 4);
					txtGerencia.setText(( campo != null ? (String)campo : ""));
					// como statusId é estrangeira e obrigatória, então não checar se é null
					String stringUnica = getTblResultado().getValueAt(linhaSelecionada, 3).toString();
					ComboBoxItem item = new ComboBoxItem(stringUnica);
					cmbStatus.setSelectedItemById(item.getId());
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
						List<User> usrs = Client.getServer().searchUsers(atributo, termo);
						popularTabelaResultado(usrs);
					} else {
						loadData();
					}
					setContextoEditar(false);
					getBtnNovo().setEnabled(true);
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
						User usr = new User();
						usr.setNome(txtNomeUsuario.getText());
						usr.setLogin(txtLogin.getText());
						usr.setGerenciaAtual(txtGerencia.getText());
						usr.getStatus().setId(cmbStatus.getIdFromSelectedItem());
						/* 	se o botão 'rmeover' estiver habilitado, então é pq não 
						 * 	não representa um novo item, mas sim um update. */
						if(getBtnRemover().isEnabled()) {
							int linhaSelecionada = getTblResultado().getSelectedRow();
							String id = getTblResultado().getValueAt(linhaSelecionada, 0).toString();
							usr.setId(Integer.parseInt(id));
							Client.getServer().updateUser(usr);
						/* se não, representa um create */
						} else {
							Client.getServer().createUser(usr);
						};
						loadData();
						setContextoEditar(false);
						getBtnNovo().setEnabled(true);
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
						msgConfirmacao = msgConfirmacao.replaceFirst("\\?", "usuário id: " + id);
						if(exibirDialogConfirmation(msgConfirmacao)) {
							Client.getServer().deleteUser(Integer.parseInt(id));
							loadData();
						}
						setContextoEditar(false);
						getBtnNovo().setEnabled(true);
					}
					catch (ServerServiceException err) { exibirDialogError(err.getMessage()); } 
					catch (RemoteException err) { exibirDialogError(Const.ERROR_REMOTE_EXCEPT); } 
					catch (NotBoundException err) { exibirDialogError(Const.ERROR_NOTBOUND_EXCEPT); }
				};
			}
		};
		initListeners(selectItemTable, saveClick, removeClick, searchClick);
	}

	@Override
	public void setContextoCriar(boolean setar) {
		super.setContextoCriar(setar);
		setContextoEditar(true);
		txtNomeUsuario.setText("");
		txtLogin.setText("");
		txtGerencia.setText("");
	}
	
	@Override
	public void loadData() throws RemoteException, ServerServiceException, NotBoundException {
		setContextoEditar(false);
		List<User> users = Client.getServer().getUsers();
		popularTabelaResultado(users);
		List<String> opcoes = convertStatusListToStringList(Client.getServer().getStatus());
		cmbStatus.popularFromStringList(opcoes);
	}

	private void initPnlForm() {
		JPanel pnlForm = new JPanel(new MigLayout("", "[right][grow]", ""));
		pnlForm.add(lblNomeUsuario);
		pnlForm.add(txtNomeUsuario, "growx, wrap");
		pnlForm.add(lblLogin);
		pnlForm.add(txtLogin, "growx, wrap");
		pnlForm.add(lblStatus);
		pnlForm.add(cmbStatus, "growx, wrap");
		pnlForm.add(lblGerencia);
		pnlForm.add(txtGerencia, "growx");
		registerForm(pnlForm);
	}

	@Override
	public void setContextoEditar(boolean setar) {
		super.setContextoEditar(setar);
		if(!setar) {
			txtNomeUsuario.setText("");
			txtLogin.setText("");
			txtGerencia.setText("");
		}
		txtNomeUsuario.setEditable(setar);
		txtLogin.setEditable(setar);
		cmbStatus.setEditable(setar);
		txtGerencia.setEditable(setar);
	}

	@Override
	public Vector<String> createItemsCmbConsulta() {
		Vector<String> out = new Vector<String>();
		out.add(UIEnums.FILTROS_USUARIO.Nome.toString());
		out.add(UIEnums.FILTROS_USUARIO.Login.toString());
		out.add(UIEnums.FILTROS_USUARIO.Status.toString());
		out.add(UIEnums.FILTROS_USUARIO.Gerência.toString());
		return out;
	}

	@Override
	public Vector<String> gerarHeader() {
		Vector<String> header = new Vector<String>();
		header.add("ID");
		header.add("NOME");
		header.add("LOGIN");
		header.add("STATUS");
		header.add("GERÊNCIA ATUAL");
		return header;
	}

	@Override
	public boolean checkFieldsOnCreate() throws UICheckFieldException {
		String campo;
		if(this.cmbStatus.getSelectedItem() == null) {
			throw new UICheckFieldException(Const.INFO_EMPTY_FIELD.replace("?", "status"));
		}
		campo = this.txtNomeUsuario.getText();
		if(campo == null || campo.length() <= 0) {
			throw new UICheckFieldException(Const.INFO_EMPTY_FIELD.replace("?", "nome"));
		}
		campo = this.txtLogin.getText();
		if(campo == null || campo.length() <= 0) {
			throw new UICheckFieldException(Const.INFO_EMPTY_FIELD.replace("?", "login"));
		}
		if(campo.length() > 4) {
			throw new UICheckFieldException(Const.INFO_BIG_FIELD.replaceFirst("\\?", "login").replaceFirst("\\?", "4"));
		}
		return true;
	}
	
	@Override
	public String converComboChoiceToDBAtributte(String cmbChoice) {
		if(cmbChoice.equals(UIEnums.FILTROS_USUARIO.Nome.toString())) return UIEnums.FILTROS_USUARIO.Nome.getValue();
		if(cmbChoice.equals(UIEnums.FILTROS_USUARIO.Login.toString())) return UIEnums.FILTROS_USUARIO.Login.getValue();
		if(cmbChoice.equals(UIEnums.FILTROS_USUARIO.Status.toString())) return UIEnums.FILTROS_USUARIO.Status.getValue();
		if(cmbChoice.equals(UIEnums.FILTROS_USUARIO.Gerência.toString())) return UIEnums.FILTROS_USUARIO.Gerência.getValue();
		return "";
	}
	
	/**
	 * Método para popular tabela de resultados de busca com lista de usuários
	 * @param users - Lista contendo os usuários a serem apresentados na tabela
	 * @param tipo  - tipo para gerar Header da tabela
	 */
	public void popularTabelaResultado(List<User> users) {
		Vector<Vector<Object>> dadosFinal = new Vector<Vector<Object>>();
		Vector<Object> linha;
		for (User usr : users) {
			linha = new Vector<Object>();
			linha.add(usr.getId());
			linha.add(usr.getNome());
			linha.add(usr.getLogin());
			linha.add(usr.getStatus().toString());
			linha.add(usr.getGerenciaAtual());
			dadosFinal.add(linha);
		};
		this.getTblResultado().setModel(new DefaultTableModel(dadosFinal, gerarHeader()));
	}
	
	private List<String> convertStatusListToStringList(List<Status> stats) {
		List<String> opcoes = new ArrayList<String>();
		for(Status stat : stats) opcoes.add(stat.toString());
		return opcoes;
	}
}
