package client.ui.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import client.Client;
import client.exceptions.UICheckFieldException;
import client.ui.UIEnums;
import client.ui.UIEnums.FORM_CONTEXT;
import common.Const;
import common.exceptions.ServerServiceException;
import common.model.BusinessEntity;
import common.model.Perfil;
import common.model.Status;
import common.model.User;
import net.miginfocom.swing.MigLayout;

public class AbaUsuario extends AbaGenerica {

	private static final long serialVersionUID = -8445952309777454337L;

	final private JTextField txtNomeUsuario = new JTextField(15);
	final private JTextField txtLogin = new JTextField(15);
	final private JTextField txtGerencia = new JTextField(15);
	final private ComboBoxWithItems cmbStatus = new ComboBoxWithItems();
	final private ComboBoxWithItems cmbPerfis = new ComboBoxWithItems();
	final private JButton btnAddPerfil = new JButton("+");
	final private JButton btnRemoverPerfil = new JButton("-");
	final private JList<BusinessEntity> lstPerfis = new JList<BusinessEntity>();
	final private DefaultListModel<BusinessEntity> modelPerfilList = new DefaultListModel<BusinessEntity>();

	public AbaUsuario(JFrame parentFrame) {
		super(parentFrame);
		initPnlForm();

		// listeners dos componentes exclusivos da aba usuário
		btnAddPerfil.addActionListener(createBtnAddPerfilActionListener());
		btnRemoverPerfil.addActionListener(createRemoverPerfilActionListener());
		lstPerfis.addListSelectionListener(createLstPerfilItemSelectListener());
	}

