package client.ui.swing;

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

import client.exceptions.UICheckFieldException;
import client.ui.UIEnums.FORM_CONTEXT;
import common.Const;
import common.exceptions.ServerServiceException;
import common.model.BusinessEntity;
import common.model.Functionality;
import net.miginfocom.swing.MigLayout;

public abstract class AbaGenerica extends JPanel implements Serializable {

	private static final long serialVersionUID = -2402354356633072054L;

	private JPanel formPanel;
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

		this.formPanel = new JPanel(new MigLayout("", "[grow]", "[grow]"));
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

	// ############################################ MÉTODOS ABSTRACT
	// ############################################
	/**
	 * Método para gerar linha da JTable. É implementado na classe que extende pois
	 * cada aba pode ter seus campos diferentes uma da outra.
	 * 
	 * @param obj - Objeto genérico que representa o modelo
	 * @return - linha genérica convertida do modelo original
	 */
	public abstract Vector<Object> generateJTableLine(Object obj);

	/**
	 * Método para converter escolha de busca da combobox para atributo que será
	 * utilizado na consulta ao banco.
	 * 
	 * @param cmbChoice - escolha do usuário
	 * @return - String do atributo utilizado no banco
	 */
	public abstract String converComboChoiceToDBAtributte(String cmbChoice);

	/**
	 * Método para gerar items dos atributos que vão compor a combobox
	 * 
	 * @return - vetor de string contendo os items
	 */
	public abstract Vector<String> createItemsCmbConsulta();

	/**
	 * Método para iniciar painel de formulário de cada aba
	 */
	public abstract void initPnlForm();

	/**
	 * Método para checar se os campos estão válidos antes de salvar novo item
	 * 
	 * @throws UICheckFieldException - se algum campo não passar na validação
	 */
	public abstract boolean checkFieldsOnCreate() throws UICheckFieldException;

	/**
	 * Método para realizar o create, onde cada aba chama o seu create do server.
	 * Neste método o objeto é convertido para sua classe específica
	 * 
	 * @param objToSave - obj genérico que extendeu BusinessEntity e que representa
	 *                  o obj a ser salvo no bd
	 * @throws RemoteException
	 * @throws ServerServiceException
	 * @throws NotBoundException
	 */
	public abstract void realizarCreate(BusinessEntity objToSave)
			throws RemoteException, ServerServiceException, NotBoundException;

	/**
	 * Método para realizar o update, onde cada aba chama o seu update do server.
	 * Neste método o objeto é convertido para sua classe específica
	 * 
	 * @param objToSave - obj genérico que extendeu BusinessEntity e que representa
	 *                  o obj a ser salvo no bd
	 * @throws RemoteException
	 * @throws ServerServiceException
	 * @throws NotBoundException
	 */
	public abstract void realizarUpdate(BusinessEntity objToSave)
			throws RemoteException, ServerServiceException, NotBoundException;

	/**
	 * Método para pegar os campos de cada form e transformar em um objeto da classe
	 * a ser trabalhada.
	 * 
	 * @return - um objeto BusinessEntity (de onde todos os modelos são extendidos),
	 *         porém com os campos específicos de cada classe.
	 */
	public abstract BusinessEntity createObjToBeSaved();

	/**
	 * Método para realizar o delete, onde cada aba chama o seu delete do server.
	 * Neste método o objeto é convertido para sua classe específica
	 * 
	 * @param id - id no banco do objeto a ser deleteado
	 * @throws RemoteException
	 * @throws ServerServiceException
	 * @throws NotBoundException
	 */
	public abstract void realizarDelete(Long id) throws RemoteException, ServerServiceException, NotBoundException;

	/**
	 * Método para realizar a busca, onde cada aba chama o seu search do server.
	 * Neste método o objeto é convertido para sua classe específica
	 * 
	 * @param atributo - atributo selecionado na combobox de consulta
	 * @param termo    - termo a ser procurado daquele atributo. Ex: atributo LIKE
	 *                 termo
	 * @return - Lista contendo os valores encontrados da classe que realizou a
	 *         busca
	 * @throws RemoteException
	 * @throws ServerServiceException
	 * @throws NotBoundException
	 */
	public abstract List<? extends BusinessEntity> realizarBusca(String atributo, String termo)
			throws RemoteException, ServerServiceException, NotBoundException;

	/**
	 * Método para carregar todos os itens de um determinado elemento na tabela.
	 * 
	 * @throws NotBoundException      - erro ao 'renderizar' component
	 * @throws ServerServiceException - erro vindo do servidor
	 * @throws RemoteException        - erro de comunicação com o servidor
	 */
	public abstract void loadData() throws RemoteException, ServerServiceException, NotBoundException;

