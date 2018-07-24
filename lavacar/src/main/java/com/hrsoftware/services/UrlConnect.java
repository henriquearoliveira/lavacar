package com.hrsoftware.services;

import com.hrsoftware.business.Cliente;

public enum UrlConnect {

	BAIRRO("http://api.postmon.com.br/v1/cep/?0", MethodHTTP.GET, TypeResponse.OBJECT, Cliente.class);

	private String url;
	private MethodHTTP methodHTTP;
	private TypeResponse typeResponse;
	private Class<?> classe;

	private <T> UrlConnect(String url, MethodHTTP methodHTTP, TypeResponse typeResponse, Class<?> classe) {
		this.url = url;
		this.methodHTTP = methodHTTP;
		this.typeResponse = typeResponse;
		this.classe = classe;
	}

	public String getUrl() {
		return url;
	}

	public MethodHTTP getMethodHTTP() {
		return methodHTTP;
	}

	public TypeResponse getTypeResponse() {
		return typeResponse;
	}
	
	public Class<?> getClasse() {
		return classe;
	}

}
