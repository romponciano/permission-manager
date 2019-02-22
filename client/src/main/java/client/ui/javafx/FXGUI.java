package client.ui.javafx;

import java.io.Serializable;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class FXGUI extends Application implements Serializable {

	private static final long serialVersionUID = 7506781721369369176L;

	@Override
	public void start(Stage stage) {
		Button btn = new Button();
		btn.setText("Say 'Hello World'");
		
		StackPane root = new StackPane();
		root.getChildren().add(btn);

		Scene scene = new Scene(root, 300, 250);

		stage.setTitle("Hello World!");
		stage.setScene(scene);
		stage.show();
	}
}