	private ListSelectionListener createLstPerfilItemSelectListener() {
		return new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				btnRemoverPerfil.setEnabled(true);
			}
		};
	}

	private ActionListener createRemoverPerfilActionListener() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				modelPerfilList.removeElementAt(lstPerfis.getSelectedIndex());
				btnRemoverPerfil.setEnabled(false);
			}
		};
	}

	private ActionListener createBtnAddPerfilActionListener() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				BusinessEntity itemSelecionado = cmbPerfis.getSelectedComboBoxItem();
				if (modelPerfilList.contains(itemSelecionado))
					showInfoMessage("Este perfil já esta lista.");
				else
					modelPerfilList.addElement(itemSelecionado);
			}
		};
	}

	private JPanel createProfilePanel() {
		JPanel lay = new JPanel(new MigLayout("", "[][][]", ""));
		lay.setBorder(BorderFactory.createTitledBorder("Perfis do Usuário"));
		lay.add(cmbPerfis, "growx");
		lay.add(btnAddPerfil, "push, al right");
		lay.add(btnRemoverPerfil, "wrap");
		lay.add(new JScrollPane(lstPerfis), "spanx, growx, h min:100%");
		return lay;
	}

	private List<Perfil> getPerfisFromList() {
		List<Perfil> out = new ArrayList<Perfil>();
		for (int i = 0; i < lstPerfis.getModel().getSize(); i++) {
			BusinessEntity be = lstPerfis.getModel().getElementAt(i);
			out.add((Perfil) be);
		}
		return out;
	}

	private void popularLstUserProfile(List<Perfil> perfisUsr) {
		modelPerfilList.removeAllElements();
		for (Perfil perf : perfisUsr)
			modelPerfilList.addElement(perf);
		lstPerfis.setModel(modelPerfilList);
	}

	@Override
	public void loadData() throws RemoteException, ServerServiceException, NotBoundException {
		setContext(FORM_CONTEXT.Proibido);
		popularTabelaResultado(Client.getServer().getUsers());
		cmbStatus.popularFromBusinessEntity(Client.getServer().getStatus());
		cmbPerfis.popularFromBusinessEntity(Client.getServer().getPerfis());
		modelPerfilList.removeAllElements();
		lstPerfis.setModel(modelPerfilList);
	}

	@Override
	public void initPnlForm() {
		JPanel pnlForm = new JPanel(new MigLayout("", "[right][grow]", ""));
		pnlForm.add(new JLabel("Nome: "));
		pnlForm.add(txtNomeUsuario, "growx, wrap");
		pnlForm.add(new JLabel("Login: "));
		pnlForm.add(txtLogin, "growx, wrap");
		pnlForm.add(new JLabel("Status: "));
		pnlForm.add(cmbStatus, "growx, wrap");
		pnlForm.add(new JLabel("Gerência Atual: "));
		pnlForm.add(txtGerencia, "growx, wrap");
		pnlForm.add(createProfilePanel(), "spanx, grow, h min:100%");
		registerForm(pnlForm);
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
	public Vector<String> gerarHeaderTabelaResultado() {
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
		if (this.cmbStatus.getSelectedItem() == null) {
			throw new UICheckFieldException(Const.INFO_EMPTY_FIELD.replace("?", "status"));
		}
		campo = this.txtNomeUsuario.getText();
		if (campo == null || campo.length() <= 0) {
			throw new UICheckFieldException(Const.INFO_EMPTY_FIELD.replace("?", "nome"));
		}
		campo = this.txtLogin.getText();
		if (campo == null || campo.length() <= 0) {
			throw new UICheckFieldException(Const.INFO_EMPTY_FIELD.replace("?", "login"));
		}
		if (campo.length() > 4) {
			throw new UICheckFieldException(Const.INFO_BIG_FIELD.replaceFirst("\\?", "login").replaceFirst("\\?", "4"));
		}
		return true;
	}

	@Override
	public String converComboChoiceToDBAtributte(String cmbChoice) {
		if (cmbChoice.equals(UIEnums.FILTROS_USUARIO.Nome.toString()))
			return UIEnums.FILTROS_USUARIO.Nome.getValue();
		if (cmbChoice.equals(UIEnums.FILTROS_USUARIO.Login.toString()))
			return UIEnums.FILTROS_USUARIO.Login.getValue();
		if (cmbChoice.equals(UIEnums.FILTROS_USUARIO.Status.toString()))
			return UIEnums.FILTROS_USUARIO.Status.getValue();
		if (cmbChoice.equals(UIEnums.FILTROS_USUARIO.Gerência.toString()))
			return UIEnums.FILTROS_USUARIO.Gerência.getValue();
		return "";
	}

	@Override
	public List<? extends BusinessEntity> realizarBusca(String atributo, String termo)
			throws RemoteException, ServerServiceException, NotBoundException {
		return Client.getServer().searchUsers(atributo, termo);
	}

	@Override
	public void realizarDelete(Long id) throws RemoteException, ServerServiceException, NotBoundException {
		Client.getServer().deleteUser(id);
	}

	@Override
	public void realizarCreate(BusinessEntity objToSave)
			throws RemoteException, ServerServiceException, NotBoundException {
		Client.getServer().createUser((User) objToSave);
	}

	@Override
	public void realizarUpdate(BusinessEntity objToSave)
			throws RemoteException, ServerServiceException, NotBoundException {
		Client.getServer().updateUser((User) objToSave);
	}

	@Override
	public BusinessEntity createObjToBeSaved() {
		String name = txtNomeUsuario.getText();
		User usr = new User(null, name);
		usr.setLogin(txtLogin.getText());
		usr.setGerenciaAtual(txtGerencia.getText());
		Long statusId = cmbStatus.getIdFromSelectedItem();
		usr.setStatus(new Status(statusId, null));
		usr.setPerfis(getPerfisFromList());
		return usr;
	}

	@Override
	public void fillFormToEdit(int selectedRowToEdit)
			throws RemoteException, ServerServiceException, NotBoundException {
		Object campo = getTblResultado().getValueAt(selectedRowToEdit, 1);
		txtNomeUsuario.setText((campo != null ? (String) campo : ""));
		campo = getTblResultado().getValueAt(selectedRowToEdit, 2);
		txtLogin.setText((campo != null ? (String) campo : ""));
		campo = getTblResultado().getValueAt(selectedRowToEdit, 4);
		txtGerencia.setText((campo != null ? (String) campo : ""));
		// como statusId é estrangeira e obrigatória, então não precisa checar se é null
		BusinessEntity statusSelecionado = (BusinessEntity) getTblResultado().getValueAt(selectedRowToEdit, 3);
		cmbStatus.setSelectedItemById(statusSelecionado.getId());
		Long userId = Long
				.parseLong(this.getTblResultado().getValueAt(this.getTblResultado().getSelectedRow(), 0).toString());
		popularLstUserProfile(Client.getServer().searchUserProfilesByUserId(userId));
	}

	@Override
	public void setEnabledForm(boolean setValue) {
		txtNomeUsuario.setEditable(setValue);
		txtLogin.setEditable(setValue);
		txtGerencia.setEditable(setValue);
		cmbStatus.setEnabled(setValue);
		cmbPerfis.setEnabled(setValue);
		lstPerfis.setEnabled(setValue);
		btnAddPerfil.setEnabled(setValue);
		btnRemoverPerfil.setEnabled(false);
	}

	@Override
	public void clearForm() {
		txtNomeUsuario.setText("");
		txtLogin.setText("");
		txtGerencia.setText("");
		popularLstUserProfile(new ArrayList<Perfil>());
	}

	@Override
	public Vector<Object> generateJTableLine(Object obj) {
		Vector<Object> out = new Vector<Object>();
		User usr = (User) obj;
		out.add(usr.getId());
		out.add(usr.getName());
		out.add(usr.getLogin());
		out.add(usr.getStatus());
		out.add(usr.getGerenciaAtual());
		return out;
	}
}
