package com.hrsoftware.dashboard;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.hrsoftware.GerenciadorStage;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;

public class IntegradoController implements Initializable {

	@FXML
	private AnchorPane ancpIntegrada;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}

	public void acaoBotaoVoltar(ActionEvent event) throws IOException {

		GerenciadorStage.getInstance().getRootLayout().getChildren().remove(0);

	}

}
