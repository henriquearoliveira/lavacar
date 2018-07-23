package com.hrsoftware.services;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.function.Consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hrsoftware.components.LoadingDialog;
import com.hrsoftware.services.http.MethodHTTP;

public class ServicosRest<T> {

	private String caminhoUrl;
	private Class<T> classe;
	private MethodHTTP method;

	private LoadingDialog loading = new LoadingDialog();

	public ServicosRest(String caminhoUrl, Class<T> classe, MethodHTTP method) {
		this.caminhoUrl = caminhoUrl;
		this.classe = classe;
		this.method = method;
	}

	public void doRequest(Consumer<T> consumer) {

		loading.start(() -> {
			doHttpRequest(consumer);
		});

	}

	private void doHttpRequest(Consumer<T> consumer) {
		
		URL url = null;

		try {
			url = new URL(caminhoUrl);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		HttpURLConnection connection = null;

		try {

			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod(method.getDescricao());
			
			connection.setRequestProperty("Content-Type", "application/json");

		} catch (IOException e) {
			e.printStackTrace();
		}

		ObjectMapper objectMapper = new ObjectMapper();
		T objeto = null;

		try {
			objeto = objectMapper.readValue(connection.getInputStream(), classe);
		} catch (IOException e) {
			e.printStackTrace();
		}

		consumer.accept(objeto);
	}

}
