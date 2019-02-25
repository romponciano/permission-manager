package client.ui.javafx;

import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;

import org.tbee.javafx.scene.layout.MigPane;

import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import common.exceptions.ServerServiceException;
import common.model.BusinessEntity;

public abstract class GenericTab extends Tab implements Serializable {

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
	
	public GenericTab() {
		MigPane mainTabPane = new MigPane("", "[grow 70][grow 30]", "[][grow][]");
		mainTabPane.add(createSearchPane(), "pushy, wrap");
		mainTabPane.add(tableAllItems, "grow, spany");
		mainTabPane.add(formPane, "top, grow, wrap");
		mainTabPane.add(createControlPane(), "skip 1, growx");
		this.setContent(mainTabPane);
		this.setClosable(false);
	}
	
	private MigPane createControlPane() {
		MigPane controlPane = new MigPane("", "[][][][]", "");
		controlPane.add(btnRemove, "pushx");
		controlPane.add(btnCancel);
		controlPane.add(btnSave);
		controlPane.add(btnNew);
		return controlPane;
	}

	protected abstract void loadData() throws RemoteException, ServerServiceException, NotBoundException;
	protected abstract void createTableAllItemsHeader();
	protected abstract List<String> createSearchOptions();
	protected abstract void populateFormPane();
	
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
}
