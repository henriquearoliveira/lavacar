package com.hrsoftware.business;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.hrsoftware.components.MoedaFormatter;
import com.hrsoftware.components.Tabela;
import com.hrsoftware.comum.BeanIdentificavel;

@Entity
@Table(name = "fluxoDeCaixa"/*
							 * , indexes = { @Index(name = "fluxo", columnList = "businessClient_id", unique
							 * = false)}
							 */)
public class FluxoDeCaixa extends BeanIdentificavel {

	@ManyToOne
	private BusinessClient businessClient;

	@Column(nullable = true)
	@Tabela(name = "Total")
	@MoedaFormatter()
	private BigDecimal valorTotal;

	@Column(nullable = true)
	private boolean fechado;

	@OneToMany(mappedBy = "fluxoDeCaixa", targetEntity = Lancamento.class, cascade = CascadeType.PERSIST)
	private List<Lancamento> lancamentos;

	@OneToMany(mappedBy = "fluxoDeCaixa", targetEntity = ServicoVeiculo.class, cascade = CascadeType.PERSIST)
	private List<ServicoVeiculo> servicoVeiculos;

	public BusinessClient getBusinessClient() {
		return businessClient;
	}

	public void setBusinessClient(BusinessClient businessClient) {
		this.businessClient = businessClient;
	}

	public BigDecimal getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}

	public boolean isFechado() {
		return fechado;
	}

	public void setFechado(boolean fechado) {
		this.fechado = fechado;
	}

}
