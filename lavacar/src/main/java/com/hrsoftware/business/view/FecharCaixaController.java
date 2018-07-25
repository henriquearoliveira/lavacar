package com.hrsoftware.business.view;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

import com.hrsoftware.GerenciadorStage;
import com.hrsoftware.business.Lancamento;
import com.hrsoftware.business.ServicoVeiculo;
import com.hrsoftware.business.TipoDeLancamento;
import com.hrsoftware.business.dao.LancamentoDAO;
import com.hrsoftware.business.dao.ServicoVeiculoDAO;
import com.hrsoftware.components.LoadingDialog;
import com.hrsoftware.components.TableFactory;
import com.hrsoftware.components.TipoController;
import com.hrsoftware.components.alerts.AlertDialog;
import com.hrsoftware.comum.AppManager;
import com.hrsoftware.dashboard.PrincipalController;
import com.hrsoftware.dashboard.ServicoVeiculoDTO;
import com.hrsoftware.utilitario.Ferramentas;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class FecharCaixaController implements Initializable {

	@FXML
	private Button btnGravar;

	@FXML
	private TabPane tabFechamento;

	@FXML
	private Tab tabPaneServico;

	@FXML
	private Tab tabPaneLancamento;

	@FXML
	private TableView<Lancamento> tabelaLancamentos;

	private TableFactory<Lancamento> factoryLancamento = new TableFactory<>();

	@FXML
	private TableView<ServicoVeiculoDTO> tabelaServicos;

	private TableFactory<ServicoVeiculoDTO> factoryServicoVeiculo = new TableFactory<>();

	@FXML
	private Label lblDate;

	@FXML
	private Label lblServicoVeiculo;

	@FXML
	private Label lblCreditos;

	@FXML
	private Label lblDebitos;

	@FXML
	private Label lblTotal;

	private LancamentoDAO lancamentoDAO = new LancamentoDAO();

	private ServicoVeiculoDAO servicoVeiculoDAO = new ServicoVeiculoDAO();

	private LoadingDialog loadingDialog = new LoadingDialog();

	private FluxoDeCaixaFacade facade = new FluxoDeCaixaFacade(this);

	private BigDecimal valorTotal = null;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		loadingDialog.start(() -> {

			preencheTabelas();

		});

	}

	/**
	 * CRIA E ALIMENTA AS TABELAS
	 */
	private void preencheTabelas() {

		List<Lancamento> lancamentos = lancamentoDAO.selecionaPelo(Ferramentas.getCaixaAberto());

		List<ServicoVeiculo> servicoVeiculos = servicoVeiculoDAO.selecionaFechados(Ferramentas.getCaixaAberto());

		List<ServicoVeiculoDTO> servicoVeiculoDTOs = new ArrayList<>();

		servicoVeiculos.forEach(sv -> {

			ServicoVeiculoDTO servicoVeiculoDTO = new ServicoVeiculoDTO(sv);
			servicoVeiculoDTOs.add(servicoVeiculoDTO);

		});

		alimentaGridFechamento(servicoVeiculoDTOs, lancamentos);

		Platform.runLater(() -> {
			factoryServicoVeiculo.createTable(servicoVeiculoDTOs, tabelaServicos, ServicoVeiculoDTO.class,
					Comparator.comparing(ServicoVeiculoDTO::getDataHoraCriacao));
			factoryLancamento.createTable(lancamentos, tabelaLancamentos, Lancamento.class,
					Comparator.comparing(Lancamento::getDataHoraCriacao));
		});

		Platform.runLater(() -> {
			configuraColunasTabela();
		});
	}

	private void alimentaGridFechamento(List<ServicoVeiculoDTO> servicoVeiculoDTOs, List<Lancamento> lancamentos) {

		BigDecimal valorTotalServicos = servicoVeiculoDTOs.stream().map(ServicoVeiculoDTO::getValor)
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		BigDecimal totalReceitas = lancamentos.stream()
				.filter(l -> l.getTipoDeLancamento().equals(TipoDeLancamento.RECEITA)).map(Lancamento::getValor)
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		BigDecimal totalDespesas = lancamentos.stream()
				.filter(l -> l.getTipoDeLancamento().equals(TipoDeLancamento.DESPESA)).map(Lancamento::getValor)
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		valorTotal = valorTotalServicos.add(totalReceitas).subtract(totalDespesas);

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
		LocalDateTime date = AppManager.getInstance().getDataHoraAtual();

		Platform.runLater(() -> {
			lblDate.setText("  " + date.format(formatter));
			lblServicoVeiculo.setText("  R$ " + valorTotalServicos.toString());
			lblCreditos.setText("  R$ " + totalReceitas.toString());
			lblDebitos.setText("  R$ " + totalDespesas.toString());
			lblTotal.setText("  R$ " + valorTotal.toString());
		});
	}

	/**
	 * CONFIGURAÇÃO DAS COLUNAS
	 */
	public void configuraColunasTabela() {

		tabelaServicos.getColumns().get(0).setPrefWidth(130);
		tabelaServicos.getColumns().get(0).setStyle("-fx-alignment: CENTER");
		tabelaServicos.getColumns().get(1).setPrefWidth(90);
		tabelaServicos.getColumns().get(1).setStyle("-fx-alignment: CENTER");
		tabelaServicos.getColumns().get(2).setPrefWidth(150);
		tabelaServicos.getColumns().get(3).setPrefWidth(112);
		tabelaServicos.getColumns().get(4).setPrefWidth(100);
		tabelaServicos.getColumns().get(4).setStyle("-fx-alignment: CENTER-RIGHT");

		tabelaLancamentos.getColumns().get(0).setPrefWidth(130);
		tabelaLancamentos.getColumns().get(0).setStyle("-fx-alignment: CENTER");
		tabelaLancamentos.getColumns().get(1).setPrefWidth(253);
		tabelaLancamentos.getColumns().get(2).setPrefWidth(100);
		tabelaLancamentos.getColumns().get(2).setStyle("-fx-alignment: CENTER-RIGHT");
		tabelaLancamentos.getColumns().get(3).setPrefWidth(100);
		tabelaLancamentos.getColumns().get(3).setStyle("-fx-alignment: CENTER");

	}

	@FXML
	public void onClose(ActionEvent event) {
		Platform.runLater(() -> {
			GerenciadorStage.getInstance().getStage().close();
		});
	}

	/*
	 * @FXML void acaoTabelaLancamento(MouseEvent event) {
	 * 
	 * }
	 * 
	 * @FXML void acaoTabelaServicoVeiculo(MouseEvent event) {
	 * 
	 * }
	 */

	@FXML
	public void onMinimize(ActionEvent event) {

		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.setIconified(true);
	}

	@FXML
	public void acaoBotaoGravar(ActionEvent event) {

		loadingDialog.start(() -> {
			fechaCaixa();
		});

	}

	/**
	 * GRAVA &/OR ATUALIZA
	 */
	private void fechaCaixa() {

		List<ServicoVeiculo> servicoVeiculosAbertos = servicoVeiculoDAO
				.selecionaAbertos(AppManager.getInstance().getUsuario().getBusinessClient());

		if (servicoVeiculosAbertos.size() > 0) {

			Platform.runLater(() -> {

				boolean resposta = AlertDialog.exibe(AlertType.CONFIRMATION, "Serviços em Aberto",
						"Existem serviços em aberto, será aberto outro caixa para os mesmos.");

				if (!resposta) {
					return;
				} else {

					loadingDialog.start(() -> {
						facade.gravaFechamento(valorTotal, Ferramentas.getCaixaAberto(), servicoVeiculosAbertos);
					});
				}

			});

		} else {

			facade.gravaFechamento(valorTotal, Ferramentas.getCaixaAberto(), null);

		}

	}

	public void configuraJanelaCaixaFechado(boolean deixaCaixaAberto) {

		fechaJanela();

		PrincipalController principalController = (PrincipalController) GerenciadorStage.getInstance()
				.getController(TipoController.PRINCIPAL_CONTROLLER);

		principalController.defineConfiguracaoCaixaFechado(deixaCaixaAberto);
	}

	private void fechaJanela() {
		onClose(null);
	}

}
