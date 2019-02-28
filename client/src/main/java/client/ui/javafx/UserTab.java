package client.ui.javafx;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import org.tbee.javafx.scene.layout.MigPane;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
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
import common.model.Perfil;
import common.model.Status;
import common.model.User;

public class UserTab extends GenericTab {

	private static final long serialVersionUID = 96859842527558937L;

	private TextField txtName = new TextField();
	private TextField txtLogin = new TextField();
	private TextField txtGerencia = new TextField();
	private ComboBox<Status> cmbStatus = new ComboBox<Status>();
	private ComboBox<Perfil> cmbProfileList = new ComboBox<Perfil>();
	private Button btnAddProfile = new Button("+");
	private Button btnRemoveProfile = new Button("-");
	private ListView<Perfil> lstProfileList = new ListView<Perfil>();
	
	public UserTab() {
		super(ABAS.Usuário);
		this.setText(ABAS.Usuário.toString());
		initPnlForm();
		btnRemoveProfile.setDisable(true);
		createTableAllItemsHeader();
		btnAddProfile.setOnAction(createBtnAddProfileClickEvent());
		btnRemoveProfile.setOnAction(createBtnRemoveProfileClickEvent());
		lstProfileList.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			if (newSelection == null) {
		    	this.btnRemoveProfile.setDisable(true);
		    } else {
		    	this.btnRemoveProfile.setDisable(false);
		    }
		});
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void createTableAllItemsHeader() {
		TableColumn nameColumn = new TableColumn<>("Name");
		nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
		TableColumn loginColumn = new TableColumn<>("Login");
		loginColumn.setCellValueFactory(new PropertyValueFactory<>("login"));
		TableColumn statusColumn = new TableColumn<>("Status");
		statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
		TableColumn gerenciaColumn = new TableColumn<>("Gerência atual");
		gerenciaColumn.setCellValueFactory(new PropertyValueFactory<>("gerenciaAtual"));
		getTableAllItems().getColumns().addAll(nameColumn, loginColumn, statusColumn, gerenciaColumn);
	}

	@Override
	public void loadData() throws RemoteException, ServerServiceException, NotBoundException {
		populateTableAllItems(Client.getServer().getUsers());
		cmbStatus.getItems().clear();
		cmbStatus.getItems().addAll(FXCollections.observableArrayList(Client.getServer().getStatus()));
		cmbProfileList.getItems().clear();
		cmbProfileList.getItems().addAll(FXCollections.observableArrayList(Client.getServer().getPerfis()));
		setContext(FORM_CONTEXT.Proibido);
	}

	@Override
	public void initPnlForm() {
		getFormPane().add(new Label("Name: "));
		getFormPane().add(txtName, "growx, wrap");
		getFormPane().add(new Label("Login: "));
		getFormPane().add(txtLogin, "growx, wrap");
		getFormPane().add(new Label("Status: "));
		getFormPane().add(cmbStatus, "growx, wrap");
		getFormPane().add(new Label("Gerência Atual: "));
		getFormPane().add(txtGerencia, "growx, wrap");
		getFormPane().add(createProfileListForm(), "grow, spanx");
	}

	@Override
	public BusinessEntity createObjToBeSaved() {
		String name = txtName.getText();
		User user = new User(null, name);
		user.setStatus(cmbStatus.getSelectionModel().getSelectedItem());
		user.setLogin(txtLogin.getText());
		user.setGerenciaAtual(txtGerencia.getText());
		user.setPerfis(getProfilesFromList());
		return user;
	}

	@Override
	public void populateConsultComboBox() {
		getCmbConsult().getItems().add(UIEnums.FILTROS_USUARIO.Login.toString());
		getCmbConsult().getItems().add(UIEnums.FILTROS_USUARIO.Nome.toString());
		getCmbConsult().getItems().add(UIEnums.FILTROS_USUARIO.Gerência.toString());
		getCmbConsult().getItems().add(UIEnums.FILTROS_USUARIO.Status.toString());
	}

	@Override
	public void fillFormToEdit(int selectedRow) throws RemoteException, ServerServiceException, NotBoundException {
		User user = (User)getTableAllItems().getSelectionModel().getSelectedItem();
		txtName.setText(user.getName());
		txtLogin.setText(user.getLogin());
		txtGerencia.setText(user.getGerenciaAtual());
		cmbStatus.getSelectionModel().select(user.getStatus());
		List<Perfil> perfs = Client.getServer().searchUserProfilesByUserId(user.getId());
		lstProfileList.getItems().clear();
		lstProfileList.getItems().addAll(perfs);
	}

	@Override
	public void clearForm() {
		txtName.setText("");
		txtGerencia.setText("");
		txtLogin.setText("");
		lstProfileList.getItems().clear();
	}

	@Override
	public void setEnabledForm(boolean b) {
		txtName.setDisable(!b);
		txtGerencia.setDisable(!b);
		txtLogin.setDisable(!b);
		cmbStatus.setDisable(!b);
		lstProfileList.setDisable(!b);
		cmbProfileList.setDisable(!b);
		btnAddProfile.setDisable(!b);
	}

	@Override
	public boolean checkFieldsOnCreate() throws UICheckFieldException {
		String aux = txtLogin.getText();
		if(aux == null || aux.isEmpty() || aux.equals("")) throw new UICheckFieldException(Const.INFO_EMPTY_FIELD.replace("?1", "login"));
		if(aux.length() > 4) throw new UICheckFieldException(Const.INFO_BIG_FIELD.replace("?1", "login").replace("?2", "4"));
		return true;
	}

	@Override
	public List<? extends BusinessEntity> realizarBusca(String atributo, String termo)
			throws RemoteException, ServerServiceException, NotBoundException {
		return Client.getServer().getUsers();
	}

	@Override
	public void realizarDelete(Long id) throws RemoteException, ServerServiceException, NotBoundException {
		Client.getServer().deleteUser(id);
	}

	@Override
	public void realizarCreate(BusinessEntity objToSave)
			throws RemoteException, ServerServiceException, NotBoundException {
		Client.getServer().createUser((User)objToSave);
	}

	@Override
	public void realizarUpdate(BusinessEntity objToSave)
			throws RemoteException, ServerServiceException, NotBoundException {
		Client.getServer().updateUser((User)objToSave);
	}
	
	/**
	 * Método para criar ação do click no botão de remover perfil 
	 * da lista de perfis. 
	 */
	private EventHandler<ActionEvent> createBtnRemoveProfileClickEvent() {
		return new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				lstProfileList.getItems().remove(lstProfileList.getSelectionModel().getSelectedItem());
				btnRemoveProfile.setDisable(true);
			}
		};
	}

	/**
	 * Método para criar ação do click no botão de adicionar perfil 
	 * na lista de perfis.
	 */
	private EventHandler<ActionEvent> createBtnAddProfileClickEvent() {
		return new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Perfil perfSelecionado = cmbProfileList.getSelectionModel().getSelectedItem();
				// só add perfil na lista se este ainda não estiver adicionado
				if(!lstProfileList.getItems().contains(perfSelecionado)) {
					lstProfileList.getItems().add(perfSelecionado);
				}
				btnRemoveProfile.setDisable(true);
			}
		};
	}

	/** 
	 * Método responsável por criar o layout da lista de 
	 * perfis do usuário.
	 */
	private MigPane createProfileListForm() {
		MigPane profileListPane = new MigPane("", "[][][]", "[][grow]");
		profileListPane.add(cmbProfileList, "pushx");
		profileListPane.add(btnAddProfile);
		profileListPane.add(btnRemoveProfile, "wrap");
		profileListPane.add(lstProfileList, "grow, spanx");
		return profileListPane;
	}

	/**
	 * Método responsável por pegar os perfis presentes
	 * na lista de perfis
	 * @return - lista com os perfis existentes na lista
	 * de perfis
	 */
	private List<Perfil> getProfilesFromList() {
		List<Perfil> perfs = new ArrayList<Perfil>();
		lstProfileList.getItems().forEach(perfil -> { perfs.add(perfil); });
		return perfs;
	}
}
