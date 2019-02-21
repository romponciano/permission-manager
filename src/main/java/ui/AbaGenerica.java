package ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.jdatepicker.impl.UtilDateModel;

import client.Client;
import common.Const;
import exception.ServerServiceException;
import exception.UICheckFieldException;
import model.BusinessEntity;
import model.Functionality;
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
	
	private List<Functionality> cacheTodasFuncionalidadesBanco;
	
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
		
		getBtnNovo().setEnabled(true); // sempre ativo

		setLayout(new MigLayout("", "[grow 70][grow 30]", "[][grow][]"));
		add(createSearchPanel(), "growx, wrap");
		add(tblResultadoScroll, "grow, spany");
		add(formPanel, "grow, wrap");
		add(createControlPanel(), "skip 1, growx");
		
		cacheTodasFuncionalidadesBanco = new ArrayList<Functionality>();
		
		initListeners();
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
	 * @param removeClick 
	 * @param searchClick 
	 * @param createNewElement 
	 */
	public void initListeners() {
		// listener do botão para cancelar edição/criação e resetar os campos preenchidos
		this.btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) { setContextoEditar(false);  }
		});
		// listener do botão para criar novo cadastro de item. Este listener só limpa os campos para iniciar preenchimento do usuário
		this.btnNovo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				try { loadData(); } catch (RemoteException | ServerServiceException | NotBoundException e) { } // nesse caso não fazer nada
				setContextoCriar(true); 
				btnRemover.setEnabled(false);
			}
		});
		// listener da tabela de resultado para preencher campos do form ao selecionar um item da tabela
		this.tblResultado.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent event) {
				int linhaSelecionada = getTblResultado().getSelectedRow();
				if (linhaSelecionada > -1) {
					setContextoEditar(true);
					setFormEdicao(linhaSelecionada);
					setContextoEditar(true);
				};
			}
		});
		// listener do botão para salvar criação de um novo dado OU update de um dado existente
		this.btnSalvar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				try {
					if(checkFieldsOnCreate()) {
						BusinessEntity objToSave = createObjToBeSaved(); 
						/* 	se o botão 'rmeover' estiver habilitado, então é pq não 
						 * 	não representa um novo item, mas sim um update. */
						if(getBtnRemover().isEnabled()) {
							int linhaSelecionada = getTblResultado().getSelectedRow();
							objToSave.setId(Long.parseLong(getTblResultado().getValueAt(linhaSelecionada, 0).toString()));
							objToSave.setDataUltimaModificacao(new Date());
							realizarUpdate(objToSave);
						/* se não, representa um create */
						} else {
							objToSave.setDataCriacao(new Date());
							realizarCreate(objToSave);
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
		});
		this.btnRemover.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				int linhaSelecionada = getTblResultado().getSelectedRow();
				if (linhaSelecionada > -1) {
					Long id = Long.parseLong(getTblResultado().getValueAt(linhaSelecionada, 0).toString());
					try {
						String msgConfirmacao = Const.WARN_CONFIRM_DELETE;
						msgConfirmacao = msgConfirmacao.replace("?1", "id: " + id);
						if(exibirDialogConfirmation(msgConfirmacao)) {
							realizarDelete(id);
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
		});
		this.btnBuscar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				try {
					String termo = getTxtStringBusca().getText();
					if(termo != null && termo.length() > 0) {
						String atributo = getCmbParametroConsulta().getSelectedItem().toString();
						atributo = converComboChoiceToDBAtributte(atributo);
						List<? extends BusinessEntity> resultadoConsulta = realizarBusca(atributo, termo);
						popularTabelaResultado(resultadoConsulta);
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
		});
	};
	
	/**
	 * Método para setar valores nos campos do form de edição
	 * @param linhaSelecionada - linha da tabela que foi selecionada para edição
	 */
	public abstract void setFormEdicao(int linhaSelecionada);

	/**
	 * Método para realizar o create, onde cada aba chama o seu create do server. 
	 * Neste método o objeto é convertido para sua classe específica
	 * @param objToSave - obj genérico que extendeu BusinessEntity e que representa o obj a ser salvo no bd
	 * @throws RemoteException
	 * @throws ServerServiceException
	 * @throws NotBoundException
	 */
	public abstract void realizarCreate(BusinessEntity objToSave) throws RemoteException, ServerServiceException, NotBoundException;

	/**
	 * Método para realizar o update, onde cada aba chama o seu update do server. 
	 * Neste método o objeto é convertido para sua classe específica
	 * @param objToSave - obj genérico que extendeu BusinessEntity e que representa o obj a ser salvo no bd
	 * @throws RemoteException
	 * @throws ServerServiceException
	 * @throws NotBoundException
	 */
	public abstract void realizarUpdate(BusinessEntity objToSave) throws RemoteException, ServerServiceException, NotBoundException;

	/**
	 * Método para pegar os campos de cada form e transformar em um objeto da classe a ser trabalhada.
	 * @return - um objeto BusinessEntity (de onde todos os modelos são extendidos), porém com os 
	 * campos específicos de cada classe. 
	 */
	public abstract BusinessEntity createObjToBeSaved();

	/**
	 * Método para realizar o delete, onde cada aba chama o seu delete do server. 
	 * Neste método o objeto é convertido para sua classe específica
	 * @param id - id no banco do objeto a ser deleteado
	 * @throws RemoteException
	 * @throws ServerServiceException
	 * @throws NotBoundException
	 */
	public abstract void realizarDelete(Long id) throws RemoteException, ServerServiceException, NotBoundException;

	/**
	 * Método para realizar a busca, onde cada aba chama o seu search do server. 
	 * Neste método o objeto é convertido para sua classe específica
	 * @param atributo - atributo selecionado na combobox de consulta
	 * @param termo - termo a ser procurado daquele atributo. Ex: atributo LIKE termo 
	 * @return - Lista contendo os valores encontrados da classe que realizou a busca
	 * @throws RemoteException
	 * @throws ServerServiceException
	 * @throws NotBoundException
	 */
	public abstract List<? extends BusinessEntity> realizarBusca(String atributo, String termo) throws RemoteException, ServerServiceException, NotBoundException;

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
	public abstract Vector<String> gerarHeaderTabelaResultado();

	/**
	 * Método para ativer os botões do modo edição: salvar e cancelar
	 * e desativar os outros
	 */
	public void setContextoEditar(boolean setar) {
		this.btnSalvar.setEnabled(setar);
		this.btnCancelar.setEnabled(setar);
		this.btnRemover.setEnabled(setar);
	};
	
	/**
	 * Método para ativer os botões do modo criação: salvar e cancelar
	 * e desativar os outros
	 */
	public void setContextoCriar(boolean setar) {
		this.btnSalvar.setEnabled(setar);
		this.btnCancelar.setEnabled(setar);
		this.btnRemover.setEnabled(!setar);
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
	
	public void setJTableColumnInsivible(JTable table, int columnIdx) {
		table.getColumnModel().getColumn(columnIdx).setMinWidth(0);
		table.getColumnModel().getColumn(columnIdx).setMaxWidth(0);
		table.getColumnModel().getColumn(columnIdx).setWidth(0);
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
	
	/**
	 * Método para exibir DialogBox de algum <b>erro</b>.
	 * @param msg - mensagem a ser exibida no DialogBox
	 */
	public boolean exibirDialogConfirmation(String msg) {
		int dialogButton = JOptionPane.YES_NO_OPTION;
		int dialogResult = JOptionPane.showConfirmDialog(this.parentFrame, msg, "Warning", dialogButton);
		if(dialogResult == JOptionPane.YES_OPTION){
			return true;
		}
		return false;
	}
	
	/**
	 * Método usado para setar uma String de data YYYY-MM-DD em um dateModel 
	 * @param dateModel - dataModel a ser setado
	 * @param data - String de data YYYY-MM-DD
	 */
	public void setDataModelFromStringDate(UtilDateModel dateModel, String data) {
		String ano = data.substring(0, data.indexOf("-"));
		String mes = data.substring(data.indexOf("-")+1, data.lastIndexOf("-"));
		String dia = data.substring(data.lastIndexOf("-")+1, data.length());
		dateModel.setDate(Integer.valueOf(ano), Integer.valueOf(mes)-1, Integer.valueOf(dia));
		dateModel.setSelected(true);
	}
	
	/**
	 * Método para converter escolha de busca da combobox
	 * para atributo que será utilizado na consulta ao banco.
	 * @param cmbChoice - escolha do usuário 
	 * @return - String do atributo utilizado no banco
	 */
	public abstract String converComboChoiceToDBAtributte(String cmbChoice);
	
	/**
	 * Método para popular tabela de resultados de busca com lista de usuários
	 * @param users - Lista contendo os usuários a serem apresentados na tabela
	 * @param tipo  - tipo para gerar Header da tabela
	 */
	public abstract void popularTabelaResultado(List<? extends BusinessEntity> objs);
	
	/**
	 * Método para iniciar painel de formulário de cada aba
	 */
	public abstract void initPnlForm();

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
	
	public JTextField getTxtStringBusca() {
		return txtStringBusca;
	}
	
	public List<Functionality> getCacheTodasFuncBanco() {
		return this.cacheTodasFuncionalidadesBanco;
	}
	
	public void setCacheTodasFuncBanco(List<Functionality> funcs) {
		this.cacheTodasFuncionalidadesBanco = funcs;
	}
	
	public void checkCacheTodasFuncBancoEmpty() {
		if(getCacheTodasFuncBanco().isEmpty()) {
			try { setCacheTodasFuncBanco(Client.getServer().getFunctionalities()); } 
			catch (RemoteException | ServerServiceException | NotBoundException e) { }
		}
	}
	
	public void atualizarCacheTodasFuncBanco() {
		try { cacheTodasFuncionalidadesBanco = Client.getServer().getFunctionalities(); } 
		catch (RemoteException | ServerServiceException | NotBoundException e) { }
	}
}
