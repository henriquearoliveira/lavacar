package com.hrsoftware.components.alerts;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class AlertDialogConfirmacao extends AlertDialogAbstract {

	private String titulo;
	private String mensagem;

	public AlertDialogConfirmacao(String titulo, String mensagem) {
		super(Alert.AlertType.CONFIRMATION);
		this.titulo = titulo;
		this.mensagem = mensagem;

		configuraMensagens();
		
		showAndWait();
	}

	/**
	 * 
	 */
	private void configuraMensagens() {

		this.setTitle(titulo);
		this.setHeaderText(null);
		this.setContentText(mensagem);

	}

	/**
	 * @return
	 */
	public boolean mostraConfirmacao() {

		Optional<ButtonType> button = this.showAndWait();

		if (button.get() == ButtonType.OK) {
			return true;
		}

		return false;
	}

}
