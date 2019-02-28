package client.ui.javafx;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import client.Client;
import client.exceptions.UICheckFieldException;
import client.ui.UIEnums;
import client.ui.UIEnums.ABAS;
import client.ui.UIEnums.FORM_CONTEXT;
import common.Const;
import common.exceptions.ServerServiceException;
import common.model.BusinessEntity;
import common.model.Plugin;

public class PluginTab extends GenericTab {

	private static final long serialVersionUID = -8732504319991640724L;
	
	private TextField txtName = new TextField();
	private TextField txtDescription = new TextField();
	private DatePicker dpCreationDate = new DatePicker();
	
	public PluginTab() {
		super(ABAS.Plugin);
		this.setText(ABAS.Plugin.toString());
		initPnlForm();
		createTableAllItemsHeader();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
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
		populateTableAllItems(Client.getServer().getPlugins());
		setContext(FORM_CONTEXT.Proibido);
	}
	
	@Override
	public BusinessEntity createObjToBeSaved() {
		String name = txtName.getText();
		String desc = txtDescription.getText();
		Date creationDate = new Date();
		return new Plugin(null, name, desc, creationDate);
	}

	@Override
	public void fillFormToEdit(int selectedRow) throws RemoteException, ServerServiceException, NotBoundException {
		Plugin plg = (Plugin) getTableAllItems().getSelectionModel().getSelectedItem();
		txtName.setText(plg.getName());
		txtDescription.setText(plg.getDescription());
		dpCreationDate.setValue(convertDateToLocalDate(plg.getDataCriacao()));
	}

	@Override
	public void initPnlForm() {
		getFormPane().add(new Label("Name: "));
		getFormPane().add(txtName, "growx, wrap");
		getFormPane().add(new Label("Description: "));
		getFormPane().add(txtDescription, "growx, wrap");
		getFormPane().add(new Label("Creation Date: "));
		dpCreationDate.setDisable(true);
		getFormPane().add(dpCreationDate, "growx, wrap");
	}

	@Override
	public void clearForm() {
		txtName.setText("");
		txtDescription.setText("");
		dpCreationDate.setValue(LocalDate.now());
	}

	@Override
	public void setEnabledForm(boolean b) {
		txtName.setDisable(!b);
		txtDescription.setDisable(!b);
	}

	@Override
	public boolean checkFieldsOnCreate() throws UICheckFieldException {
		String aux = txtName.getText();
		if(aux == null || aux.equals("") || aux.isEmpty()) 
				throw new UICheckFieldException(Const.INFO_EMPTY_FIELD.replace("?1", "nome"));
		return true;
	}

	@Override
	public List<? extends BusinessEntity> realizarBusca(String atributo, String termo)
			throws RemoteException, ServerServiceException, NotBoundException {
		return Client.getServer().searchPlugins(atributo, termo);
	}
	
	@Override
	public void realizarDelete(Long id) throws RemoteException, ServerServiceException, NotBoundException {
		Client.getServer().deletePlugin(id);
	}

	@Override
	public void realizarCreate(BusinessEntity objToSave)
			throws RemoteException, ServerServiceException, NotBoundException {
		Client.getServer().createPlugin((Plugin)objToSave);
	}

	@Override
	public void realizarUpdate(BusinessEntity objToSave)
			throws RemoteException, ServerServiceException, NotBoundException {
		Client.getServer().updatePlugin((Plugin)objToSave);
	}

	@Override
	public void populateConsultComboBox() {
		getCmbConsult().getItems().add(UIEnums.FILTROS_PLUGIN.Nome.toString());
		getCmbConsult().getItems().add(UIEnums.FILTROS_PLUGIN.Descrição.toString());
		getCmbConsult().getItems().add(UIEnums.FILTROS_PLUGIN.Data.toString());
	}
}
