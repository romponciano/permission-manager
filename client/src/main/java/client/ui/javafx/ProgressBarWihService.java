package client.ui.javafx;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public abstract class ProgressBarWihService<R> extends ProgressBar {

	private Service<R> service;
	private Stage stage;

	public ProgressBarWihService() {
		super();
		stage = new Stage();
		configStage();
		service = createService();
		createOnScheduled();
		createOnSuccedded();
		this.progressProperty().bind(getService().progressProperty());
	}

	private void configStage() {
		stage.initStyle(StageStyle.UNDECORATED);
		stage.setAlwaysOnTop(true);
	}

	private Service<R> createService() {
		return new Service<R>() {
			@Override
			protected Task<R> createTask() {
				return new Task<R>() {
					@Override
					protected R call() throws Exception {
						return methodToBeLoad();
					}
				};
			}
		};
	}

	private void createOnScheduled() {
		getService().setOnScheduled(e -> {
			stage.setScene(new Scene(this));
			stage.show();
		});
	}

	private void createOnSuccedded() {
		getService().setOnSucceeded(e -> {
			stage.close();
			actionAfterSuccess(getResult());
		});
	}

	public abstract void actionAfterSuccess(R result);

	public abstract R methodToBeLoad() throws Exception;

	public void startService() {
		getService().restart();
	}

	public R getResult() {
		return getService().getValue();
	}

	public Service<R> getService() {
		return service;
	}
}
