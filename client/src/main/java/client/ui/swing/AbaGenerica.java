package client.ui.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
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
import client.ui.GenericUIFunctions;
import client.ui.UIEnums.ABAS;
import client.ui.UIEnums.FORM_CONTEXT;
import common.Const;
import common.exceptions.ServerServiceException;
import common.model.BusinessEntity;
import common.model.Functionality;
import net.miginfocom.swing.MigLayout;

public abstract class AbaGenerica extends JPanel implements Serializable, GenericUIFunctions {

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
	private ABAS aba;
	protected JFrame parentFrame;

	public AbaGenerica(JFrame parentFrame, ABAS aba) {
		this.parentFrame = parentFrame;
		this.aba = aba;

		this.formPanel = new JPanel(new MigLayout("", "[grow]", "[grow]"));
		this.cmbParametroConsulta = new JComboBox<String>();
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
	/**
	 * Método para gerar linha da JTable. É implementado na classe que extende pois
	 * cada aba pode ter seus campos diferentes uma da outra.
	 * 
	 * @param obj - Objeto genérico que representa o modelo
	 * @return - linha genérica convertida do modelo original
	 */
	public abstract Vector<Object> generateJTableLine(Object obj);

	/**
	 * Método para gerar vetor de strings com o cabeçalho da tabela de resultados da
	 * busca.
	 * 
	 * @return - retorna um Vector<String> com o nome de cada coluna
	 */
	public abstract Vector<String> gerarHeaderTabelaResultado();

	// ############################################ MÉTODOS DA UI SWING
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

	// ############################################ LISTENERS
	/**
	 * Listener responsável por resetar form de edição e proibir seu uso
	 * 
	 * @return
	 */
	private ActionListener createBtnCancelarAction() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				actionCancel();
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
				String termo = getTxtStringBusca().getText();
				if (termo != null && termo.length() > 0) {
					String atributo = convertComboChoiceToDBAtributte(
							getCmbParametroConsulta().getSelectedItem().toString(),
							getAba());
					actionSearchItems(atributo, termo);
				}
				// se for campo em branco, então é para buscar todos
				else {
					try {
						loadData();
					} catch (RemoteException | ServerServiceException | NotBoundException e) {
						dealWithError(e);
					}
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
				if (askConfirmation(Const.WARN_CONFIRM_DELETE))
					actionRemoveItem();
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
				/*
				 * se o botão 'remover' estiver habilitado, então é pq não não representa um
				 * novo item, mas sim um update
				 */
				if (getBtnRemover().isEnabled())
					actionUpdateItem();
				/* se não, representa um create */
				else
					actionSaveNewItem();
			};
		};
	};

	@Override
	public Long getSelectedItemId() {
		int linhaSelecionada = getTblResultado().getSelectedRow();
		if (linhaSelecionada > -1) {
			return Long.parseLong(getTblResultado().getValueAt(linhaSelecionada, 0).toString());
		}
		return null;
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
				actionNewItem();
			}
		};
	}

	// ############################################ MÉTODOS DA INTERFACE GENÉRICA
	@Override
	public void initListeners() {
		this.btnCancelar.addActionListener(createBtnCancelarAction());
		this.btnNovo.addActionListener(createBtnNovoAction());
		this.tblResultado.getSelectionModel().addListSelectionListener(createTblResultadoItemSelectListener());
		this.btnSalvar.addActionListener(createBtnSalverActionListener());
		this.btnRemover.addActionListener(createBtnRemoverActionListener());
		this.btnBuscar.addActionListener(createBtnBuscarActionListener());
	};

	@Override
	public void setContextoProibido() {
		this.btnSalvar.setEnabled(false);
		this.btnCancelar.setEnabled(false);
		this.btnRemover.setEnabled(false);
		this.setEnabledForm(false);
		this.clearForm();
	}

	@Override
	public void setContextoCriar() {
		this.btnSalvar.setEnabled(true);
		this.btnCancelar.setEnabled(true);
		this.btnRemover.setEnabled(false);
		this.setEnabledForm(true);
		this.clearForm();
	}

	@Override
	public void setContextoEditar(int selectedRowToEdit) {
		this.btnSalvar.setEnabled(true);
		this.btnCancelar.setEnabled(true);
		this.btnRemover.setEnabled(true);
		this.setEnabledForm(true);
		try {
			this.fillFormToEdit(selectedRowToEdit);
		} catch (RemoteException | ServerServiceException | NotBoundException e) {
			dealWithError(e);
		}
	}

	@Override
	public void showInfoMessage(String msg) {
		JOptionPane.showMessageDialog(this.parentFrame, msg, "Info", JOptionPane.INFORMATION_MESSAGE);
	}

	@Override
	public void showErrorMessage(String msg) {
		JOptionPane.showMessageDialog(this.parentFrame, msg, "Error", JOptionPane.ERROR_MESSAGE);
	}

	@Override
	public boolean askConfirmation(String msg) {
		int dialogButton = JOptionPane.YES_NO_OPTION;
		int dialogResult = JOptionPane.showConfirmDialog(this.parentFrame, msg, "Warning", dialogButton);
		if (dialogResult == JOptionPane.YES_OPTION) {
			return true;
		}
		return false;
	}

	@Override
	public void populateTableAllItems(List<? extends BusinessEntity> objs) {
		Vector<Vector<Object>> dadosFinal = new Vector<Vector<Object>>();
		for (Object obj : objs) {
			Vector<Object> linha = generateJTableLine(obj);
			dadosFinal.add(linha);
		}
		this.getTblResultado().setModel(new TableModelWithoutEdition(dadosFinal, gerarHeaderTabelaResultado()));
		setJTableColumnInsivible(this.getTblResultado(), 0);
	};

	// ############################################# MÉTODOS DA CLASSE
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

	public ABAS getAba() {
		return aba;
	}
}
