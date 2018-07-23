package com.hrsoftware;

import java.io.IOException;

import com.hrsoftware.relatorios.RelatorioBuilder;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

	@Override
	public void start(Stage stageRoot) throws IOException {

		/* lançaJPA(); */

		RelatorioBuilder.deletaRelatoriosTemp();

		FXMLLoader loader = new FXMLLoader(getClass().getResource("login/view/Login.fxml"));

		Parent root = loader.load();

		GerenciadorStage.getInstance().setStage(stageRoot);

		root.setStyle("-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.4), 10, 0.5, 0.0, 0.0);"
				+ "-fx-background-color: #fafafa;");
		Scene scene = new Scene(root);

		stageRoot.setScene(scene);
		stageRoot.setResizable(false);
		stageRoot.setTitle("Login");
		stageRoot.initStyle(StageStyle.UNDECORATED);
		stageRoot.show();

	}

	/*private void lançaJPA() {
		TesteDBDAO dao = new TesteDBDAO();
		dao.seleciona();
	}*/

	public static void main(String[] args) {
		launch(args);
	}
}
