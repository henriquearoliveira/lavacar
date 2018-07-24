package com.hrsoftware.relatorios;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.hrsoftware.business.FluxoDeCaixa;
import com.hrsoftware.components.LoadingDialog;
import com.hrsoftware.components.alerts.AlertDialog;
import com.hrsoftware.components.dialogs.DateInterval;
import com.hrsoftware.components.dialogs.DialogDateInterval;
import com.hrsoftware.services.UrlConnect;
import com.hrsoftware.utilitario.Ferramentas;

import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;

public class RelatorioFechamentoDeCaixaPeriodo {

	@FXML
	private Button btnImprimir;

	@FXML
	private TableView<FluxoDeCaixa> tableCadastrados;

	// private FluxoDeCaixaDAO fluxoDeCaixaDAO = new FluxoDeCaixaDAO();

	// private LoadingDialog loadingDialog = new LoadingDialog();

	public RelatorioFechamentoDeCaixaPeriodo() {
		exibeEscolhaDatas();
	}

	private void exibeEscolhaDatas() {

		Optional<DateInterval> dateOptional = DialogDateInterval.exibeDialogDateInterval("Fluxo de Caixa",
				"Selecione o período:");

		if (!dateOptional.isPresent()) {
			AlertDialog.exibe(AlertType.ERROR, "Relatório", "Por favor preencha o período.");
			return;
		}

		new LoadingDialog().start(() -> {
			exibeRealatorio(dateOptional);
		});

	}

	private void exibeRealatorio(Optional<DateInterval> dateOptional) {

		DateInterval dateInterval = dateOptional.get();

		Map<String, Object> params = new HashMap<>();
		params.put("dataInicio", Ferramentas.localDateToDate(dateInterval.getInicio()));
		params.put("dataTermino", Ferramentas.localDateToDate(dateInterval.getTermino()));

		Database database = new Database();
		database.setPassword("");
		database.setUrlConnection("");
		database.setUsername("");

		FTP ftp = new FTP();
		ftp.setHostFTP("");
		ftp.setPassword("");
		ftp.setPathReports(PathReports.FECHAMENTO_CAIXA_PERIODO);
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

}
