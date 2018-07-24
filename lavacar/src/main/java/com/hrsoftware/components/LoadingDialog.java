package com.hrsoftware.components;

import java.io.IOException;

import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class LoadingDialog {

	private ProgressForm pForm = new ProgressForm();

	public void start(TaskManager taskManager) {

		// PROGRESS
		Task<Void> task = new Task<Void>() {

			@Override
			public Void call() throws InterruptedException {
				taskManager.doIt();
				return null;
			}
		};

		// binds progress of progress bars to progress of task:
		pForm.activateProgressBar(task);

		task.setOnSucceeded(event -> {

			pForm.getDialogStage().close();

		});

		task.setOnFailed(event -> {

			pForm.getDialogStage().close();
			System.out.println(event.getSource().getException());
			System.out.println(event.getSource().getTitle());
			System.out.println(event.getSource().getMessage());

		});

		pForm.getDialogStage().show();

		Thread thread = new Thread(task);
		thread.start();

	}

	public class ProgressForm {
		private final Stage dialogStage;
		private final ProgressBar pb = new ProgressBar();
		private final ProgressIndicator pin = new ProgressIndicator();

		public ProgressForm() {

			dialogStage = new Stage();
			dialogStage.initStyle(StageStyle.UTILITY);
			dialogStage.setResizable(false);
			dialogStage.setAlwaysOnTop(true);
			dialogStage.initModality(Modality.APPLICATION_MODAL);

			pb.setProgress(-1F);
			pin.setProgress(-1F);

			FXMLLoader loader = new FXMLLoader(getClass().getResource("view/Loading.fxml"));

			Scene scene = null;

			try {
				scene = new Scene(loader.load());
			} catch (IOException e) {
				e.printStackTrace();
			}
			dialogStage.setScene(scene);
		}

		public void activateProgressBar(final Task<?> task) {
			dialogStage.show();
			dialogStage.toFront();
		}

		public Stage getDialogStage() {
			return dialogStage;
		}
	}

	public ProgressForm getpForm() {
		return pForm;
	}

}
