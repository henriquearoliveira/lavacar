package com.hrsoftware.business;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.hrsoftware.components.MoedaFormatter;
import com.hrsoftware.components.Tabela;
import com.hrsoftware.comum.BeanIdentificavel;

@Entity
@Table(name = "servico", indexes = { @Index(name = "descricao", columnList = "descricao", unique = false) })
public class Servico extends BeanIdentificavel {

	@ManyToOne
	private BusinessClient businessClient;

	@Tabela(name = "Descrição")
	@Column(nullable = false)
	private String descricao;

	@Tabela(name = "Valor")
	@MoedaFormatter()
	@Column(nullable = false)
	private BigDecimal valor;

	@Column(nullable = true)
	private String observação;

	@OneToMany(mappedBy = "servico", targetEntity = ServicoVeiculo.class, cascade = CascadeType.PERSIST)
	private List<ServicoVeiculo> servicoVeiculos;

	public Servico() {
	}

	public Servico(String descricao, BigDecimal valor, String observação) {
		this.descricao = descricao;
		this.valor = valor;
		this.observação = observação;
	}

	public BusinessClient getBusinessClient() {
		return businessClient;
	}

	public void setBusinessClient(BusinessClient businessClient) {
		this.businessClient = businessClient;
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

	public String getObservação() {
		return observação;
	}

	public void setObservação(String observação) {
		this.observação = observação;
	}

	@Override
	public String toString() {
		return descricao + " R$ " + valor;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((businessClient == null) ? 0 : businessClient.hashCode());
		result = prime * result + ((descricao == null) ? 0 : descricao.hashCode());
		result = prime * result + ((observação == null) ? 0 : observação.hashCode());
		result = prime * result + ((valor == null) ? 0 : valor.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Servico other = (Servico) obj;
		if (businessClient == null) {
			if (other.businessClient != null)
				return false;
		} else if (!businessClient.equals(other.businessClient))
			return false;
		if (descricao == null) {
			if (other.descricao != null)
				return false;
		} else if (!descricao.equals(other.descricao))
			return false;
		if (observação == null) {
			if (other.observação != null)
				return false;
		} else if (!observação.equals(other.observação))
			return false;
		if (valor == null) {
			if (other.valor != null)
				return false;
		} else if (!valor.equals(other.valor))
			return false;
		return true;
	}

}
