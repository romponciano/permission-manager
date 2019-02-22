package client.ui.javafx;

import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import common.ServerInterface;

public class Client extends Application implements Serializable {

	private static final long serialVersionUID = 7506781721369369176L;
	
	private static ServerInterface server;

	@Override
	public void start(Stage primaryStage) {
		Button btn = new Button();
		btn.setText("Say 'Hello World'");
		
		StackPane root = new StackPane();
		root.getChildren().add(btn);

		Scene scene = new Scene(root, 300, 250);

		primaryStage.setTitle("Hello World!");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public static ServerInterface getServer() throws RemoteException, NotBoundException {
		if (Client.server == null) {
			Registry registry = LocateRegistry.getRegistry("localhost", ServerInterface.RMI_PORT);
			Client.server = (ServerInterface) registry.lookup(ServerInterface.REFERENCE_NAME);
		}
		return Client.server;
	}

	public static void main(String[] args) {
		try {
			Application.launch(args);
		} catch (Exception e) {
			System.err.println("Client exception: " + e.toString());
			e.printStackTrace();
		}
	}
}
