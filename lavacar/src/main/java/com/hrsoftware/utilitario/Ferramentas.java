package com.hrsoftware.utilitario;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import com.hrsoftware.business.FluxoDeCaixa;
import com.hrsoftware.business.dao.FluxoDeCaixaDAO;
import com.hrsoftware.comum.AppManager;
import com.jfoenix.controls.JFXTextField;

public class Ferramentas {

	public static void limitaCaracteres(JFXTextField campoTexto, int quantidadeCaracteres) {

		campoTexto.textProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null) {
				if (newValue.length() > quantidadeCaracteres) {
					campoTexto.setText(oldValue);
				}
			}
		});

	}

	public static void apenasPontos(JFXTextField campoTexto) {

		campoTexto.textProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null) {

				if (!newValue.matches(("^[0-9,;]+$"))) { // "^[0-9,;]+$" \\d{0-9}([\\.])
					campoTexto.setText(oldValue);
				}

			}
		});

	}

	public static boolean contemLetras(String cep) {

		try {
			Integer.parseInt(cep);
		} catch (Exception e) {
			return true;
		}

		return false;
	}

	public static String trataValor(String textoValor) {
		return textoValor.replace(",", ".");
	}

	public static FluxoDeCaixa getCaixaAberto() {

		FluxoDeCaixa fluxoDeCaixa = new FluxoDeCaixaDAO()
				.getCaixaAberto(AppManager.getInstance().getUsuario().getBusinessClient());

		if (fluxoDeCaixa == null) {
			return null;
		}

		return fluxoDeCaixa;

	}

	public static String getCaminhoLocal(String caminhoCompleto) {
		String caminho = caminhoCompleto.substring(caminhoCompleto.lastIndexOf("/") + 1, caminhoCompleto.length());
		return caminho;
	}

	public LocalDateTime dateToLocalDateTime(Date date) {
		return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
	}

	public static Date localDateToDate(LocalDate localDate) {
		return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
	}

}
