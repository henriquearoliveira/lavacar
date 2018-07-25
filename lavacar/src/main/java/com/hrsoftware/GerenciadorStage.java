package com.hrsoftware;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.hrsoftware.components.TipoController;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.util.Duration;

public class GerenciadorStage<T> {

	private StackPane rootLayout;

	private Stage primaryStage;

	private Map<TipoController, T> controller = new HashMap<>();

	private Stage stage;

	private Parent node = null;

	private static GerenciadorStage<?> instance = null;

	/**
	 *
	 */
	private GerenciadorStage() {
	}

	public static GerenciadorStage<?> getInstance() {

		return instance == null ? instance = new GerenciadorStage<Object>() : instance;

	}

	public StackPane getRootLayout() {
		return rootLayout;
	}

	public void setRootLayout(StackPane rootLayout) {
		this.rootLayout = rootLayout;
	}

	public void setController(T controller, TipoController tipoController) {
		this.controller.put(tipoController, controller);
	}

	public T getController(TipoController tipoController) {
		return controller.get(tipoController);
	}

	public Stage getStage() {
		return stage;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public void showPopupUndecorated(String caminho, String titulo) {

		Node node = null;
		try {
			node = FXMLLoader.load(getClass().getResource(caminho));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Scene scene = new Scene((Parent) node);

		stage.setScene(scene);
		stage.setWidth(scene.getWidth());
		stage.setMaxWidth(scene.getWidth());
		stage.setMinWidth(scene.getWidth());
		
		stage.centerOnScreen();

		if (stage.isShowing()) {
			Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
			stage.setX((screenBounds.getWidth() - stage.getWidth()) / 2);
			stage.setY((screenBounds.getHeight() - stage.getHeight()) / 2);
		}

		if (titulo != null)
			stage.setTitle(titulo);

		if (stage.getStyle() != StageStyle.UNDECORATED)
			stage.initStyle(StageStyle.UNDECORATED);

		adminated(node);
		stage.show();

	}

	public T configureViewController(String caminho, Callback<Class<?>, Object> controllerFactory) {

		FXMLLoader loader = new FXMLLoader(getClass().getResource(caminho));

		loader.setControllerFactory(controllerFactory);

		node = null;
		try {
			node = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return loader.getController();

	}

	public void showPopupUndecoratedController(String titulo) {

		Scene scene = new Scene((Parent) node);

		stage.setScene(scene);

		if (titulo != null)
			stage.setTitle(titulo);

		if (stage.getStyle() != StageStyle.UNDECORATED)
			stage.initStyle(StageStyle.UNDECORATED);

		adminated(node);

		if (!stage.isShowing())
			stage.showAndWait();
		// stage.show();

	}

	private void adminated(Node vbox) {

		FadeTransition ft = new FadeTransition(Duration.seconds(3), vbox);
		ft.setFromValue(0.1);
		ft.setToValue(1.0);
		ft.setCycleCount(1);
		ft.setAutoReverse(false);
		ft.play();

	}

	public void showPopupUndecoratedNewStage(Node node, String caminho, String titulo) {

		loadJanelaEscuroParaClaro(node, e -> {
			carregaJanela(caminho, titulo);
		});

	}

	private void carregaJanela(String caminho, String titulo) {

		Node node = null;
		try {
			node = FXMLLoader.load(getClass().getResource(caminho));
		} catch (IOException e) {
			e.printStackTrace();
		}

		Scene scene = new Scene((Parent) node);

		Stage stageUnica = new Stage();

		stageUnica.setScene(scene);

		if (titulo != null)
			stageUnica.setTitle(titulo);

		if (stageUnica.getStyle() != StageStyle.UNDECORATED)
			stageUnica.initStyle(StageStyle.UNDECORATED);

		stageUnica.show();
	}

	private void loadJanelaEscuroParaClaro(Node node, EventHandler<ActionEvent> value) {

		FadeTransition ft = new FadeTransition(Duration.seconds(1), node);
		ft.setFromValue(1);
		ft.setToValue(0);
		ft.setOnFinished(value);
		ft.play();

	}

	public void showPrincipalFXML(String caminho) {

		Parent root = null;

		FXMLLoader loader = new FXMLLoader(getClass().getResource(caminho));

		try {
			root = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}

		this.setController(loader.getController(), TipoController.PRINCIPAL_CONTROLLER);

		Scene scene = new Scene(root);

		primaryStage = new Stage();
		primaryStage.setScene(scene);
		// stage.setMaximized(true);
		primaryStage.setTitle("Lava Rápido");
		primaryStage.setMinHeight(680);
		primaryStage.setMinWidth(1100);
		primaryStage.getIcons().add(new Image(getClass().getResource("login/images/lava-rapido.png").toString()));
		primaryStage.setOnCloseRequest(req -> {
			Platform.exit();
			System.exit(0);
		});
		primaryStage.show();

	}

}