	/**
	 * Método para gerar vetor de strings com o cabeçalho da tabela de resultados da
	 * busca.
	 * 
	 * @return - retorna um Vector<String> com o nome de cada coluna
	 */
	public abstract Vector<String> gerarHeaderTabelaResultado();

	/**
	 * Método responsável por limpar formulário de edição/criação
	 */
	public abstract void clearForm();

	/**
	 * Método responsável por preencher formulário de edição com os valores de uma
	 * linha que foi selecionada da tabela de resultados. Como alguns campos são
	 * preenchidos com valores vindos do banco, então este método pode lançar
	 * exceções vindas do server.
	 * 
	 * @param selectedRowToEdit - linha selecionada
	 * @throws RemoteException
	 * @throws ServerServiceException
	 * @throws NotBoundException
	 */
	public abstract void fillFormToEdit(int selectedRowToEdit)
			throws RemoteException, ServerServiceException, NotBoundException;

	/**
	 * Método responsável por habilitar ou desabilitar os campos de formulário
	 * 
	 * @param value - boolean: true para habilitar; false para desabilitar;
	 */
	public abstract void setEnabledForm(boolean value);

	// ############################################ LISTENERS
	// ############################################
	/**
	 * Listener responsável por resetar form de edição e proibir seu uso
	 * 
	 * @return
	 */
	private ActionListener createBtnCancelarAction() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				setContext(FORM_CONTEXT.Proibido);
			}
		};
	}

	/**
	 * Listener responsável por pegar o campo (atributo) e termos desejados para
	 * consulta do usuário e exibir o resultado da consulta na tabela de resultados.
	 * 
	 * @return
	 */
	private ActionListener createBtnBuscarActionListener() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				try {
					String termo = getTxtStringBusca().getText();
					if (termo != null && termo.length() > 0) {
						String atributo = converComboChoiceToDBAtributte(
								getCmbParametroConsulta().getSelectedItem().toString());
						List<? extends BusinessEntity> resultadoConsulta = realizarBusca(atributo, termo);
						popularTabelaResultado(resultadoConsulta);
					} else
						loadData(); // se for campo em branco, então é para buscar todos
					setContext(FORM_CONTEXT.Proibido);
				} catch (ServerServiceException err) {
					exibirDialogError(err.getMessage());
				} catch (RemoteException err) {
					exibirDialogError(Const.ERROR_REMOTE_EXCEPT);
				} catch (NotBoundException err) {
					exibirDialogError(Const.ERROR_NOTBOUND_EXCEPT);
				}
			}
		};
	}

	/**
	 * Listener responsável por exibir confirmação de exclusão e chamar o método
	 * para excluir um item selecionado
	 * 
	 * @return
	 */
	private ActionListener createBtnRemoverActionListener() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				int linhaSelecionada = getTblResultado().getSelectedRow();
				if (linhaSelecionada > -1) {
					Long id = Long.parseLong(getTblResultado().getValueAt(linhaSelecionada, 0).toString());
					try {
						String msgConfirmacao = Const.WARN_CONFIRM_DELETE;
						msgConfirmacao = msgConfirmacao.replace("?1", "id: " + id);
						if (exibirDialogConfirmation(msgConfirmacao)) {
							realizarDelete(id);
							loadData();
						}
						setContext(FORM_CONTEXT.Proibido);
					} catch (ServerServiceException err) {
						exibirDialogError(err.getMessage());
					} catch (RemoteException err) {
						exibirDialogError(Const.ERROR_REMOTE_EXCEPT);
					} catch (NotBoundException err) {
						exibirDialogError(Const.ERROR_NOTBOUND_EXCEPT);
					}
				}
				;
			}
		};
	}

	/**
	 * Listener responsável por gerar pegar valores do form e salvar OU criar novo
	 * obj
	 * 
	 * @return
	 */
	private ActionListener createBtnSalverActionListener() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				try {
					if (checkFieldsOnCreate()) {
						BusinessEntity objToSave = createObjToBeSaved();
						/*
						 * se o botão 'remover' estiver habilitado, então é pq não não representa um
						 * novo item, mas sim um update.
						 */
						if (getBtnRemover().isEnabled()) {
							int linhaSelecionada = getTblResultado().getSelectedRow();
							objToSave.setId(
									Long.parseLong(getTblResultado().getValueAt(linhaSelecionada, 0).toString()));
							objToSave.setDataUltimaModificacao(new Date());
							realizarUpdate(objToSave);
							/* se não, representa um create */
						} else {
							objToSave.setDataCriacao(new Date());
							realizarCreate(objToSave);
						}
						;
						setContext(FORM_CONTEXT.Proibido);
						loadData();
					}
					;
				} catch (UICheckFieldException err) {
					exibirDialogInfo(err.getMessage());
				} catch (ServerServiceException err) {
					exibirDialogError(err.getMessage());
				} catch (RemoteException err) {
					exibirDialogError(Const.ERROR_REMOTE_EXCEPT);
				} catch (NotBoundException err) {
					exibirDialogError(Const.ERROR_NOTBOUND_EXCEPT);
				}
			}
		};
	}

	/**
	 * Listener responsável por (1) setar valores no formulário caso o usuário
	 * selecione item da tabela, (2) proibir e limpar formulário caso o usuário
	 * selecione mais de uma item ou caso nenhuma linha esteja selecionada
	 * 
	 * @return
	 */
	private ListSelectionListener createTblResultadoItemSelectListener() {
		return new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent event) {
				int linhaSelecionada = getTblResultado().getSelectedRow();
				if (tblResultado.getSelectedRowCount() > 1 || linhaSelecionada < 0)
					setContext(FORM_CONTEXT.Proibido);
				else if (linhaSelecionada > -1)
					setContext(FORM_CONTEXT.Editar, linhaSelecionada);

			}
		};
	}

	/**
	 * Listener responsável por limpar campos de formulário para um novo cadastro
	 * 
	 * @return
	 */
	private ActionListener createBtnNovoAction() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				try {
					loadData();
				} catch (RemoteException | ServerServiceException | NotBoundException e) {
				} // nesse caso não fazer nada
				setContext(FORM_CONTEXT.Criar);
			}
		};
	}

	// ############################################ MÉTODOS GENÉRICOS
	// ############################################
	/**
	 * Método para iniciar os listeners padrões das abas
	 */
	public void initListeners() {
		this.btnCancelar.addActionListener(createBtnCancelarAction());
		this.btnNovo.addActionListener(createBtnNovoAction());
		this.tblResultado.getSelectionModel().addListSelectionListener(createTblResultadoItemSelectListener());
		this.btnSalvar.addActionListener(createBtnSalverActionListener());
		this.btnRemover.addActionListener(createBtnRemoverActionListener());
		this.btnBuscar.addActionListener(createBtnBuscarActionListener());
	};

	/**
	 * Método responsável por setar contexto do momento. Por exemplo: habilitar e
	 * desabilitar os campos e botões corretos para edição de um item;
	 * 
	 * @param context           - Contexto desejado: Edição, Criação ou Proibido
	 *                          (desabilita e limpa campos)
	 * @param selectedRowToEdit - <b>OBRIGRATÓRIO SE<b> for contexto de Edição.
	 *                          Passar linha que foi selecionada pelo usuário na
	 *                          tabela de resultados
	 */
	public void setContext(FORM_CONTEXT context, Integer... selectedRowToEdit) {
		if (context.equals(FORM_CONTEXT.Criar))
			setContextoCriar();
		else if (context.equals(FORM_CONTEXT.Editar))
			setContextoEditar((int) selectedRowToEdit[0]);
		else if (context.equals(FORM_CONTEXT.Proibido))
			setContextoProibido();
	}

	/**
	 * Método para setar campos do contexto proibido. Ou seja, contexto onde o
	 * usuário não consegue clicar ou edição nenhum campo do formulário
	 */
	private void setContextoProibido() {
		this.btnSalvar.setEnabled(false);
		this.btnCancelar.setEnabled(false);
		this.btnRemover.setEnabled(false);
		this.setEnabledForm(false);
		this.clearForm();
	}

	/**
	 * Método para setar campos do contexto de criação. Ou seja, contexto onde o
	 * usuário irá inserir um novo item e, portanto, preencher os campos do
	 * formulário
	 */
	private void setContextoCriar() {
		this.btnSalvar.setEnabled(true);
		this.btnCancelar.setEnabled(true);
		this.btnRemover.setEnabled(false);
		this.setEnabledForm(true);
		this.clearForm();
	}

	/**
	 * Método para setar campos do contexto de edição. Ou seja, contexto onde o
	 * usuário irá editar dados de uma dada linha selecionada.
	 * 
	 * @param selectedRowToEdit - linha selecionada pelo usuário, da tabela de
	 *                          resultados, para edição
	 */
	private void setContextoEditar(int selectedRowToEdit) {
		this.btnSalvar.setEnabled(true);
		this.btnCancelar.setEnabled(true);
		this.btnRemover.setEnabled(true);
		this.setEnabledForm(true);
		try {
			this.fillFormToEdit(selectedRowToEdit);
		} catch (ServerServiceException err) {
			exibirDialogError(err.getMessage());
		} catch (RemoteException err) {
			exibirDialogError(Const.ERROR_REMOTE_EXCEPT);
		} catch (NotBoundException err) {
			exibirDialogError(Const.ERROR_NOTBOUND_EXCEPT);
		}
	}

	/**
	 * Método para add o conteúdo pronto com os campos de formulário.
	 * 
	 * @param content - formulário que será add
	 */
	protected void registerForm(JPanel content) {
		formPanel.add(content, "grow");
	}

	/**
	 * Método para criar linha com botões de controle
	 * 
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
	 * 
	 * @return - linha com os botões de consulta
	 */
	private JPanel createSearchPanel() {
		JPanel searchPanel = new JPanel(new MigLayout("ins 5", "[][][grow][]", ""));
		searchPanel.add(new JLabel("Filtrar por: "));
		searchPanel.add(cmbParametroConsulta);
		searchPanel.add(txtStringBusca, "growx");
		searchPanel.add(btnBuscar);
		return searchPanel;
	}

	/**
	 * Método para setar uma coluna de uma JTable como <b>invisível</b>. Ou seja, a
	 * coluna continua existindo na JTable, porém não é apresentada na interface.
	 * Atualmente é utilizado para setar as colunas de IDs das tabelas, pois o ID é
	 * necessário posteriormente ao editar ou remover item. <b>Para funcionar, a
	 * JTable já precisa estar populada.</b>
	 * 
	 * @param table     - JTable que terá coluna setada como invisível
	 * @param columnIdx - índice da coluna que ficará invisível (começa em 0)
	 */
	public void setJTableColumnInsivible(JTable table, int columnIdx) {
		table.getColumnModel().getColumn(columnIdx).setMinWidth(0);
		table.getColumnModel().getColumn(columnIdx).setMaxWidth(0);
		table.getColumnModel().getColumn(columnIdx).setWidth(0);
	}

	/**
	 * Método para exibir DialogBox de algum <b>erro</b>.
	 * 
	 * @param msg - mensagem a ser exibida no DialogBox
	 */
	public void exibirDialogInfo(String msg) {
		JOptionPane.showMessageDialog(this.parentFrame, msg, "Info", JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * Método para exibir DialogBox de algum <b>erro</b>.
	 * 
	 * @param msg - mensagem a ser exibida no DialogBox
	 */
	public void exibirDialogError(String msg) {
		JOptionPane.showMessageDialog(this.parentFrame, msg, "Error", JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * Método para exibir DialogBox de algum <b>erro</b>.
	 * 
	 * @param msg - mensagem a ser exibida no DialogBox
	 */
	public boolean exibirDialogConfirmation(String msg) {
		int dialogButton = JOptionPane.YES_NO_OPTION;
		int dialogResult = JOptionPane.showConfirmDialog(this.parentFrame, msg, "Warning", dialogButton);
		if (dialogResult == JOptionPane.YES_OPTION) {
			return true;
		}
		return false;
	}

	/**
	 * Método usado para setar uma String de data YYYY-MM-DD em um dateModel
	 * 
	 * @param dateModel - dataModel a ser setado
	 * @param data      - String de data YYYY-MM-DD
	 */
	public void setDataModelFromStringDate(UtilDateModel dateModel, String data) {
		String ano = data.substring(0, data.indexOf("-"));
		String mes = data.substring(data.indexOf("-") + 1, data.lastIndexOf("-"));
		String dia = data.substring(data.lastIndexOf("-") + 1, data.length());
		dateModel.setDate(Integer.valueOf(ano), Integer.valueOf(mes) - 1, Integer.valueOf(dia));
		dateModel.setSelected(true);
	}

	/**
	 * Método para popular tabela de resultados de busca com lista de usuários
	 * 
	 * @param users - Lista contendo os usuários a serem apresentados na tabela
	 * @param tipo  - tipo para gerar Header da tabela
	 */
	public void popularTabelaResultado(List<? extends BusinessEntity> objs) {
		Vector<Vector<Object>> dadosFinal = new Vector<Vector<Object>>();
		for (Object obj : objs) {
			Vector<Object> linha = generateJTableLine(obj);
			dadosFinal.add(linha);
		}
		this.getTblResultado().setModel(new TableModelWithoutEdition(dadosFinal, gerarHeaderTabelaResultado()));
		setJTableColumnInsivible(this.getTblResultado(), 0);
	};

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
		return cacheTodasFuncionalidadesBanco;
	}

	public void setCacheTodasFuncBanco(List<Functionality> funcs) {
		this.cacheTodasFuncionalidadesBanco = funcs;
	}

	public void checkCacheTodasFuncBancoEmpty() {
		if (getCacheTodasFuncBanco().isEmpty()) {
			try {
				setCacheTodasFuncBanco(Client.getServer().getFunctionalities());
			} catch (RemoteException | ServerServiceException | NotBoundException e) {
			}
		}
	}

	public void atualizarCacheTodasFuncBanco() {
		try {
			cacheTodasFuncionalidadesBanco = Client.getServer().getFunctionalities();
		} catch (RemoteException | ServerServiceException | NotBoundException e) {
		}
	}
}
