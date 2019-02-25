package client.ui.javafx;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import client.Client;
import client.exceptions.UICheckFieldException;
import client.ui.UIEnums.ABAS;
import client.ui.UIEnums.FILTROS_PERFIL;
import common.exceptions.ServerServiceException;
import common.model.BusinessEntity;

public class PluginTab extends GenericTab {

	private static final long serialVersionUID = -8732504319991640724L;
	
	private TextField txtName = new TextField();
	private TextField txtDescription = new TextField();
	private DatePicker dpCreationDate = new DatePicker();
	
	public PluginTab() {
		super();
		this.setText(ABAS.Plugin.toString());
		populateFormPane();
		createTableAllItemsHeader();
	}
	
	@Override
	public void createTableAllItemsHeader() {
		TableColumn nameColumn = new TableColumn<>("Name");
		nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
		TableColumn descriptionColumn = new TableColumn<>("Description");
		descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
		TableColumn creationDateColumn = new TableColumn<>("Creation Date");
		creationDateColumn.setCellValueFactory(new PropertyValueFactory<>("dataCriacao"));
		getTableAllItems().getColumns().addAll(nameColumn, descriptionColumn, creationDateColumn);
	}

	@Override
	public void loadData() throws RemoteException, ServerServiceException, NotBoundException {
		getTableAllItems().getItems().clear();
		getTableAllItems().getItems().addAll((FXCollections.observableArrayList(Client.getServer().getPlugins())));
	}

	@Override
	protected List<String> createSearchOptions() {
		List<String> out = new ArrayList<String>();
		out.add(FILTROS_PERFIL.Nome.toString());
		out.add(FILTROS_PERFIL.Descrição.toString());
		out.add(FILTROS_PERFIL.Data.toString());
		return out;
	}

	@Override
	protected void populateFormPane() {
		getFormPane().add(new Label("Name: "));
		getFormPane().add(txtName, "growx, wrap");
		getFormPane().add(new Label("Description: "));
		getFormPane().add(txtDescription, "growx, wrap");
		getFormPane().add(new Label("Creation Date: "));
		getFormPane().add(dpCreationDate, "growx, wrap");
	}

	@Override
	public BusinessEntity createObjToBeSaved() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setContextoEditar(int selectedRowToEdit) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean checkFieldsOnCreate() throws UICheckFieldException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<? extends BusinessEntity> realizarBusca(String atributo, String termo)
			throws RemoteException, ServerServiceException, NotBoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void popularTabelaResultado(List<? extends BusinessEntity> resultadoConsulta) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void realizarDelete(Long id) throws RemoteException, ServerServiceException, NotBoundException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void realizarCreate(BusinessEntity objToSave)
			throws RemoteException, ServerServiceException, NotBoundException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void realizarUpdate(BusinessEntity objToSave)
			throws RemoteException, ServerServiceException, NotBoundException {
		// TODO Auto-generated method stub
		
	}
}
