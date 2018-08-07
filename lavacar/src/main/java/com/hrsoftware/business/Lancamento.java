package com.hrsoftware.business;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.hrsoftware.components.Tabela;
import com.hrsoftware.comum.BeanIdentificavel;

@Entity
@Table(name = "lancamento", indexes = { @Index(name = "lancamento", columnList = "fluxoDeCaixa_id", unique = false) })
public class Lancamento extends BeanIdentificavel {

	@ManyToOne
	private FluxoDeCaixa fluxoDeCaixa;

	@Tabela(name = "Descrição")
	@Column(nullable = false)
	private String descricao;

	@Tabela(name = "Valor")
	@Column(nullable = false)
	private BigDecimal valor;

	@Tabela(name = "Tipo")
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private TipoDeLancamento tipoDeLancamento;

	public Lancamento() {

	}

	public Lancamento(FluxoDeCaixa fluxoDeCaixa, String descricao, BigDecimal valor,
			TipoDeLancamento tipoDeLancamento) {

		this.fluxoDeCaixa = fluxoDeCaixa;
		this.descricao = descricao;
		this.valor = valor;
		this.tipoDeLancamento = tipoDeLancamento;

	}

	public FluxoDeCaixa getFluxoDeCaixa() {
		return fluxoDeCaixa;
	}

	public void setFluxoDeCaixa(FluxoDeCaixa fluxoDeCaixa) {
		this.fluxoDeCaixa = fluxoDeCaixa;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public TipoDeLancamento getTipoDeLancamento() {
		return tipoDeLancamento;
	}

	public void setTipoDeLancamento(TipoDeLancamento tipoDeLancamento) {
		this.tipoDeLancamento = tipoDeLancamento;
	}

}
