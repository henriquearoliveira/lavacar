package com.hrsoftware.dashboard;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

public class CadastroController implements Initializable {
	
	@FXML
	private Button btnCancelar;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
	}
	
	@FXML
	public void acaoCancelar(ActionEvent event) {
		btnCancelar.getScene().getWindow().hide();
	}

}
