package com.hrsoftware.ftp;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import com.hrsoftware.utilitario.Ferramentas;

public abstract class FTPAbstract implements FTPConnection {

	public static final String host = "lavacardb-cloud.umbler.net";
	public static final int port = 21;
	public static final String user = "lavacardb-cloud";
	public static final String password = ">R9*)CavrYg_a$[";

	public static String getPathDownloads() {
		return System.getProperty("java.io.tmpdir") + "/relatorio/";
	}

	@SuppressWarnings("finally")
	protected boolean downloadArquivo(String arquivo) {

		FTPClient ftpClient = new FTPClient();

		boolean success = false;

		try {

			ftpClient.connect(host, port);
			ftpClient.login(user, password);
			ftpClient.enterLocalPassiveMode();
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

			String remoteFile = "/lavacar-relatorios/" + arquivo;

			String caminhoLocal = Ferramentas.getCaminhoLocal(arquivo);
			
			File diretorioLocal = new File(getPathDownloads());
			
			if (!diretorioLocal.exists())
				diretorioLocal.mkdirs();
			
			File downloadFile = new File(getPathDownloads() + caminhoLocal);

			OutputStream outputStream1 = new BufferedOutputStream(new FileOutputStream(downloadFile));
			success = ftpClient.retrieveFile(remoteFile, outputStream1);
			outputStream1.close();

			if (success) {
				downloadFile.deleteOnExit();
			}

		} catch (IOException ex) {
			ex.printStackTrace();
			return success;

		} finally {

			try {
				if (ftpClient.isConnected()) {
					ftpClient.logout();
					ftpClient.disconnect();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}

			return success;
		}

	}

}
