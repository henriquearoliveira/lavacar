package com.hrsoftware.login;

import java.net.URL;
import java.util.ResourceBundle;

import com.hrsoftware.GerenciadorStage;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

public class SplashFXMLController implements Initializable {

	@FXML
	private StackPane rootPane;

	@Override
	public void initialize(URL url, ResourceBundle rb) {

		// FECHA A JANELA DO LOGIN
		GerenciadorStage.getInstance().getStage().close();

		rootPane.setOpacity(0);
		fazFadeIn();

	}

	private void fazFadeIn() {

		FadeTransition ft = new FadeTransition(Duration.seconds(2), rootPane);
		ft.setFromValue(0);
		ft.setToValue(1);
		ft.setOnFinished(e -> {
			new SplashScreen().start();
		});
		ft.play();

	}

	class SplashScreen extends Thread {

		@Override
		public void run() {

			try {
				Thread.sleep(1000);

				Platform.runLater(() -> {

					GerenciadorStage.getInstance().showPrincipalFXML("dashboard/view/Principal.fxml");

					rootPane.getScene().getWindow().hide();
				});

			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}

	}

}
