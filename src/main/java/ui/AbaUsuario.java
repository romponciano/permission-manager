package ui;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import client.Client;
import exception.ServerServiceException;
import model.User;
import net.miginfocom.swing.MigLayout;

public class AbaUsuario extends AbaGenerica {

	private static final long serialVersionUID = -8445952309777454337L;

	private JLabel lblNomeUsuario = new JLabel("Nome: ");
	private JLabel lblLogin = new JLabel("Login: ");
	private JLabel lblStatus = new JLabel("Status: ");
	private JLabel lblGerencia = new JLabel("Gerência Atual: ");
	private JTextField txtNomeUsuario = new JTextField(15);
	private JTextField txtLogin = new JTextField(15);
	private JTextField txtStatus = new JTextField(15);
	private JTextField txtGerencia = new JTextField(15);

	public AbaUsuario() {
		super();
		initPnlForm();
		setContextoEditar(false);
	}

	@Override
	public void initListeners() {
		super.initListeners();
		setOnItemSelectListenerTableResult(
				new ListSelectionListener() {
					public void valueChanged(ListSelectionEvent event) {
						int linhaSelecionada = getTblResultado().getSelectedRow();
						if (linhaSelecionada > -1) {
							txtNomeUsuario.setText(getTblResultado().getValueAt(linhaSelecionada, 0).toString());
							txtLogin.setText(getTblResultado().getValueAt(linhaSelecionada, 1).toString());
							txtStatus.setText(getTblResultado().getValueAt(linhaSelecionada, 2).toString());
							txtGerencia.setText(getTblResultado().getValueAt(linhaSelecionada, 3).toString());
							setContextoEditar(true);
						};
					}
				}
		);
	}

	@Override
	public void loadData() throws RemoteException, ServerServiceException, NotBoundException {
		setContextoEditar(false);
		List<User> users = Client.getServer().getUsers();
		popularTabelaResultado(users);
	}

	private void initPnlForm() {
		JPanel pnlForm = new JPanel(new MigLayout("", "[right][grow]", ""));
		pnlForm.add(lblNomeUsuario);
		pnlForm.add(txtNomeUsuario, "growx, wrap");
		pnlForm.add(lblLogin);
		pnlForm.add(txtLogin, "growx, wrap");
		pnlForm.add(lblStatus);
		pnlForm.add(txtStatus, "growx, wrap");
		pnlForm.add(lblGerencia);
		pnlForm.add(txtGerencia, "growx");
		registerForm(pnlForm);
	}

	@Override
	public void setContextoEditar(boolean setar) {
		super.setContextoEditar(setar);
		txtNomeUsuario.setEditable(setar);
		txtLogin.setEditable(setar);
		txtStatus.setEditable(setar);
		txtGerencia.setEditable(setar);
	}

	@Override
	public Vector<String> createItemsCmbConsulta() {
		Vector<String> out = new Vector<String>();
		out.add(FILTROS_USUARIO.Nome.toString());
		out.add(FILTROS_USUARIO.Login.toString());
		out.add(FILTROS_USUARIO.Status.toString());
		out.add(FILTROS_USUARIO.Gerência.toString());
		return out;
	}

	@Override
	public Vector<String> gerarHeader() {
		Vector<String> header = new Vector<String>();
		header.add("NOME");
		header.add("LOGIN");
		header.add("STATUS");
		header.add("GERÊNCIA ATUAL");
		return header;
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
			linha.add(usr.getNome());
			linha.add(usr.getLogin());
			linha.add(usr.getStatus());
			linha.add(usr.getGerenciaAtual());
			dadosFinal.add(linha);
		};
		this.getTblResultado().setModel(new DefaultTableModel(dadosFinal, gerarHeader()));
	}
}
