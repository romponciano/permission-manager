package ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionListener;

import exception.ServerServiceException;
import exception.UICheckFieldException;
import net.miginfocom.swing.MigLayout;

public abstract class AbaGenerica extends JPanel implements Serializable {

	private static final long serialVersionUID = -2402354356633072054L;

	private JPanel formPanel;
	private JLabel lblFiltrarPor;
	private JComboBox<String> cmbParametroConsulta;
	private JTextField txtStringBusca;
	private JButton btnBuscar;
	private JTable tblResultado;
	private JScrollPane tblResultadoScroll;
	private JButton btnSalvar;
	private JButton btnCancelar;
	private JButton btnRemover;
	private JButton btnNovo;
	
	protected JFrame parentFrame;
	
	public AbaGenerica(JFrame parentFrame) {
		this.parentFrame = parentFrame;
		
		this.formPanel = new JPanel(new MigLayout("","[grow]", "[grow]"));
		this.lblFiltrarPor = new JLabel("Filtrar por: ");
		this.cmbParametroConsulta = new JComboBox<String>(createItemsCmbConsulta());
		this.txtStringBusca = new JTextField(10);
		this.btnBuscar = new JButton("Buscar");
		this.btnRemover = new JButton("Remover");
		this.btnSalvar = new JButton("Salvar");
		this.btnCancelar = new JButton("Cancelar");
		this.btnNovo = new JButton("Novo");
		this.tblResultado = new JTable();
		this.tblResultado.setAutoCreateRowSorter(true);
		this.tblResultadoScroll = new JScrollPane(tblResultado);

		setLayout(new MigLayout("debug 1", "[grow 70][grow 30]", "[][grow][]"));
		add(createSearchPanel(), "growx, wrap");
		add(tblResultadoScroll, "grow, spany");
		add(formPanel, "grow, wrap");
		add(createControlPanel(), "skip 1, growx");
	}
	
	/**
	 * Método para checar se os campos estão válidos antes de
	 * salvar novo item
	 * @throws UICheckFieldException - se algum campo não passar na validação
	 */
	public abstract boolean checkFieldsOnCreate() throws UICheckFieldException;
	
	/**
	 * Método para iniciar os listeners da aba
	 * @param selectItemTable 
	 * @param createNewElement 
	 */
	public void initListeners(ListSelectionListener selectItemTable, ActionListener saveClick) {
		this.btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) { setContextoEditar(false);}
		});
		this.btnNovo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) { setContextoCriar(true); }
		});
		this.tblResultado.getSelectionModel().addListSelectionListener(selectItemTable);
		this.btnSalvar.addActionListener(saveClick);
	};
	
	/**
	 * Método para carregar todos os itens de um determinado elemento
	 * na tabela.
	 * @throws NotBoundException - erro ao 'renderizar' component
	 * @throws ServerServiceException - erro vindo do servidor
	 * @throws RemoteException - erro de comunicação com o servidor
	 */
	public abstract void loadData() throws RemoteException, ServerServiceException, NotBoundException;

	/**
	 * Método para gerar vetor de strings com o cabeçalho da tabela de 
	 * resultados da busca.
	 * @return - retorna um Vector<String> com o nome de cada coluna
	 */
	public abstract Vector<String> gerarHeader();

	/**
	 * Método para ativar (true) ou não (false) contexto de edição e criação
	 * @param setar - bool desejado para setar os campos
	 */
	public void setContextoEditar(boolean setar) {
		btnRemover.setEnabled(setar);
		btnSalvar.setEnabled(setar);
		btnCancelar.setEnabled(setar);
		btnNovo.setEnabled(true);
	};
	
	/**
	 * Método para ativer (true) ou não (false) contexto de criação de dados
	 * @param setar - bool desejado para setar os campos
	 */
	public void setContextoCriar(boolean setar) {
		setContextoEditar(setar);
		this.btnRemover.setEnabled(!setar);
		this.btnNovo.setEnabled(!setar);
	}

	/**
	 * Método para gerar items dos atributos que vão compor a combobox
	 * @return - vetor de string contendo os items
	 */
	public abstract Vector<String> createItemsCmbConsulta();

	/**
	 * Método para add o conteúdo pronto com os campos de
	 * formulário.
	 * @param content - formulário que será add
	 */
	protected void registerForm(JPanel content) {
		formPanel.add(content, "grow");
	}
	
	/**
	 * Método para criar linha com botões de controle
	 * @return - linha com os botões de controle
	 */
	private JPanel createControlPanel() {
		JPanel controlPanel = new JPanel(new MigLayout("ins 0", "", ""));
		controlPanel.add(btnRemover);
		controlPanel.add(btnSalvar, "push, al right");
		controlPanel.add(btnCancelar);
		controlPanel.add(btnNovo, "push, al right");
		return controlPanel;
	}

	/**
	 * Método para criar linha com botões de consulta
	 * @return - linha com os botões de consulta
	 */
	private JPanel createSearchPanel() {
		JPanel searchPanel = new JPanel(new MigLayout("ins 5", "[][][grow][]", ""));
		searchPanel.add(lblFiltrarPor);
		searchPanel.add(cmbParametroConsulta);
		searchPanel.add(txtStringBusca, "growx");
		searchPanel.add(btnBuscar);
		return searchPanel;
	}
	
	/**
	 * Método para exibir DialogBox de algum <b>erro</b>.
	 * @param msg - mensagem a ser exibida no DialogBox
	 */
	public void exibirDialogInfo(String msg) {
		JOptionPane.showMessageDialog(this.parentFrame, msg, "Info", JOptionPane.INFORMATION_MESSAGE);
	}
	
	/**
	 * Método para exibir DialogBox de algum <b>erro</b>.
	 * @param msg - mensagem a ser exibida no DialogBox
	 */
	public void exibirDialogError(String msg) {
		JOptionPane.showMessageDialog(this.parentFrame, msg, "Error", JOptionPane.ERROR_MESSAGE);
	}

	public JComboBox<String> getCmbParametroConsulta() {
		return cmbParametroConsulta;
	}

	public JTable getTblResultado() {
		return tblResultado;
	}

	public JButton getBtnSalvar() {
		return btnSalvar;
	}

	public JButton getBtnCancelar() {
		return btnCancelar;
	}

	public JButton getBtnRemover() {
		return btnRemover;
	}
	
	public JButton getBtnNovo() {
		return btnNovo;
	}
}
