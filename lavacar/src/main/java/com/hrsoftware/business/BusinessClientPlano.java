package com.hrsoftware.business;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.hrsoftware.comum.BeanIdentificavel;

@Entity
@Table(name = "businessClientPlano", indexes = {
		@Index(name = "buness_cli_plano", columnList = "businessClient_id, plano_id", unique = false) })
public class BusinessClientPlano extends BeanIdentificavel {

	@ManyToOne
	private BusinessClient businessClient;

	@ManyToOne
	private Plano plano;

	@Column(nullable = false)
	private LocalDate dateTermino;

	@Column(nullable = true)
	private LocalDate datePagamento;

	public BusinessClient getBusinessClient() {
		return businessClient;
	}

	public void setBusinessClient(BusinessClient businessClient) {
		this.businessClient = businessClient;
	}

	public Plano getPlano() {
		return plano;
	}

	public void setPlano(Plano plano) {
		this.plano = plano;
	}

	public LocalDate getDateTermino() {
		return dateTermino;
	}

	public void setDateTermino(LocalDate dateTermino) {
		this.dateTermino = dateTermino;
	}

	public LocalDate getDatePagamento() {
		return datePagamento;
	}

	public void setDatePagamento(LocalDate datePagamento) {
		this.datePagamento = datePagamento;
	}

}
