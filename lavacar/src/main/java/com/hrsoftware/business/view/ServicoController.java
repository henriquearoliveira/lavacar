package com.hrsoftware.business.view;

import java.math.BigDecimal;
import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

import com.hrsoftware.business.Servico;
import com.hrsoftware.business.dao.ServicoDAO;
import com.hrsoftware.components.LoadingDialog;
import com.hrsoftware.components.TableFactory;
import com.hrsoftware.components.alerts.AlertDialog;
import com.hrsoftware.comum.AppManager;
import com.hrsoftware.utilitario.Ferramentas;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class ServicoController extends ViewAbstract<Servico> {

	@FXML
	private JFXTextField txtDescricao;

	@FXML
	private JFXTextArea txtObservacao;

	@FXML
	private JFXTextField txtValor;

	@FXML
	private TableView<Servico> tableCadastrados;

	@FXML
	private Button btnGravar;

	@FXML
	private Button btnNovo;

	@FXML
	private Button btnExcluir;

	private TableFactory<Servico> factoryServico = new TableFactory<>();

	private Servico servico = new Servico();

	private ServicoDAO servicoDAO = new ServicoDAO();

	private LoadingDialog loadingDialog = null;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		super.configuraViewAbstract(servicoDAO, tableCadastrados, factoryServico, Servico.class);

		loadingDialog = new LoadingDialog();
		loadingDialog.start(() -> {

			preencheTabela();

			configuraValor();

		});

	}

	private void configuraValor() {
		Ferramentas.apenasPontos(txtValor);
	}

	/**
	 * @param cliente
	 */
	@Override
	public void preencheCampos(Servico servico) {

		txtDescricao.setText(servico == null ? null : servico.getDescricao() == null ? null : servico.getDescricao());
		txtValor.setText(servico == null ? null
				: servico.getValor() == null ? null : servico.getValor().toString().replace(".", ","));
		txtObservacao
				.setText(servico == null ? null : servico.getObservação() == null ? null : servico.getObservação());

		btnExcluir.setDisable(servico == null ? true : servico.getDescricao() == null ? true : false);
		btnGravar.setText(servico == null ? "Gravar" : servico.getDescricao() == null ? "Gravar" : "Editar");
	}

	/**
	 * CRIA E ALIMENTA AS TABELAS
	 */
	private void preencheTabela() {

		List<Servico> servicos = servicoDAO.seleciona(AppManager.getInstance().getUsuario().getBusinessClient());

		preencheTabela(servicos, Comparator.comparing(Servico::getDataHoraCriacao).reversed());

	}

	/**
	 * CONFIGURAÇÃO DAS COLUNAS
	 */
	@Override
	public void configuraColunasTabela() {

		tableCadastrados.getColumns().get(0).setPrefWidth(130);
		tableCadastrados.getColumns().get(0).setStyle("-fx-alignment: CENTER");
		tableCadastrados.getColumns().get(1).setPrefWidth(350);
		tableCadastrados.getColumns().get(2).setPrefWidth(118);

	}

	@FXML
	void acaoTabelaCliente(MouseEvent event) {

		servico = tableCadastrados.getSelectionModel().getSelectedItem();

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
		String textoValor = txtValor.getText();
		String observacao = txtObservacao.getText();

		if (existeCamposNulos(descricao, textoValor, observacao))
			return;

		descricao = trocaParaMaiusculaPrimeiraLetraNome(descricao);

		String valorString = Ferramentas.trataValor(textoValor);

		BigDecimal valor = new BigDecimal(valorString);

		if (servico.getId() == null) {
			servico = new Servico(descricao, valor, observacao);
		} else {
			atualizaServico(descricao, valor, observacao);
		}

		servico.setBusinessClient(AppManager.getInstance().getUsuario().getBusinessClient());

		if (gravaOuAtualiza(servico)) {
			Platform.runLater(() -> {
				acaoBotaoNovo(null);
			});
		}

	}

	private void atualizaServico(String descricao, BigDecimal valor, String observacao) {

		servico.setDescricao(descricao);
		servico.setValor(valor);
		servico.setObservação(observacao);

	}

	private String trocaParaMaiusculaPrimeiraLetraNome(String nome) {

		String primeiraLetraNome = nome.substring(0, 1);
		return nome.replaceFirst(primeiraLetraNome, primeiraLetraNome.toUpperCase());

	}

	/**
	 *
	 * @param descricao
	 * @param valor
	 * @param observacao
	 * @return
	 */
	private boolean existeCamposNulos(String descricao, String valor, String observacao) {

		if (descricao == null || descricao.isEmpty() || valor == null || valor.isEmpty() || observacao == null
				|| observacao.isEmpty()) {

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

		servico = new Servico();

		factoryServico.clearTabela();
	}

	@FXML
	public void acaoBotaoExcluir(ActionEvent event) {

		if (servico == null)
			return;

		if (!AlertDialog.exibe(AlertType.CONFIRMATION, "Excluir", "Deseja realmente excluir o Servico selecionado ?"))
			return;

		loadingDialog.start(() -> {

			if (exclui(servico)) {
				Platform.runLater(() -> {
					acaoBotaoNovo(null);
				});
			}
		});

	}

}
