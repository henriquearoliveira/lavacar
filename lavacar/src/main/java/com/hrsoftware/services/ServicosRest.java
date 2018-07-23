package com.hrsoftware.services;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ServicosRest<T> {
	
	private String caminhoUrl;
	private Class<T> classe;

	public ServicosRest(String caminhoUrl, Class<T> classe) {
		this.caminhoUrl = caminhoUrl;
		this.classe = classe;
	}
	
	public T readValue() {
		
		URL url = null;
		
		try {
			url = new URL(caminhoUrl);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		HttpURLConnection connection = null;
		
		try {
			
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			
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
		
		return objeto;
		
	}

}
