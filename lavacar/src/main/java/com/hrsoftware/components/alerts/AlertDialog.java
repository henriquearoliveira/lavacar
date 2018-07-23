package com.hrsoftware.components.alerts;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;

public class AlertDialog {

	public static boolean exibe(AlertType alertType, String titulo, String mensagem) {

		Alert alert = new Alert(alertType);
		alert.setTitle(titulo);
		alert.setHeaderText(null);
		alert.setContentText(mensagem);
		
		if (alertType.equals(AlertType.CONFIRMATION)) {
			
			Optional<ButtonType> button = alert.showAndWait();

			if (button.get() == ButtonType.OK) {
				return true;
			}

		} else {
			alert.showAndWait();
		}

		return false;

	}

	public static String exibeInput(String titulo, String mensagem) {

		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle(titulo);
		dialog.setHeaderText(null);
		dialog.setContentText(mensagem);

		Optional<String> resultado = dialog.showAndWait();

		return resultado.get() != null ? resultado.get() : null;

	}

}
