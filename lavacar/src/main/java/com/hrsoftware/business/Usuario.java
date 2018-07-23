package com.hrsoftware.business;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.hrsoftware.comum.BeanIdentificavel;

@Entity
@Table(name = "usuario", indexes = { @Index(name = "nome_email_senha", columnList = "nome, email, senha", unique = true) })
public class Usuario extends BeanIdentificavel {

	@ManyToOne
	@JoinColumn(name = "businessClient_id", nullable = false)
	private BusinessClient businessClient;

	@Column(nullable = false)
	private String nome;

	@Column(nullable = true)
	private String email;

	@Column(nullable = false)
	private String senha;

	public BusinessClient getBusinessClient() {
		return businessClient;
	}

	public void setBusinessClient(BusinessClient businessClient) {
		this.businessClient = businessClient;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

}
