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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import client.Client;
import common.Const;
import exception.DBConnectException;
import exception.DBConsultException;
import model.User;
import net.miginfocom.swing.MigLayout;

public class GUI implements Serializable {
	private static final long serialVersionUID = -188826826138600878L;
	
	public static void init() {
		final JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// ---------- Components: Consulta
		String[] tiposConsulta = {TIPO_CONSULTA.Usuário.toString(), TIPO_CONSULTA.Plugin.toString(), TIPO_CONSULTA.Funcionalidade.toString()};
		final JComboBox<String> cmbTipoConsulta = new JComboBox<String>(tiposConsulta);
		final JButton btnConsultar = new JButton("Consultar");
		final JTable tblResultado = new JTable(); 
		tblResultado.setAutoCreateRowSorter(true);
        final JScrollPane tblResultadoScroll = new JScrollPane(tblResultado);
		final JComboBox<String> cmbParametroConsulta = new JComboBox<String>();
		final JTextField txtStringBusca = new JTextField(15);
		final JButton btnBuscar = new JButton("Buscar");
		final JButton btnEditar = new JButton("Editar");
		final JButton btnRemover = new JButton("Remover");
		
		
		// ---------- MigLayout
		JPanel mainPanel = new JPanel(new MigLayout());
		mainPanel.add(cmbTipoConsulta);
		mainPanel.add(btnConsultar, "wrap");
		mainPanel.add(tblResultadoScroll, "span 3");
		mainPanel.add(btnEditar);
		mainPanel.add(btnRemover, "wrap");
		mainPanel.add(cmbParametroConsulta);
		mainPanel.add(txtStringBusca);
		mainPanel.add(btnBuscar);
		
		frame.getContentPane().add(mainPanel);
		frame.pack();
		frame.setVisible(true);
		
		// ----------- Listeners: Click para consultar ----------------
		btnConsultar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String tipo = String.valueOf(cmbTipoConsulta.getSelectedItem());
				if(tipo.equals(TIPO_CONSULTA.Usuário.toString())) {
					try {
						List<User> users = Client.getServer().getUsers();
						popularTabelaResultadosComUsuarios(tblResultado, users, tipo);
					} catch (DBConsultException err) {
						JOptionPane.showMessageDialog(frame, err.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					} catch (DBConnectException err) {
						JOptionPane.showMessageDialog(frame, err.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					} catch (RemoteException err) {
						JOptionPane.showMessageDialog(frame, Const.ERROR_REMOTE_EXCEPT, "Error", JOptionPane.ERROR_MESSAGE);
					} catch (NotBoundException err) {
						JOptionPane.showMessageDialog(frame, Const.ERROR_NOTBOUND_EXCEPT, "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		
		// ----------- Listeners: Atualizar cmb de parâmetros na troca do cmb de consulta ----------------
		cmbTipoConsulta.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				cmbParametroConsulta.removeAllItems();
				String tipo = String.valueOf(cmbTipoConsulta.getSelectedItem());
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
		});
	}
	
	private static void popularTabelaResultadosComUsuarios(JTable tblResultado, List<User> users, String tipo) {
		Vector<Vector<Object>> dadosFinal = new Vector<Vector<Object>>();
		Vector<Object> linha;
		for(User usr : users) {
			linha = new Vector<Object>();
			linha.add(usr.getId());
			linha.add(usr.getNome());
			linha.add(usr.getLogin());
			linha.add(usr.getStatus());
			linha.add(usr.getGerenciaAtual());
			dadosFinal.add(linha);
		};
		tblResultado.setModel(new DefaultTableModel(dadosFinal, gerarHeader(tipo)));
	}

	private static Vector<String> gerarHeader(String tipo) {
		Vector<String> header = new Vector<String>();
		if(tipo.equals(TIPO_CONSULTA.Usuário.toString())) {
			header.add("ID");
			header.add("NOME");
			header.add("LOGIN");
			header.add("STATUS");
			header.add("GERÊNCIA ATUAL");
		}
		return header;
	}

}
