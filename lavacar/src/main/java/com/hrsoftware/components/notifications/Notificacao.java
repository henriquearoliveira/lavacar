package com.hrsoftware.components.notifications;

import java.net.URL;

public class Notificacao {

	private final URL icone;
	private final String mensagem;
	private final long duracao;

	public Notificacao(URL icone, String mensagem, long duracao) {
		this.icone = icone;
		this.mensagem = mensagem;
		this.duracao = duracao;
	}

	public URL getIcone() {
		return icone;
	}

	public String getMensagem() {
		return mensagem;
	}

	public long getDuracao() {
		return duracao;
	}

}
