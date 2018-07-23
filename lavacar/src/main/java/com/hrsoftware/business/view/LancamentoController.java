package com.hrsoftware.business.view;

import java.math.BigDecimal;
import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

import com.hrsoftware.GerenciadorStage;
import com.hrsoftware.business.FluxoDeCaixa;
import com.hrsoftware.business.Lancamento;
import com.hrsoftware.business.TipoDeLancamento;
import com.hrsoftware.business.dao.FluxoDeCaixaDAO;
import com.hrsoftware.business.dao.LancamentoDAO;
import com.hrsoftware.components.LoadingDialog;
import com.hrsoftware.components.TableFactory;
import com.hrsoftware.components.alerts.AlertDialog;
import com.hrsoftware.comum.AppManager;
import com.hrsoftware.utilitario.Ferramentas;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class LancamentoController extends ViewAbstract<Lancamento> implements Initializable {

	@FXML
	private JFXTextField txtDescricao;

	@FXML
	private JFXTextField txtValor;

	@FXML
	private JFXComboBox<String> cbxTipoDeLancamento;

	@FXML
	private TableView<Lancamento> tableCadastrados;

	@FXML
	private Button btnGravar;

	@FXML
	private Button btnNovo;

	@FXML
	private Button btnExcluir;

	private TableFactory<Lancamento> factoryLancamento = new TableFactory<>();

	private Lancamento lancamento = new Lancamento();

	private LancamentoDAO lancamentoDAO = new LancamentoDAO();

	private FluxoDeCaixaDAO fluxoDeCaixaDAO = new FluxoDeCaixaDAO();

	private LoadingDialog loadingDialog = null;

	private FluxoDeCaixa fluxoDeCaixa = null;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		super.configuraViewAbstract(lancamentoDAO, tableCadastrados, factoryLancamento, Lancamento.class);

		loadingDialog = new LoadingDialog();
		loadingDialog.start(() -> {

			caixaAberto();

		});

	}

	private void caixaAberto() {

		if (Ferramentas.getCaixaAberto() == null) {

			Platform.runLater(() -> {

				AlertDialog.exibe(AlertType.INFORMATION, "Caixa Fechado",
						"Nem um caixa aberto, por favor abra o caixa.");
				onClose(null);
			});

		} else {
			preencheTabela();

			carregaCombo();

			configuraValor();
		}

	}

	private void configuraValor() {
		Ferramentas.apenasPontos(txtValor);
	}

	/**
	 * 
	 */
	private void carregaCombo() {
		cbxTipoDeLancamento.setItems(FXCollections.observableArrayList(TipoDeLancamento.lista()));
	}

	/**
	 * @param cliente
	 */
	@Override
	public void preencheCampos(Lancamento lancamento) {

		if (lancamento != null) {
			cbxTipoDeLancamento.setValue(lancamento.getTipoDeLancamento().getDescricao());
			cbxTipoDeLancamento.getSelectionModel().select(0);
		} else {
			cbxTipoDeLancamento.getItems().clear();
			carregaCombo();
		}

		txtDescricao.setText(
				lancamento == null ? null : lancamento.getDescricao() == null ? null : lancamento.getDescricao());
		txtValor.setText(lancamento == null ? null
				: lancamento.getValor() == null ? null : lancamento.getValor().toString().replace(".", ","));

		btnExcluir.setDisable(lancamento == null ? true : lancamento.getDescricao() == null ? true : false);
		btnGravar.setText(lancamento == null ? "Gravar" : lancamento.getDescricao() == null ? "Gravar" : "Editar");

	}

	/**
	 * CRIA E ALIMENTA AS TABELAS
	 */
	private void preencheTabela() {

		fluxoDeCaixa = fluxoDeCaixaDAO.getCaixaAberto(AppManager.getInstance().getUsuario().getBusinessClient());

		if (fluxoDeCaixa == null)
			return;

		List<Lancamento> lancamentos = lancamentoDAO.selecionaPelo(fluxoDeCaixa);

		preencheTabela(lancamentos, Comparator.comparing(Lancamento::getDescricao));

	}

	/**
	 * CONFIGURAÇÃO DAS COLUNAS
	 */
	@Override
	public void configuraColunasTabela() {

		tableCadastrados.getColumns().get(0).setPrefWidth(130);
		tableCadastrados.getColumns().get(0).setStyle("-fx-alignment: CENTER");
		tableCadastrados.getColumns().get(1).setPrefWidth(200);
		tableCadastrados.getColumns().get(2).setPrefWidth(100);
		tableCadastrados.getColumns().get(2).setStyle("-fx-alignment: CENTER-RIGHT");
		tableCadastrados.getColumns().get(3).setPrefWidth(100);
		tableCadastrados.getColumns().get(3).setStyle("-fx-alignment: CENTER");

	}

	@FXML
	public void onClose(ActionEvent event) {
		GerenciadorStage.getInstance().getStage().hide();
	}

	@FXML
	void acaoTabelaCliente(MouseEvent event) {

		lancamento = tableCadastrados.getSelectionModel().getSelectedItem();

		acaoTabelaCadastrados();

	}

	@FXML
	public void onMinimize(ActionEvent event) {

		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.setIconified(true);
	}

	@FXML
	public void acaoBotaoGravar(ActionEvent event) {

		loadingDialog = new LoadingDialog();
		loadingDialog.start(() -> {
			gravaOuAtualiza();
		});

	}

	/**
	 * GRAVA &/OR ATUALIZA
	 */
	private void gravaOuAtualiza() {

		String descricao = txtDescricao.getText();
		String txtValorDecimal = txtValor.getText();
		String tipo = cbxTipoDeLancamento.getSelectionModel().getSelectedItem();

		TipoDeLancamento tipoDeLancamento = TipoDeLancamento.get(tipo);

		if (existeCamposNulos(descricao, txtValorDecimal, tipoDeLancamento))
			return;

		descricao = trocaParaMaiusculaPrimeiraLetraNome(descricao);

		BigDecimal valor = new BigDecimal(txtValorDecimal.replaceAll(",", "."));

		if (lancamento.getId() == null) {
			lancamento = new Lancamento(fluxoDeCaixa, descricao, valor, tipoDeLancamento);
		} else {
			atualizaLancamento(descricao, valor, tipoDeLancamento);
		}

		if (gravaOuAtualiza(lancamento)) {
			Platform.runLater(() -> {
				acaoBotaoNovo(null);
			});
		}

	}

	private void atualizaLancamento(String descricao, BigDecimal valor, TipoDeLancamento tipoDeLancamento) {

		lancamento.setDescricao(descricao);
		lancamento.setValor(valor);
		lancamento.setTipoDeLancamento(tipoDeLancamento);

	}

	private String trocaParaMaiusculaPrimeiraLetraNome(String nome) {

		String primeiraLetraNome = nome.substring(0, 1);
		return nome.replaceFirst(primeiraLetraNome, primeiraLetraNome.toUpperCase());

	}

	/**
	 * @param nome
	 * @param sexo
	 * @param telefone
	 * @param cep
	 * @param endereco
	 * @param bairro
	 * @param cidade
	 * @param estado
	 * @return
	 */
	private boolean existeCamposNulos(String descricao, String txtValorDecimal, TipoDeLancamento tipoDeLancamento) {

		if (descricao == null || descricao.isEmpty() || txtValorDecimal == null || txtValorDecimal.isEmpty()
				|| tipoDeLancamento == null) {

			Platform.runLater(() -> {
				AlertDialog.exibe(AlertType.ERROR, "Obrigatórios", "Campos obrigatórios não preenchidos");
			});

			return true;
		}

		return false;
	}

	@FXML
	public void acaoBotaoNovo(ActionEvent event) {

		preencheCampos(null);
		btnGravar.setText("Gravar");

		lancamento = new Lancamento();

		factoryLancamento.clearTabela();
	}

	@FXML
	public void acaoBotaoExcluir(ActionEvent event) {

		if (lancamento == null)
			return;

		if (!AlertDialog.exibe(AlertType.CONFIRMATION, "Excluir",
				"Deseja realmente excluir o Lancamento selecionado ?"))
			return;

		loadingDialog.start(() -> {

			if (exclui(lancamento)) {
				Platform.runLater(() -> {
					acaoBotaoNovo(null);
				});
			}
		});

	}

}
