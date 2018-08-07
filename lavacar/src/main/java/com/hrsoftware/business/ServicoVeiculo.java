package com.hrsoftware.business;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.hrsoftware.comum.BeanIdentificavel;

@Entity
@Table(name = "servicoVeiculo", indexes = {
		@Index(name = "veiculo_servico", columnList = "veiculo_id, servico_id", unique = false) })
public class ServicoVeiculo extends BeanIdentificavel {
	
	@ManyToOne
	private FluxoDeCaixa fluxoDeCaixa;

	@ManyToOne
	private Veiculo veiculo;

	@ManyToOne
	private Servico servico;

	@Column(nullable = false)
    @Enumerated(EnumType.STRING)
	private StatusDoServico statusDoServico;
	
	public ServicoVeiculo() {
	}

	public ServicoVeiculo(Veiculo veiculo, Servico servico, StatusDoServico statusDoServico, FluxoDeCaixa fluxoDeCaixa) {
		this.veiculo = veiculo;
		this.servico = servico;
		this.statusDoServico = statusDoServico;
		this.fluxoDeCaixa = fluxoDeCaixa;
	}
	
	public FluxoDeCaixa getFluxoDeCaixa() {
		return fluxoDeCaixa;
	}
	
	public void setFluxoDeCaixa(FluxoDeCaixa fluxoDeCaixa) {
		this.fluxoDeCaixa = fluxoDeCaixa;
	}

	public Veiculo getVeiculo() {
		return veiculo;
	}

	public void setVeiculo(Veiculo veiculo) {
		this.veiculo = veiculo;
	}

	public Servico getServico() {
		return servico;
	}

	public void setServico(Servico servico) {
		this.servico = servico;
	}

	public StatusDoServico getStatusDoServiço() {
		return statusDoServico;
	}

	public void setStatusDoServico(StatusDoServico statusDoServico) {
		this.statusDoServico = statusDoServico;
	}
	
}
