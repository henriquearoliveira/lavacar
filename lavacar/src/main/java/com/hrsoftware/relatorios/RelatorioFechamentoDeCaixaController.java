package com.hrsoftware.relatorios;

import java.net.URL;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import com.hrsoftware.business.FluxoDeCaixa;
import com.hrsoftware.business.dao.FluxoDeCaixaDAO;
import com.hrsoftware.business.view.ViewAbstract;
import com.hrsoftware.components.LoadingDialog;
import com.hrsoftware.components.TableFactory;
import com.hrsoftware.comum.AppManager;
import com.hrsoftware.services.UrlConnect;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class RelatorioFechamentoDeCaixaController extends ViewAbstract<FluxoDeCaixa> {

	@FXML
	private Button btnImprimir;

	@FXML
	private TableView<FluxoDeCaixa> tableCadastrados;

	private FluxoDeCaixaDAO fluxoDeCaixaDAO = new FluxoDeCaixaDAO();

	private TableFactory<FluxoDeCaixa> factoryFluxoDeCaixa = new TableFactory<>();

	private LoadingDialog loadingDialog = new LoadingDialog();

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		super.configuraViewAbstract(fluxoDeCaixaDAO, tableCadastrados, factoryFluxoDeCaixa, FluxoDeCaixa.class);

		loadingDialog = new LoadingDialog();
		loadingDialog.start(() -> {

			preencheTabela();
			btnImprimir.setDisable(true);

		});

	}

	/**
	 *
	 */
	private void preencheTabela() {

		List<FluxoDeCaixa> fluxoDeCaixas = fluxoDeCaixaDAO
				.seleciona(AppManager.getInstance().getUsuario().getBusinessClient());

		preencheTabela(fluxoDeCaixas, Comparator.comparing(FluxoDeCaixa::getDataHoraCriacao).reversed());

	}

	@Override
	public void configuraColunasTabela() {

		tableCadastrados.getColumns().get(0).setPrefWidth(170);
		tableCadastrados.getColumns().get(0).setStyle("-fx-alignment: CENTER");
		tableCadastrados.getColumns().get(1).setPrefWidth(146);
		tableCadastrados.getColumns().get(1).setStyle("-fx-alignment: CENTER-RIGHT");

	}

	@Override
	@Deprecated
	public void preencheCampos(FluxoDeCaixa objeto) {

	}

	@FXML
	void acaoBotaoImprimir(ActionEvent event) {
		exibeRealatorio();
	}

	private void exibeRealatorio() {

		FluxoDeCaixa fluxoDeCaixa = tableCadastrados.getSelectionModel().getSelectedItem();

		Map<String, Object> params = new HashMap<>();
		params.put("idFluxoDeCaixa", fluxoDeCaixa.getId());

		Database database = new Database();
		database.setPassword("");
		database.setUrlConnection("");
		database.setUsername("");

		FTP ftp = new FTP();
		ftp.setHostFTP("");
		ftp.setPassword("");
		ftp.setPathReports(PathReports.FECHAMENTO_CAIXA);
		ftp.setPort(21);
		ftp.setUsername("");

		Report report = new Report();
		report.setDatabase(database);
		report.setFtp(ftp);
		report.setParams(params);
		report.setTypeFormat(TypeFormat.PDF);

		Relatorio relatorio = new RelatorioBuilder().criaRelatorio(UrlConnect.RELATORIO_FECHAMENTO_CAIXA, report)
				.build();
		relatorio.exportPDF();

	}

	@FXML
	void acaoTabelaCaixasFinalizados(MouseEvent event) {

		if (tableCadastrados.getSelectionModel().getSelectedIndex() >= 0) {
			btnImprimir.setDisable(false);
		} else {
			btnImprimir.setDisable(true);
		}

	}

	@FXML
	void onMinimize(ActionEvent event) {
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.setIconified(true);
	}

}
