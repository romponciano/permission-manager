package ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
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
import javax.swing.table.DefaultTableModel;

import client.Client;
import common.Const;
import exception.DBConnectException;
import exception.DBConsultException;
import exception.ServerServiceException;
import model.Plugin;
import model.User;
import net.miginfocom.swing.MigLayout;

public class GUI implements Serializable {
	private static final long serialVersionUID = -188826826138600878L;
	
	final JFrame frame = new JFrame();
	final JPanel mainPanel = new JPanel(new MigLayout());
	
	// ---------- Components: Consulta
	String[] tiposConsulta = {TIPO_CONSULTA.Usuário.toString(), TIPO_CONSULTA.Plugin.toString(), TIPO_CONSULTA.Funcionalidade.toString()};
	final JComboBox<String> cmbTipoConsulta = new JComboBox<String>(tiposConsulta);
	final JButton btnConsultar = new JButton("Consultar");
	final JTable tblResultado = new JTable(); 
    final JScrollPane tblResultadoScroll = new JScrollPane(tblResultado);
	final JComboBox<String> cmbParametroConsulta = new JComboBox<String>(); 
	final JTextField txtStringBusca = new JTextField(10);
	final JButton btnBuscar = new JButton("Buscar");
	// ---------- Components: Edição e Criação
	final JButton btnRemover = new JButton("Remover");
	final JButton btnSalvar = new JButton("Salvar");
	final JButton btnCancelar = new JButton("Cancelar");
	final JLabel lblNome = new JLabel("Nome: ");
	final JLabel lblLogin = new JLabel("Login: ");
	final JLabel lblStatus = new JLabel("Status: ");
	final JLabel lblGerencia = new JLabel("Gerência Atual: ");
	final JTextField txtNome = new JTextField(15);
	final JTextField txtLogin = new JTextField(15);
	final JTextField txtStatus = new JTextField(15);
	final JTextField txtGerencia = new JTextField(15);
	
