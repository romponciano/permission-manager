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
import model.Plugin;
import net.miginfocom.swing.MigLayout;

public class AbaPlugin extends AbaGenerica {

	private static final long serialVersionUID = -8445952309777454337L;
	
	private JLabel lblNomePlugin = new JLabel("Plugin: ");
	private JLabel lblDescricao = new JLabel("Descrição: ");
	private JLabel lblDataCriacao = new JLabel("Data de criação: ");
	private JTextField txtNomePlugin = new JTextField(15);
	private JTextField txtDescricao = new JTextField(15);
	private JTextField txtDataCriacao = new JTextField(15);

	public AbaPlugin() {
		super();
		initPnlForm();
		setContextoEditar(false);
	}
	
	private void initPnlForm() {
		JPanel pnlForm = new JPanel(new MigLayout("","[right][grow]",""));
		pnlForm.add(lblNomePlugin);
		pnlForm.add(txtNomePlugin, "wrap, growx");
		pnlForm.add(lblDescricao);
		pnlForm.add(txtDescricao, "wrap, growx");
		pnlForm.add(lblDataCriacao);
		pnlForm.add(txtDataCriacao, "wrap, growx");		
		registerForm(pnlForm);
	}
	
	@Override
	public void setContextoEditar(boolean setar) {
		super.setContextoEditar(setar);
		txtNomePlugin.setEditable(setar);
		txtDescricao.setEditable(setar);
		txtDataCriacao.setEditable(false);
	}
	
	@Override
	public Vector<String> createItemsCmbConsulta() {
		Vector<String> out = new Vector<String>();
		out.add(FILTROS_PLUGIN.Nome.toString());
		out.add(FILTROS_PLUGIN.Descrição.toString());
		out.add(FILTROS_PLUGIN.Data.toString());
		return out;
	}
	
	@Override
	public Vector<String> gerarHeader() {
		Vector<String> header = new Vector<String>();
		header.add("NOME");
		header.add("DESCRIÇÃO");
		header.add("DATA DE CRIAÇÃO");
		return header;
	}
	
	/**
	 * Método para popular tabela de resultados de busca com lista de plugins
	 * @param plugins - Lista contendo os plugins a serem apresentados na tabela
	 * @param tipo - tipo para gerar Header da tabela
	 */
	public void popularTabelaResultado(List<Plugin> plugins) {
		Vector<Vector<Object>> dadosFinal = new Vector<Vector<Object>>();
		Vector<Object> linha;
		for(Plugin plg : plugins) {
			linha = new Vector<Object>();
			linha.add(plg.getNome());
			linha.add(plg.getDescricao());
			linha.add(plg.getDataCriacao());
			dadosFinal.add(linha);
		};
		this.getTblResultado().setModel(new DefaultTableModel(dadosFinal, gerarHeader()));
	}

	@Override
	public void loadData() throws RemoteException, ServerServiceException, NotBoundException {
		setContextoEditar(false);
		List<Plugin> plugins = Client.getServer().getPlugins();
		popularTabelaResultado(plugins);
		
	}
	
	@Override
	public void initListeners() {
		super.initListeners();
		setOnItemSelectListenerTableResult(
				new ListSelectionListener() {
					public void valueChanged(ListSelectionEvent event) {
						int linhaSelecionada = getTblResultado().getSelectedRow();
						if (linhaSelecionada > -1) {
							txtNomePlugin.setText(getTblResultado().getValueAt(linhaSelecionada, 0).toString());
							txtDescricao.setText(getTblResultado().getValueAt(linhaSelecionada, 1).toString());
							txtDataCriacao.setText(getTblResultado().getValueAt(linhaSelecionada, 2).toString());
							setContextoEditar(true);
						};
					}
				}
		);
	}

	
}
