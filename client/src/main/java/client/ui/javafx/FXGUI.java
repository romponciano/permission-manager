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

import client.ui.UIEnums.ABAS;
import common.exceptions.ServerServiceException;

public class FXGUI extends Application implements Serializable {

	private static final long serialVersionUID = 7506781721369369176L;	

	@Override
	public void start(Stage stage) {
		TabPane tabPane = new TabPane();
		GenericTab userTab = new UserTab();
		GenericTab pluginTab = new PluginTab();
		GenericTab funcTab = new FunctionalityTab();
		Tab tabProfile = new Tab(ABAS.Perfil.toString());
		
		tabPane.getTabs().addAll(userTab, pluginTab, funcTab, tabProfile);

		stage.setTitle("Gerenciador de Plugins");
		stage.setScene(new Scene(tabPane));
		stage.sizeToScene();
		stage.show();
		
		tabPane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
			@Override
			public void changed(ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue) {
				try {
					if(newValue.equals(userTab)) userTab.loadData();
					else if(newValue.equals(pluginTab)) pluginTab.loadData(); 
					else if(newValue.equals(funcTab)) funcTab.loadData();
				} catch (RemoteException e) {
					System.out.println(e.getMessage());
				} catch (ServerServiceException e) {
					System.out.println(e.getMessage());
				} catch (NotBoundException e) {
					System.out.println(e.getMessage());
				}
			}
	    });
		
	}
}