package com.hrsoftware.relatorios;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;

import com.hrsoftware.components.alerts.AlertDialog;
import com.hrsoftware.dao.JpaUtil;
import com.hrsoftware.ftp.FTPAbstract;
import com.hrsoftware.ftp.FTPRetrieveFile;
import com.hrsoftware.utilitario.Ferramentas;

import javafx.application.Platform;
import javafx.scene.control.Alert.AlertType;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

public class RelatorioBuilder {

	private JasperViewer viewer = null;

	private JasperPrint print = null;

	private Connection connection = null;

	public RelatorioBuilder criaRelatorio(Map<String, Object> params, RelatoriosCaminho relatorios) {

		List<String> listaRelatorios = Arrays.asList(relatorios.getDescricao());

		listaRelatorios.forEach(r -> {
			downloadRelatorioFTP(r);
		});

		Session session = JpaUtil.getInstance().getEntityManager().unwrap(Session.class);

		session.doWork(con -> {
			connection = con;
		});

		String caminhoLocal = Ferramentas.getCaminhoLocal(listaRelatorios.get(0));

		try {
			print = JasperFillManager.fillReport(FTPRetrieveFile.getPathDownloads() + caminhoLocal + ".jasper", params,
					connection);
		} catch (JRException e) {
			e.printStackTrace();
		}

		if (print == null) {
			Platform.runLater(() -> {
				AlertDialog.exibe(AlertType.ERROR, "Relatórios", "Caminho do relatório inválido.");
			});
			return this;
		}

		if (print.getPages().isEmpty()) {
			Platform.runLater(() -> {
				AlertDialog.exibe(AlertType.ERROR, "Relatórios", "Relatório não contém páginas.");
			});
			return this;
		}

		viewer = new JasperViewer(print, false);

		return this;

	}

	private void downloadRelatorioFTP(String relatorio) {

		FTPRetrieveFile ftp = new FTPRetrieveFile();

		ftp.download(relatorio + ".jasper");

	}

	public Factory build() {
		return new Factory(this);
	}

	public JasperViewer getViewer() {
		return viewer;
	}

	public JasperPrint getPrint() {
		return print;
	}

	public static void deletaRelatoriosTemp() throws IOException {

		String diretorio = FTPAbstract.getPathDownloads();

		if (!Files.exists(Paths.get(diretorio)))
			return;
		
		Files.walk(Paths.get(diretorio)).map(Path::toFile).forEach(File::delete);

	}

}

class Factory implements Relatorio {

	private RelatorioBuilder builder;

	public Factory(RelatorioBuilder builder) {

		this.builder = builder;
	}

	@Override
	public void exibeRelatorio() {

		if (builder.getViewer() == null) {
			return;
		}
		builder.getViewer().setVisible(true);
	}

	@Override
	public void exportPDF() {

		if (builder.getViewer() == null)
			return;

		if (builder.getPrint() == null)
			return;

		try {
			JasperExportManager.exportReportToPdfFile(builder.getPrint(),
					FTPAbstract.getPathDownloads() + "relatorio.pdf");

			abreRelatorio();
			
		} catch (JRException e) {
			e.printStackTrace();
			Platform.runLater(() -> {
				AlertDialog.exibe(AlertType.ERROR, "Relatórios", "Caminho do relatório inválido.");
			});
		}

	}

	private void abreRelatorio() {

		if (Desktop.isDesktopSupported()) {
			try {
				File myFile = new File(FTPAbstract.getPathDownloads() + "relatorio.pdf");
				Desktop.getDesktop().open(myFile);
			} catch (IOException ex) {
				System.err.println("DEU RUIM PRA ABRIR O RELATÓRIO EM PDF");
				ex.printStackTrace();
			}
		}
	}

}
