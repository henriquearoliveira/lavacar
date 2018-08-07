package com.hrsoftware.business;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.hrsoftware.comum.BeanIdentificavel;

@Entity
@Table(name = "plano", indexes = { @Index(name = "plano", columnList = "nome", unique = false) })
public class Plano extends BeanIdentificavel {

	@Column(nullable = false)
	private String nome;

	@Column(nullable = false)
	private BigDecimal valor;

	@Column(nullable = false)
	private TipoDePlano tipoDePlano;

	@OneToMany(mappedBy = "plano", targetEntity = BusinessClientPlano.class, cascade = CascadeType.PERSIST)
	private List<BusinessClientPlano> BusinessClientPlanos;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public TipoDePlano getTipoDePlano() {
		return tipoDePlano;
	}

	public void setTipoDePlano(TipoDePlano tipoDePlano) {
		this.tipoDePlano = tipoDePlano;
	}

}
