package client.ui.javafx;

import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

import common.exceptions.ServerServiceException;

public class FXGUI extends Application implements Serializable {

	private static final long serialVersionUID = 7506781721369369176L;	

	@Override
	public void start(Stage stage) {
		TabPane tabPane = new TabPane();
		GenericTab userTab = new UserTab();
		GenericTab pluginTab = new PluginTab();
		GenericTab funcTab = new FunctionalityTab();
		GenericTab profileTab = new ProfileTab();
		
		tabPane.getTabs().addAll(userTab, pluginTab, funcTab, profileTab);

		stage.setTitle("Gerenciador de Plugins");
		stage.setScene(new Scene(tabPane));
		stage.sizeToScene();
		stage.show();
		
		// selecionando uma tabela qualquer diferente da tabela inicial
		tabPane.getSelectionModel().select(pluginTab);
		
		tabPane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
			@Override
			public void changed(ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue) {
				try {
					if(newValue.equals(userTab)) userTab.loadData();
					else if(newValue.equals(pluginTab)) pluginTab.loadData(); 
					else if(newValue.equals(funcTab)) funcTab.loadData();
					else if(newValue.equals(profileTab)) profileTab.loadData();
				} catch (RemoteException | ServerServiceException | NotBoundException e) {
					userTab.dealWithError(e);
				}
			}
	    });
		
		// para poder selecionar a tabela inicial novamente e ativar o listener de carregamento
		tabPane.getSelectionModel().select(userTab);
	}
}