	/**
	 * Inicializa a GUI
	 */
	public void init() {
		tblResultado.setAutoCreateRowSorter(true); // setar filtro da table
		atualizarCmbParametros(TIPO_CONSULTA.Usuário.toString()); // setar cmbbox inicial
		setContextoEditar(false); // iniciar em modo não-edição 
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// ---------- MigLayout
		mainPanel.add(cmbTipoConsulta);
		mainPanel.add(btnConsultar);
		mainPanel.add(cmbParametroConsulta);
		mainPanel.add(txtStringBusca);
		mainPanel.add(btnBuscar, "wrap");
		mainPanel.add(tblResultadoScroll, "span 5 6");
		mainPanel.add(btnSalvar);
		mainPanel.add(btnCancelar);
		mainPanel.add(btnRemover, "wrap");
		mainPanel.add(lblNome);
		mainPanel.add(txtNome, "wrap");
		mainPanel.add(lblLogin);
		mainPanel.add(txtLogin, "wrap");
		mainPanel.add(lblStatus);
		mainPanel.add(txtStatus, "wrap");
		mainPanel.add(lblGerencia);
		mainPanel.add(txtGerencia, "wrap");
		
		frame.getContentPane().add(mainPanel);
		frame.pack();
		frame.setVisible(true);
		
		// ----------- Listeners: Click para consultar ----------------
		btnConsultar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					setContextoEditar(false); // desabilitar contexto de edição
					String tipo = String.valueOf(cmbTipoConsulta.getSelectedItem());
					if(tipo.equals(TIPO_CONSULTA.Usuário.toString())) {
						List<User> users = Client.getServer().getUsers();
						popularTabelaResultadosComUsuarios(users, tipo);
					} else if(tipo.equals(TIPO_CONSULTA.Plugin.toString())) {
						List<Plugin> plugins = Client.getServer().getPlugins();
						popularTabelaResultadosComPlugin(plugins, tipo);
					}
				} catch (ServerServiceException err) {
					exibirDialogError(err.getMessage());
				} catch (RemoteException err) {
					exibirDialogError(Const.ERROR_REMOTE_EXCEPT);
				} catch (NotBoundException err) {
					exibirDialogError(Const.ERROR_NOTBOUND_EXCEPT);
				}
			}
		});
		
		// ----------- Listeners: Click para cancelar edição em item ---------------
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				setContextoEditar(false);
			}
		});
		
		// ----------- Listeners: Atualizar cmb de parâmetros na troca do cmb de consulta ----------------
		cmbTipoConsulta.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				String tipo = String.valueOf(cmbTipoConsulta.getSelectedItem());
				atualizarCmbParametros(tipo);
			}
		});
		
		// ----------- Listeners: Selecionar item na tabela
		tblResultado.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
	        public void valueChanged(ListSelectionEvent event) {
	        	int linhaSelecionada = tblResultado.getSelectedRow();
				if(linhaSelecionada > -1) {
		        	txtNome.setText(tblResultado.getValueAt(linhaSelecionada, 0).toString());
		        	txtLogin.setText(tblResultado.getValueAt(linhaSelecionada, 1).toString());
		        	txtStatus.setText(tblResultado.getValueAt(linhaSelecionada, 2).toString());
		        	txtGerencia.setText(tblResultado.getValueAt(linhaSelecionada, 3).toString());
		        	setContextoEditar(true);
				};
	        }
	    });
	}
	
	/**
	 * Método para atualizar o ComboBox para busca por parâmetro
	 */
	private void atualizarCmbParametros(String tipo) {
		cmbParametroConsulta.removeAllItems();
		if(tipo.equals(TIPO_CONSULTA.Usuário.toString())) {
			cmbParametroConsulta.addItem(FILTROS_USUARIO.Login.toString());
			cmbParametroConsulta.addItem(FILTROS_USUARIO.Nome.toString());
			cmbParametroConsulta.addItem(FILTROS_USUARIO.Status.toString());
			cmbParametroConsulta.addItem(FILTROS_USUARIO.Gerência.toString());
		}
		else if(tipo.equals(TIPO_CONSULTA.Plugin.toString()) || tipo.equals(TIPO_CONSULTA.Funcionalidade.toString())) {
			cmbParametroConsulta.addItem(FILTROS_PLUGIN_FUNC.Nome.toString());
			cmbParametroConsulta.addItem(FILTROS_PLUGIN_FUNC.Descrição.toString());
			cmbParametroConsulta.addItem(FILTROS_PLUGIN_FUNC.Data.toString());
		}
	}
	
	/**
	 * Método para popular tabela de resultados de busca com lista de usuários
	 * @param users - Lista contendo os usuários a serem apresentados na tabela
	 * @param tipo - tipo para gerar Header da tabela
	 */
	private void popularTabelaResultadosComUsuarios(List<User> users, String tipo) {
		Vector<Vector<Object>> dadosFinal = new Vector<Vector<Object>>();
		Vector<Object> linha;
		for(User usr : users) {
			linha = new Vector<Object>();
			linha.add(usr.getNome());
			linha.add(usr.getLogin());
			linha.add(usr.getStatus());
			linha.add(usr.getGerenciaAtual());
			dadosFinal.add(linha);
		};
		tblResultado.setModel(new DefaultTableModel(dadosFinal, gerarHeader(tipo)));
	}
	
	/**
	 * Método para popular tabela de resultados de busca com lista de plugins
	 * @param users - Lista contendo os usuários a serem apresentados na tabela
	 * @param tipo - tipo para gerar Header da tabela
	 */
	private void popularTabelaResultadosComPlugin(List<Plugin> plugins, String tipo) {
		Vector<Vector<Object>> dadosFinal = new Vector<Vector<Object>>();
		Vector<Object> linha;
		for(Plugin plugin : plugins) {
			linha = new Vector<Object>();
			linha.add(plugin.getNome());
			linha.add(plugin.getDescricao());
			linha.add(plugin.getDataCriacao());
			dadosFinal.add(linha);
		};
		tblResultado.setModel(new DefaultTableModel(dadosFinal, gerarHeader(tipo)));
	}

	/**
	 * Método para gerar vetor de strings com o cabeçalho da tabela de 
	 * resultados de busca.
	 * @param tipo - tipo de consulta para definir o cabeçalho
	 * @return - retorna um Vector<String> com o nome de cada coluna
	 */
	private Vector<String> gerarHeader(String tipo) {
		Vector<String> header = new Vector<String>();
		if(tipo.equals(TIPO_CONSULTA.Usuário.toString())) {
			header.add("NOME");
			header.add("LOGIN");
			header.add("STATUS");
			header.add("GERÊNCIA ATUAL");
		} else if(tipo.equals(TIPO_CONSULTA.Plugin.toString())) {
			header.add("NOME");
			header.add("DESCRIÇÃO");
			header.add("DATA DE CRIAÇÃO");
		}
		return header;
	}
	
	/**
	 * Método para exibir DialogBox de algum erro.
	 * @param msg - mensagem a ser exibida no DialogBox 
	 */
	private void exibirDialogError(String msg) {
		JOptionPane.showMessageDialog(frame, msg, "Error", JOptionPane.ERROR_MESSAGE);
	}
	
	/**
	 * Método para trocar contexto de edição e contexto 
	 * fora de edição
	 * @param setar - bool desejado para setar os campos
	 */
	private void setContextoEditar(boolean setar) {
		btnRemover.setEnabled(setar);
		btnSalvar.setEnabled(setar);
		btnCancelar.setEnabled(setar);
		txtNome.setEditable(setar);
		txtLogin.setEditable(setar);
		txtStatus.setEditable(setar);
		txtGerencia.setEditable(setar);
	}
}
