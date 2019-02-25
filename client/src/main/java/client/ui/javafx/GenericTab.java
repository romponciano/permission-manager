package client.ui.javafx;

import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;

import org.tbee.javafx.scene.layout.MigPane;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Tab;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import client.Client;
import client.ui.GenericUIFunctions;
import client.ui.UIEnums.FORM_CONTEXT;
import common.exceptions.ServerServiceException;
import common.model.BusinessEntity;
import common.model.Functionality;

public abstract class GenericTab extends Tab implements Serializable, GenericUIFunctions {

	private static final long serialVersionUID = 9025913767736072389L;
	
	private TableView<BusinessEntity> tableAllItems = new TableView<>();
	private ComboBox<String> cmbFiltro = new ComboBox<String>();
	private TextField txtSearchString = new TextField();
	private Button btnSearch = new Button("Search");
	private MigPane formPane = new MigPane("", "[al right][grow]", "[grow]");
	private Button btnRemove = new Button("Remove");
	private Button btnCancel = new Button("Cancel");
	private Button btnSave = new Button("Save");
	private Button btnNew = new Button("New");
	
	private List<Functionality> cacheTodasFuncionalidadesBanco;
	
	public GenericTab() {
		MigPane mainTabPane = new MigPane("", "[grow 70][grow 30]", "[][grow][]");
		mainTabPane.add(createSearchPane(), "pushy, wrap");
		mainTabPane.add(tableAllItems, "grow, spany");
		mainTabPane.add(formPane, "top, grow, wrap");
		mainTabPane.add(createControlPane(), "skip 1, growx");
		this.setContent(mainTabPane);
		this.setClosable(false);
		this.tableAllItems.getSelectionModel().setSelectionMode((SelectionMode.MULTIPLE));
	}
	
	private MigPane createControlPane() {
		MigPane controlPane = new MigPane("", "[][][][]", "");
		controlPane.add(btnRemove, "pushx");
		controlPane.add(btnCancel);
		controlPane.add(btnSave);
		controlPane.add(btnNew);
		return controlPane;
	}

	protected abstract void createTableAllItemsHeader();
	protected abstract List<String> createSearchOptions();
	protected abstract void populateFormPane();
	
	public void initListeners() {
		btnNew.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try { loadData(); } catch (RemoteException | ServerServiceException | NotBoundException e) { /* ignorar */ }
				setContext(FORM_CONTEXT.Criar);
			}
		});
	}
	
	private MigPane createSearchPane() {
		MigPane searchPane = new MigPane("ins 0", "[grow]", "");
		searchPane.add(new Label("Filtrar por:"));
		cmbFiltro = new ComboBox<String>(FXCollections.observableArrayList(createSearchOptions()));
		searchPane.add(cmbFiltro);
		searchPane.add(txtSearchString, "growx");
		searchPane.add(btnSearch);
		return searchPane;
	}
	
	public TableView<BusinessEntity> getTableAllItems() {
		return tableAllItems;
	}

	public MigPane getFormPane() {
		return formPane;
	}

	public void setFormPane(MigPane formPane) {
		this.formPane = formPane;
	}
	
	public List<Functionality> getCacheTodasFuncBanco() {
		return cacheTodasFuncionalidadesBanco;
	}

	public void atualizarCacheTodasFuncBanco() {
		try {
			cacheTodasFuncionalidadesBanco = Client.getServer().getFunctionalities();
		} catch (RemoteException | ServerServiceException | NotBoundException e) { }
	}
	
	// ######################################################

	@Override
	public void setContextoProibido() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setContextoCriar() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Long getIdToRemoveItem() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long getIdToUpdateItem() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void showInfoMessage(String message) {
		// TODO Auto-generated method stub
	}

	@Override
	public void showErrorMessage(String message) {
		// TODO Auto-generated method stub
	}
}
