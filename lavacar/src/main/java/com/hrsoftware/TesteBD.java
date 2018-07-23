package com.hrsoftware;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.hrsoftware.comum.BeanIdentificavel;

/*import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;*/

@Entity
@Table(name = "testedb")
public class TesteBD extends BeanIdentificavel{

	private String text;
	private int numero;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	@Override
	public String toString() {
		return "numero: " + numero + " texto: " + text;
	}

}
