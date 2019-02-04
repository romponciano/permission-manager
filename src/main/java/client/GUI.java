package client;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.JList;
import javax.swing.JScrollPane;

public class GUI {

	private JFrame frame;
	private int COMPONENT_HEIGHT_ẀITH_TEXT = 35;
	private int HORIZONTAL_ALIGN = 10;
	private int VERTICAL_ALIGN = 10;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI window = new GUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public GUI() {
		inicializarFrame();
	}


	private void inicializarFrame() {
		frame = new JFrame();
		frame.setBounds(100, 100, 750, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		// linha de consulta
		String[] tiposConsulta = {"Usuário", "Plugin", "Funcionalidade"};
		JComboBox cmbTipoConsulta = new JComboBox(tiposConsulta);
		JTextField txtStringBusca = new JTextField();
		JButton btnConsultar = new JButton("Consultar");
		// resultado da consulta
		JList lstResultadoConsulta = new JList();
		lstResultadoConsulta.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		lstResultadoConsulta.setSelectedIndex(0);
		lstResultadoConsulta.setVisibleRowCount(5);
		JScrollPane lstScrResultadoBusca = new JScrollPane(lstResultadoConsulta);
		// TODO: inserir components em layouts para não setar posições x, y da tela
		cmbTipoConsulta.setBounds(5, 5, 140, COMPONENT_HEIGHT_ẀITH_TEXT);
		txtStringBusca.setBounds(140 + HORIZONTAL_ALIGN, 5, 120, COMPONENT_HEIGHT_ẀITH_TEXT);
		btnConsultar.setBounds(280 + HORIZONTAL_ALIGN, 5, 110, COMPONENT_HEIGHT_ẀITH_TEXT);
		lstScrResultadoBusca.setBounds(5, COMPONENT_HEIGHT_ẀITH_TEXT + VERTICAL_ALIGN, 280, 350);
		
		frame.getContentPane().add(cmbTipoConsulta);
		frame.getContentPane().add(txtStringBusca);
		frame.getContentPane().add(btnConsultar);
		frame.getContentPane().add(lstScrResultadoBusca);
	}

}
