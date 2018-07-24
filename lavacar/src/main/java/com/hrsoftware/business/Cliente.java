package com.hrsoftware.business;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.hrsoftware.components.Tabela;
import com.hrsoftware.comum.BeanIdentificavel;

@Entity
@Table(name = "cliente", indexes = { @Index(name = "nome", columnList = "nome", unique = false) })
//@JsonIgnoreProperties(ignoreUnknown = true) // ANOTAÇÃO DO JACKSON PRA IGNORAR OS CAMPOS QUE NÃO TENHO
public class Cliente extends BeanIdentificavel {

	@ManyToOne
	@JoinColumn(name = "businessClient_id", nullable = false)
	private BusinessClient businessClient;

	@Tabela(name = "Nome")
	@Column(nullable = false)
	private String nome;

	@Tabela(name = "Telefone")
	@Column(nullable = true)
	private String telefone;
	
	@Column(nullable = false)
	private String estado;

	@Column(nullable = false)
	private String cidade;

	@Column(nullable = false)
	private String logradouro;

	@Column(nullable = true)
	private String numero;
	
	@Column(nullable = true)
	private String bairro;
	
	@Column(nullable = true)
	private String sexo;
	
	@Column(nullable = false)
	private String cep;

	@OneToMany(mappedBy = "cliente", targetEntity = Veiculo.class, fetch = FetchType.LAZY) //, cascade = CascadeType.ALL
	private List<Veiculo> vaiculos;
	
	/**
	 * PARA O JPA/HIBERNATE
	 */
	public Cliente() {
	}

	public Cliente(String nome, String sexo, String telefone, String cep, String endereco, String numero,
			String bairro, String cidade, String estado) {
		this.nome = nome;
		this.sexo = sexo;
		this.telefone = telefone;
		this.cep = cep;
		this.logradouro = endereco;
		this.numero = numero;
		this.bairro = bairro;
		this.cidade = cidade;
		this.estado = estado;
	}

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

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}
	
	public String getEstado() {
		return estado;
	}
	
	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getLogradouro() {
		return logradouro;
	}
	
	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}
	
	public String getBairro() {
		return bairro;
	}
	
	public void setBairro(String bairro) {
		this.bairro = bairro;
	}
	
	public String getSexo() {
		return sexo;
	}
	
	public void setSexo(String sexo) {
		this.sexo = sexo;
	}
	
	public String getCep() {
		return cep;
	}
	
	public void setCep(String cep) {
		this.cep = cep;
	}
	
	@Override
	public String toString() {
		return this.getNome();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bairro == null) ? 0 : bairro.hashCode());
		result = prime * result + ((businessClient == null) ? 0 : businessClient.hashCode());
		result = prime * result + ((cep == null) ? 0 : cep.hashCode());
		result = prime * result + ((cidade == null) ? 0 : cidade.hashCode());
		result = prime * result + ((estado == null) ? 0 : estado.hashCode());
		result = prime * result + ((logradouro == null) ? 0 : logradouro.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		result = prime * result + ((numero == null) ? 0 : numero.hashCode());
		result = prime * result + ((sexo == null) ? 0 : sexo.hashCode());
		result = prime * result + ((telefone == null) ? 0 : telefone.hashCode());
		result = prime * result + ((vaiculos == null) ? 0 : vaiculos.hashCode());
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
		Cliente other = (Cliente) obj;
		if (bairro == null) {
			if (other.bairro != null)
				return false;
		} else if (!bairro.equals(other.bairro))
			return false;
		if (businessClient == null) {
			if (other.businessClient != null)
				return false;
		} else if (!businessClient.equals(other.businessClient))
			return false;
		if (cep == null) {
			if (other.cep != null)
				return false;
		} else if (!cep.equals(other.cep))
			return false;
		if (cidade == null) {
			if (other.cidade != null)
				return false;
		} else if (!cidade.equals(other.cidade))
			return false;
		if (estado == null) {
			if (other.estado != null)
				return false;
		} else if (!estado.equals(other.estado))
			return false;
		if (logradouro == null) {
			if (other.logradouro != null)
				return false;
		} else if (!logradouro.equals(other.logradouro))
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		if (numero == null) {
			if (other.numero != null)
				return false;
		} else if (!numero.equals(other.numero))
			return false;
		if (sexo == null) {
			if (other.sexo != null)
				return false;
		} else if (!sexo.equals(other.sexo))
			return false;
		if (telefone == null) {
			if (other.telefone != null)
				return false;
		} else if (!telefone.equals(other.telefone))
			return false;
		if (vaiculos == null) {
			if (other.vaiculos != null)
				return false;
		} else if (!vaiculos.equals(other.vaiculos))
			return false;
		return true;
	}
	
	

}
