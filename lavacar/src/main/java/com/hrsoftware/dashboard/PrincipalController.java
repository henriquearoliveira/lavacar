package com.hrsoftware.dashboard;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.hrsoftware.GerenciadorStage;
import com.hrsoftware.business.view.FluxoDeCaixaFacade;
import com.hrsoftware.components.TipoController;
import com.hrsoftware.relatorios.RelatorioFechamentoDeCaixaPeriodo;
import com.hrsoftware.utilitario.Ferramentas;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class PrincipalController implements Initializable {

	@FXML
	private BorderPane bpPrincipal;

	@FXML
	private StackPane stkpPrincipal;

	@FXML
	private Label menuCaixaFinaliza;

	private Stage stage = null;

	private InicioController inicioController = null;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		stage = new Stage();
		GerenciadorStage.getInstance().setRootLayout(stkpPrincipal);
		GerenciadorStage.getInstance().setStage(stage);

		configuraAberturaCaixa();

		preencheInicio();

	}

	private void configuraAberturaCaixa() {

		if (Ferramentas.getCaixaAberto() != null) {
			configuraCaixaFechado(false);
		} else {
			configuraCaixaFechado(true);
		}

	}

	/**
	 * PREENCHE O INICIO NO CENTRO
	 */
	private void preencheInicio() {

		FXMLLoader fxmlLoader = new FXMLLoader();

		fxmlLoader.setLocation(getClass().getResource("view/Inicio.fxml"));

		AnchorPane anchorPane = null;
		try {
			anchorPane = fxmlLoader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// inicioController = fxmlLoader.getController();

		GerenciadorStage.getInstance().setController(fxmlLoader.getController(), TipoController.INICIO_CONTROLLER);
		inicioController = fxmlLoader.getController();

		anchorPane.setStyle("-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.4), 10, 0.5, 0.0, 0.0);"
				+ "-fx-background-color: #fafafa;");
		stkpPrincipal.getChildren().addAll(anchorPane);

	}

	@FXML
	public void acaoBotaoTeste(ActionEvent event) throws IOException {

		GerenciadorStage.getInstance().showPopupUndecorated("Cadastro.fxml", null);

	}

	@FXML
	public void acaoBotaoChamaIntegrada(ActionEvent event) throws IOException {

		FXMLLoader loader = new FXMLLoader();

		// loader.setLocation(IntegradoController.class.getResource("Integrado.fxml"));
		// TANTO FAZ
		loader.setLocation(getClass().getResource("Integrado.fxml"));

		AnchorPane anchorPane = loader.load();
		// IntegradoController controller = loader.getController(); // SE NECESSÁRIO

		stkpPrincipal.getChildren().setAll(anchorPane);

	}

	@FXML
	void acaoFechamentoCaixa(MouseEvent event) {

		if (Ferramentas.getCaixaAberto() != null) {
			new FluxoDeCaixaFacade(null).fechaCaixa();
		} else {
			new FluxoDeCaixaFacade(null).abreCaixa();
		}

	}

	public void configuraCaixaBotao(boolean isFechado) {
		Platform.runLater(() -> {
			configuraCaixaFechado(false);
			inicioController.btnNovo.setDisable(false);
		});
	}

	private void configuraCaixaFechado(boolean isFechado) {

		menuCaixaFinaliza.setText(isFechado ? "Abrir Caixa" : "Fechar Caixa");

	}

	public void defineConfiguracaoCaixaFechado(boolean isAberto) {

		InicioController controller = (InicioController) GerenciadorStage.getInstance()
				.getController(TipoController.INICIO_CONTROLLER);

		menuCaixaFinaliza.setText(isAberto ? "Fechar Caixa" : "Abrir Caixa");
		controller.btnNovo.setDisable(!isAberto);
		controller.limpaInicio(!isAberto);

	}

	@FXML
	void acaoCadastroCliente(MouseEvent event) {

		inicioController.limpaInicio(true);
		GerenciadorStage.getInstance().showPopupUndecorated("business/view/Cliente.fxml", "Clientes");

	}

	@FXML
	void acaoCadastroVeiculo(MouseEvent event) {

		inicioController.limpaInicio(true);
		GerenciadorStage.getInstance().showPopupUndecorated("business/view/Veiculo.fxml", "Veiculos");

	}

	@FXML
	void acaoCadastroServico(MouseEvent event) {

		inicioController.limpaInicio(true);
		GerenciadorStage.getInstance().showPopupUndecorated("business/view/Servico.fxml", "Serviços");
	}

	@FXML
	void acaoCadastroLancamento(MouseEvent event) {

		inicioController.limpaInicio(true);
		GerenciadorStage.getInstance().showPopupUndecorated("business/view/Lancamento.fxml", "Lançamentos");

	}

	@FXML
	void acaoRelatorioFluxoCaixa(MouseEvent event) {
		GerenciadorStage.getInstance().showPopupUndecorated("relatorios/FechamentoDeCaixa.fxml", "Fechamentos");
	}

	@FXML
	void acaoRelatorioFluxoCaixaPeriodo(MouseEvent event) {
		new RelatorioFechamentoDeCaixaPeriodo();
	}

}
