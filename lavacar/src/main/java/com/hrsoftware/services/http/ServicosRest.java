package com.hrsoftware.services.http;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hrsoftware.components.LoadingDialog;
import com.hrsoftware.services.GsonMapper;
import com.hrsoftware.services.MethodHTTP;
import com.hrsoftware.services.ServicesHTTP;
import com.hrsoftware.services.TypeResponse;
import com.hrsoftware.services.UrlConnect;

public class ServicosRest<T> {

	private UrlConnect urlConnect;
	private T body;
	private String[] params;

	public ServicosRest(UrlConnect urlConnect, T body, String... paramns) {
		this.urlConnect = urlConnect;
		this.body = body;
		this.params = paramns;
	}

	public void doRequest(Consumer<ServicesHTTP<T>> consumer) {

		new LoadingDialog().start(() -> {
			doHttpRequest(consumer);
		});

	}

	@SuppressWarnings("unchecked")
	private void doHttpRequest(Consumer<ServicesHTTP<T>> consumer) {

		URL url = null;
		ServicesHTTP<T> servicesHTTP = new ServicesHTTP<>();

		String urlWithParams = trataParametros();

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

			if (urlConnect.getMethodHTTP().equals(MethodHTTP.POST)
					|| urlConnect.getMethodHTTP().equals(MethodHTTP.PUT)) {
				configuraPost(con);
			}

			servicesHTTP.setResponseCode(con.getResponseCode());
			servicesHTTP.setResponseMessage(con.getResponseMessage());

			con.setConnectTimeout(5000);
			con.setReadTimeout(5000);

		} catch (IOException e) {
			e.printStackTrace();
		}

		Gson gson = new GsonMapper().createGson();

		T objeto = null;
		List<T> list = null;

		Reader reader = null;
		try {
			reader = new InputStreamReader(con.getInputStream(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (urlConnect.getTypeResponse().equals(TypeResponse.OBJECT)) {
			objeto = gson.fromJson(reader, (Class<T>) urlConnect.getClasse());
			servicesHTTP.setObject(objeto);
		} else {
			Type type = new TypeToken<ArrayList<T>>() {
			}.getType();
			list = gson.fromJson(reader, type);
			servicesHTTP.setList(list);
		}

		consumer.accept(servicesHTTP);
	}

	private String trataParametros() {

		String url = null;

		for (int i = 0; i < params.length; i++) {
			url = urlConnect.getUrl().replace("?" + i, params[i]);
		}

		return url;
	}

	private void configuraPost(HttpURLConnection con) {

		con.setDoOutput(true);
		Gson gson = new GsonMapper().createGson();
		String json = gson.toJson(body);

		try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
			wr.writeUTF(json);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
