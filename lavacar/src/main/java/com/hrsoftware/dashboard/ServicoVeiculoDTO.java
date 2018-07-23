package com.hrsoftware.dashboard;

import java.math.BigDecimal;

import com.hrsoftware.business.ServicoVeiculo;
import com.hrsoftware.components.MoedaFormatter;
import com.hrsoftware.components.Tabela;
import com.hrsoftware.comum.BeanIdentificavel;

public class ServicoVeiculoDTO extends BeanIdentificavel {

	@Tabela(name = "Placa")
	private String placa;

	@Tabela(name = "Cliente")
	private String cliente;

	@Tabela(name = "Tipo")
	private String tipoDeServico;

	@Tabela(name = "Valor")
	@MoedaFormatter()
	private BigDecimal valor;

	private Long idVeiculo;

	private Long idServico;

	public ServicoVeiculoDTO(ServicoVeiculo servicoVeiculo) {

		this.setId(servicoVeiculo.getId());
		this.setDataHoraCriacao(servicoVeiculo.getDataHoraCriacao());
		this.placa = servicoVeiculo.getVeiculo().getPlaca();
		this.cliente = servicoVeiculo.getVeiculo().getCliente().getNome();
		this.tipoDeServico = servicoVeiculo.getServico().getDescricao();
		this.valor = servicoVeiculo.getServico().getValor();
		this.idVeiculo = servicoVeiculo.getVeiculo().getId();
		this.idServico = servicoVeiculo.getServico().getId();

	}

	public String getCliente() {
		return cliente;
	}

	public void setCliente(String cliente) {
		this.cliente = cliente;
	}

	public String getPlaca() {
		return placa;
	}

	public void setPlaca(String placa) {
		this.placa = placa;
	}

	public String getTipoDeServico() {
		return tipoDeServico;
	}

	public void setTipoDeServico(String tipoDeServico) {
		this.tipoDeServico = tipoDeServico;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public Long getIdVeiculo() {
		return idVeiculo;
	}

	public void setIdVeiculo(Long idVeiculo) {
		this.idVeiculo = idVeiculo;
	}

	public Long getIdServico() {
		return idServico;
	}

	public void setIdServico(Long idServico) {
		this.idServico = idServico;
	}

}
