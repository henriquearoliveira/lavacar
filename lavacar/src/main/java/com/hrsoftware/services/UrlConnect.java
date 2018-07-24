package com.hrsoftware.services;

import com.hrsoftware.business.Cliente;
import com.hrsoftware.relatorios.Report;

public enum UrlConnect {

	BAIRRO("http://api.postmon.com.br/v1/cep/?0", MethodHTTP.GET, TypeResponse.OBJECT, Cliente.class, null),

	RELATORIO_FECHAMENTO_CAIXA("http://localhost:8080/report", MethodHTTP.POST, TypeResponse.BYTE, Report.class,
			TypeResponse.OBJECT);

	private String url;
	private MethodHTTP methodHTTP;
	private TypeResponse typeResponseGet;
	private Class<?> classe;
	private TypeResponse typeResponsePost;

	private <T> UrlConnect(String url, MethodHTTP methodHTTP, TypeResponse typeResponseGet, Class<?> classe,
			TypeResponse typeResponsePost) {
		this.url = url;
		this.methodHTTP = methodHTTP;
		this.typeResponseGet = typeResponseGet;
		this.classe = classe;
		this.typeResponsePost = typeResponsePost;
	}

	public String getUrl() {
		return url;
	}

	public MethodHTTP getMethodHTTP() {
		return methodHTTP;
	}

	public Class<?> getClasse() {
		return classe;
	}

	public TypeResponse getTypeResponsePost() {
		return typeResponsePost;
	}

	public TypeResponse getTypeResponseGet() {
		return typeResponseGet;
	}

}
