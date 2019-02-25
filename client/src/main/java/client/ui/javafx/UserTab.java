package client.ui.javafx;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import org.tbee.javafx.scene.layout.MigPane;

import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import client.Client;
import client.ui.UIEnums.ABAS;
import client.ui.UIEnums.FILTROS_USUARIO;
import common.exceptions.ServerServiceException;
import common.model.Perfil;
import common.model.Status;

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
		super();
		this.setText(ABAS.Usuário.toString());
		populateFormPane();
		createTableAllItemsHeader();
	}
	
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
		getTableAllItems().getItems().clear();
		getTableAllItems().getItems().addAll((FXCollections.observableArrayList(Client.getServer().getUsers())));
		cmbStatus.getItems().clear();
		cmbStatus.getItems().addAll(FXCollections.observableArrayList(Client.getServer().getStatus()));
		cmbProfileList.getItems().clear();
		cmbProfileList.getItems().addAll(FXCollections.observableArrayList(Client.getServer().getPerfis()));
	}

	@Override
	protected List<String> createSearchOptions() {
		List<String> out = new ArrayList<String>();
		out.add(FILTROS_USUARIO.Nome.toString());
		out.add(FILTROS_USUARIO.Login.toString());
		out.add(FILTROS_USUARIO.Status.toString());
		out.add(FILTROS_USUARIO.Gerência.toString());
		return out;
	}

	@Override
	protected void populateFormPane() {
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

	private MigPane createProfileListForm() {
		MigPane profileListPane = new MigPane("", "[][][]", "[][grow]");
		profileListPane.add(cmbProfileList, "pushx");
		profileListPane.add(btnAddProfile);
		profileListPane.add(btnRemoveProfile, "wrap");
		profileListPane.add(lstProfileList, "grow, spanx");
		return profileListPane;
	}
	
	
	
	
}
