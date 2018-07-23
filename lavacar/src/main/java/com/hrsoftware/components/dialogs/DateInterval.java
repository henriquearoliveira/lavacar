package com.hrsoftware.components.dialogs;

import java.time.LocalDate;

public class DateInterval {

	private LocalDate inicio, termino;

	public LocalDate getInicio() {
		return inicio;
	}

	public void setInicio(LocalDate inicio) {
		this.inicio = inicio;
	}

	public LocalDate getTermino() {
		return termino;
	}

	public void setTermino(LocalDate termino) {
		this.termino = termino;
	}

}
