package client.ui.javafx;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
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
import common.model.Functionality;
import common.model.Plugin;

public class FunctionalityTab extends GenericTab {
	
	private static final long serialVersionUID = -5689122862752332650L;
	
	private ComboBox<Plugin> cmbPlugin = new ComboBox<Plugin>();
	private TextField txtName = new TextField();
	private TextField txtDescription = new TextField();
	private DatePicker dpCreationDate = new DatePicker();
	
	public FunctionalityTab() {
		super(ABAS.Funcionalidade);
		this.setText(ABAS.Funcionalidade.toString());
		initPnlForm();
		createTableAllItemsHeader();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void createTableAllItemsHeader() {
		TableColumn pluginColumn = new TableColumn<>("Plugin");
		pluginColumn.setCellValueFactory(new PropertyValueFactory<>("plugin"));
		TableColumn nameColumn = new TableColumn<>("Name");
		nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
		TableColumn descriptionColumn = new TableColumn<>("Description");
		descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
		TableColumn creationDateColumn = new TableColumn<>("Creation Date");
		creationDateColumn.setCellValueFactory(new PropertyValueFactory<>("dataCriacao"));
		getTableAllItems().getColumns().addAll(pluginColumn, nameColumn, descriptionColumn, creationDateColumn);
	}

	@Override
	public void loadData() throws RemoteException, ServerServiceException, NotBoundException {
		populateTableAllItems(Client.getServer().getFunctionalities());
		cmbPlugin.getItems().clear();
		cmbPlugin.getItems().addAll(FXCollections.observableArrayList(Client.getServer().getPlugins()));
		setContext(FORM_CONTEXT.Proibido);
	}

	@Override
	public void initPnlForm() {
		getFormPane().add(new Label("Plugin: "));
		getFormPane().add(cmbPlugin, "growx, wrap");
		getFormPane().add(new Label("Name: "));
		getFormPane().add(txtName, "growx, wrap");
		getFormPane().add(new Label("Description: "));
		getFormPane().add(txtDescription, "growx, wrap");
		getFormPane().add(new Label("Creation Date: "));
		dpCreationDate.setDisable(true);
		getFormPane().add(dpCreationDate, "growx, wrap");
	}

	@Override
	public BusinessEntity createObjToBeSaved() {
		String name = txtName.getText();
		String desc = txtDescription.getText();
		Date creationDate = new Date();
		Functionality func = new Functionality(null, name, desc, creationDate);
		Plugin plugin = cmbPlugin.getSelectionModel().getSelectedItem();
		func.setPlugin(plugin);
		return func;
	}

	@Override
	public void populateConsultComboBox() {
		getCmbConsult().getItems().add(UIEnums.FILTROS_FUNCIONALIDADE.Plugin.toString());
		getCmbConsult().getItems().add(UIEnums.FILTROS_FUNCIONALIDADE.Nome.toString());
		getCmbConsult().getItems().add(UIEnums.FILTROS_FUNCIONALIDADE.Descrição.toString());
		getCmbConsult().getItems().add(UIEnums.FILTROS_FUNCIONALIDADE.Data.toString());
	}

	@Override
	public void fillFormToEdit(int selectedRow) throws RemoteException, ServerServiceException, NotBoundException {
		Functionality func = (Functionality) getTableAllItems().getSelectionModel().getSelectedItem();
		txtName.setText(func.getName());
		txtDescription.setText(func.getDescription());
		dpCreationDate.setValue(convertDateToLocalDate(func.getDataCriacao()));
		cmbPlugin.getSelectionModel().select(func.getPlugin());
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
		cmbPlugin.setDisable(!b);
	}

	@Override
	public boolean checkFieldsOnCreate() throws UICheckFieldException {
		int value = cmbPlugin.getSelectionModel().getSelectedIndex();
		if(value < 0) 
			throw new UICheckFieldException(Const.INFO_EMPTY_FIELD.replace("?1", "plugin"));
		String aux = txtName.getText(); 
		if(aux == null || aux.isEmpty() || aux.equals("")) 
			throw new UICheckFieldException(Const.INFO_EMPTY_FIELD.replace("?1", "nome"));
		return true;
	}

	@Override
	public List<? extends BusinessEntity> realizarBusca(String atributo, String termo)
			throws RemoteException, ServerServiceException, NotBoundException {
		return Client.getServer().searchFunctionalities(atributo, termo);
	}

	@Override
	public void realizarDelete(Long id) throws RemoteException, ServerServiceException, NotBoundException {
		Client.getServer().deleteFunctionality(id);
	}

	@Override
	public void realizarCreate(BusinessEntity objToSave)
			throws RemoteException, ServerServiceException, NotBoundException {
		Client.getServer().createFunctionality((Functionality)objToSave);
		atualizarCacheTodasFuncBanco();
	}

	@Override
	public void realizarUpdate(BusinessEntity objToSave)
			throws RemoteException, ServerServiceException, NotBoundException {
		Client.getServer().updateFunctionality((Functionality)objToSave);
	}
	
}
