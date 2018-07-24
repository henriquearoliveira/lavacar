package com.hrsoftware.services.http;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hrsoftware.components.LoadingDialog;
import com.hrsoftware.components.notifications.Notificacoes;
import com.hrsoftware.ftp.FTPAbstract;
import com.hrsoftware.relatorios.RelatorioBuilder;
import com.hrsoftware.services.GsonMapper;
import com.hrsoftware.services.MethodHTTP;
import com.hrsoftware.services.ServicesHTTP;
import com.hrsoftware.services.TypeResponse;
import com.hrsoftware.services.UrlConnect;

import javafx.application.Platform;

public class ServicosRest<T> {

	private UrlConnect urlConnect;
	private T body;
	private String[] params;

	public ServicosRest(UrlConnect urlConnect, T body, String[] params) {
		this.urlConnect = urlConnect;
		this.body = body;
		this.params = params;
	}

	public void doRequest(Consumer<ServicesHTTP<T>> consumer) {

		new LoadingDialog().start(() -> {
			doHttpRequest(consumer);
		});

	}

	private void doHttpRequest(Consumer<ServicesHTTP<T>> consumer) {
		
		limpaRelatoriosExistentes();

		URL url = null;
		ServicesHTTP<T> servicesHTTP = new ServicesHTTP<>();

		String urlWithParams = params == null ? urlConnect.getUrl() : trataParametros();

		try {
			url = new URL(urlWithParams);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		HttpURLConnection con = null;

		try {

			con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod(urlConnect.getMethodHTTP().getDescricao());
			con.setRequestProperty("Content-Type", "application/json");

			con.setConnectTimeout(5000);
			con.setReadTimeout(5000);

			if (urlConnect.getMethodHTTP().equals(MethodHTTP.POST)
					|| urlConnect.getMethodHTTP().equals(MethodHTTP.PUT)) {
				System.out.println("entrou");
				configuraPost(con);
			}

			servicesHTTP.setResponseCode(con.getResponseCode());
			servicesHTTP.setResponseMessage(con.getResponseMessage());

			System.out.println("WHAT");
			if (trataResponseCode(con.getResponseCode()))
				return;

		} catch (IOException e) {
			e.printStackTrace();
		}

		if (urlConnect.getTypeResponseGet().equals(TypeResponse.BYTE)) {

			configuraFile(consumer, servicesHTTP, con);
			consumer.accept(servicesHTTP);
			
			return;

		}

		configuraGet(servicesHTTP, con);

		consumer.accept(servicesHTTP);
	}

	private void configuraFile(Consumer<ServicesHTTP<T>> consumer, ServicesHTTP<T> servicesHTTP,
			HttpURLConnection con) {
		
		String contentDisposition = con.getHeaderField("Content-Disposition");
		String reportName = contentDisposition.substring(contentDisposition.indexOf("=") + 1,
				con.getHeaderField("Content-Disposition").length());

		try (InputStream inputStream = con.getInputStream()) {

			Files.copy(inputStream, Paths.get(FTPAbstract.getPathDownloads() + reportName),
					StandardCopyOption.REPLACE_EXISTING);
			
			servicesHTTP.setFileName(reportName);
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@SuppressWarnings("unchecked")
	private void configuraGet(ServicesHTTP<T> servicesHTTP, HttpURLConnection con) {

		Reader reader = null;

		try {
			reader = new InputStreamReader(con.getInputStream(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Gson gson = new GsonMapper().createGson();
		T objeto = null;
		List<T> list = null;

		if (urlConnect.getTypeResponseGet().equals(TypeResponse.OBJECT)) {
			System.out.println("oxi");
			objeto = gson.fromJson(reader, (Class<T>) urlConnect.getClasse());
			servicesHTTP.setObject(objeto);
		} else if (urlConnect.getTypeResponseGet().equals(TypeResponse.LIST)) {
			Type type = new TypeToken<ArrayList<T>>() {
			}.getType();
			list = gson.fromJson(reader, type);
			servicesHTTP.setList(list);
		}

	}

	private void configuraPost(HttpURLConnection con) {

		con.setDoOutput(true);
		Gson gson = new GsonMapper().createGson();

		String json = null;

		if (urlConnect.getTypeResponsePost().equals(TypeResponse.OBJECT)) {
			Type type = new TypeToken<T>() {
			}.getType();
			json = gson.toJson(body, type);
		} else if (urlConnect.getTypeResponsePost().equals(TypeResponse.LIST)) {
			Type type = new TypeToken<ArrayList<T>>() {
			}.getType();
			json = gson.toJson(body, type);
		}

		try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
			wr.write(json.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private boolean trataResponseCode(int responseCode) {

		if (responseCode == HttpURLConnection.HTTP_INTERNAL_ERROR) {
			Platform.runLater(() -> {
				Notificacoes.getInstance().mostraCurta(null, "Erro no servidor, por favor tente mais tarde.");
			});
			return true;
		}

		return false;

	}

	private String trataParametros() {

		String url = null;

		for (int i = 0; i < params.length; i++) {
			url = urlConnect.getUrl().replace("?" + i, params[i]);
		}

		return url;
	}
	
	private void limpaRelatoriosExistentes() {
		try {
			RelatorioBuilder.deletaRelatoriosTemp();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
