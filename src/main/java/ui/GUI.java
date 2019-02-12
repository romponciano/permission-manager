package ui;

import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import common.Const;
import exception.ServerServiceException;

public class GUI implements Serializable {
	private static final long serialVersionUID = -188826826138600878L;
	
	JFrame frame;
	JTabbedPane abasContainer;
	AbaGenerica abaUsuario;
	AbaGenerica abaPlugin;
	
	public GUI() {
		frame = new JFrame();
		abasContainer = new JTabbedPane();
		abaUsuario = new AbaUsuario(frame);
		abaPlugin = new AbaPlugin(frame);
	}
	
	/**
	 * Inicializa a GUI
	 */
	public void init() {
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		abasContainer.add(abaUsuario, ABAS.Usuário.toString());
		abasContainer.add(abaPlugin, ABAS.Plugin.toString());
		frame.setContentPane(abasContainer);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		abasContainer.setSelectedIndex(1); // trocar para segunda aba
		
		// ao clicar na aba, verificar se ela possui tabela populada. Caso não possuir, então carregar dados
		abasContainer.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent changeEvent) {
				JTabbedPane sourceTabbedPane = (JTabbedPane) changeEvent.getSource();
				int index = sourceTabbedPane.getSelectedIndex();
				try {  
					if(sourceTabbedPane.getTitleAt(index).equals(ABAS.Usuário.toString()) && abaUsuario.getTblResultado().getRowCount() <= 0) {
						abaUsuario.loadData();
					}
					else if(sourceTabbedPane.getTitleAt(index).equals(ABAS.Plugin.toString()) && abaPlugin.getTblResultado().getRowCount() <= 0) {
						abaPlugin.loadData();
					}
					else if(sourceTabbedPane.getTitleAt(index).equals(ABAS.Funcionalidade.toString()) && abaPlugin.getTblResultado().getRowCount() <= 0) {
						
					}
				} 
				catch (ServerServiceException err) { exibirDialogError(err.getMessage()); } 
				catch (RemoteException err) { exibirDialogError(Const.ERROR_REMOTE_EXCEPT); } 
				catch (NotBoundException err) { exibirDialogError(Const.ERROR_NOTBOUND_EXCEPT); }
			};
		});
		
		abasContainer.setSelectedIndex(0); // voltar para primeira e, assim, disparar evento para carregar dados
	}


	/**
	 * Método para exibir DialogBox de algum <b>erro</b>.
	 * @param msg - mensagem a ser exibida no DialogBox
	 */
	private void exibirDialogError(String msg) {
		JOptionPane.showMessageDialog(frame, msg, "Error", JOptionPane.ERROR_MESSAGE);
	}
	
		
	
	
	
}
