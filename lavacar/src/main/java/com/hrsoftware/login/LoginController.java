package com.hrsoftware.login;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.hrsoftware.GerenciadorStage;
import com.hrsoftware.business.Usuario;
import com.hrsoftware.business.dao.UsuarioDAO;
import com.hrsoftware.components.LoadingDialog;
import com.hrsoftware.components.alerts.AlertDialog;
import com.hrsoftware.comum.AppManager;
import com.hrsoftware.cryptography.Sha512Cryptography;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class LoginController implements Initializable {

	@FXML
	private JFXTextField username;

	@FXML
	private JFXPasswordField password;

	@FXML
	private Label idUsuarioSenhaIncorreto;

	@FXML
	private Button btnLogin;

	@FXML
	private AnchorPane apLogin;

	private UsuarioDAO usuarioDAO = new UsuarioDAO();

	private static double xOffset = 0;
	private static double yOffset = 0;

	public void initialize(URL location, ResourceBundle resources) {

		Node root = apLogin;
		setGlobalEventHandler(root);

		/*
		 * Platform.runLater(() -> { username.requestFocus(); });
		 */

		try {
			loginAction(null);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@FXML
	public void loginAction(ActionEvent event) throws IOException {

		LoadingDialog loadingDialog = new LoadingDialog();
		loadingDialog.start(() -> {
			logaUsuario();
		});

	}

	private void logaUsuario() {

		/*
		 * String nome = username.getText(); String senha = password.getText();
		 */

		String nome = "mailson";
		String senha = "dell";

		if (nome == null || nome.isEmpty() || senha == null || senha.isEmpty()) {
			Platform.runLater(() -> {
				AlertDialog.exibe(AlertType.ERROR, "Login", "Usuario ou senha vazios.");
			});
			return;
		}

		Usuario usuario = usuarioDAO.getPelo(nome, Sha512Cryptography.getSha512Cryptography(senha));

		if (usuario == null) {
			Platform.runLater(() -> {
				AlertDialog.exibe(AlertType.ERROR, "Login", "Usuario ou senha inexistentes.");
			});
			return;
		}

		AppManager.getInstance().setUsuario(usuario);

		loadSplashScreen();

	}

	@FXML
	public void mousePressed(MouseEvent event) {

		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

		xOffset = stage.getX() - event.getScreenX();
		yOffset = stage.getY() - event.getScreenY();

	}

	@FXML
	public void mouseDragged(MouseEvent event) {

		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

		stage.setX(event.getScreenX() + xOffset);
		stage.setY(event.getScreenY() + yOffset);

	}

	/*
	 * ENTER GERA AÇÃO NO BOTÃO ENTRAR
	 */
	private void setGlobalEventHandler(Node root) {
		root.addEventHandler(KeyEvent.KEY_PRESSED, ev -> {
			if (ev.getCode() == KeyCode.ENTER) {
				btnLogin.fire();
				ev.consume();
			}
		});
	}

	private void loadSplashScreen() {

		GerenciadorStage.getInstance().showPopupUndecoratedNewStage(apLogin, "login/view/SplashFXML.fxml",
				"Carregando");

	}

	@FXML
	public void onMinimize(ActionEvent event) {

		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

		stage.setIconified(true);
	}

	@FXML
	public void onClose(ActionEvent event) {

		Platform.exit();
		System.exit(0);

	}

}
