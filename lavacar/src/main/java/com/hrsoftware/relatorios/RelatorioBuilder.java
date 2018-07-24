package com.hrsoftware.relatorios;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.hrsoftware.ftp.FTPAbstract;
import com.hrsoftware.services.UrlConnect;
import com.hrsoftware.services.http.ServicosRest;

public class RelatorioBuilder {

	protected ServicosRest<Report> servicosRest = null;

	public RelatorioBuilder criaRelatorio(UrlConnect urlConnect, Report report) {
		servicosRest = new ServicosRest<>(UrlConnect.RELATORIO_FECHAMENTO_CAIXA, report, null);
		return this;

	}

	public Factory build() {
		return new Factory(this);
	}

	public ServicosRest<Report> getServicosRest() {
		return servicosRest;
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
	}

	@Override
	public void exportPDF() {

		builder.servicosRest.doRequest(shttp -> {
			abreRelatorio(shttp.getFileName());
		});

	}

	private void abreRelatorio(String fileName) {

		if (Desktop.isDesktopSupported()) {
			try {
				File myFile = new File(FTPAbstract.getPathDownloads() + fileName);
				Desktop.getDesktop().open(myFile);
			} catch (IOException ex) {
				System.err.println("DEU RUIM PRA ABRIR O RELATÓRIO EM PDF");
				ex.printStackTrace();
			}
		}
	}

}
