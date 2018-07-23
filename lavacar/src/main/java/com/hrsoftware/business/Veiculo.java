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
@Table(name = "veiculo", indexes = { @Index(name = "nome_placa", columnList = "nome, placa", unique = false) })
public class Veiculo extends BeanIdentificavel {

	@ManyToOne
	@JoinColumn(name = "cliente_id", nullable = false)
	private Cliente cliente;

	@Tabela(name = "Nome")
	@Column(nullable = false)
	private String nome;

	@Tabela(name = "Placa")
	@Column(nullable = false)
	private String placa;

	@Column(nullable = true)
	private String cor;

	public Veiculo() {
	}

	public Veiculo(Cliente cliente, String nome, String placa, String cor) {

		this.cliente = cliente;
		this.nome = nome;
		this.placa = placa;
		this.cor = cor;

	}

	@OneToMany(mappedBy = "veiculo", targetEntity = ServicoVeiculo.class, fetch = FetchType.LAZY) //, cascade = CascadeType.ALL
	private List<ServicoVeiculo> servicoVeiculos;

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getPlaca() {
		return placa;
	}

	public void setPlaca(String placa) {
		this.placa = placa;
	}

	public String getCor() {
		return cor;
	}

	public void setCor(String cor) {
		this.cor = cor;
	}
	
	@Override
	public String toString() {
		return nome + " - " + placa;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cliente == null) ? 0 : cliente.hashCode());
		result = prime * result + ((cor == null) ? 0 : cor.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		result = prime * result + ((placa == null) ? 0 : placa.hashCode());
		result = prime * result + ((servicoVeiculos == null) ? 0 : servicoVeiculos.hashCode());
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
		Veiculo other = (Veiculo) obj;
		if (cliente == null) {
			if (other.cliente != null)
				return false;
		} else if (!cliente.equals(other.cliente))
			return false;
		if (cor == null) {
			if (other.cor != null)
				return false;
		} else if (!cor.equals(other.cor))
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		if (placa == null) {
			if (other.placa != null)
				return false;
		} else if (!placa.equals(other.placa))
			return false;
		if (servicoVeiculos == null) {
			if (other.servicoVeiculos != null)
				return false;
		} else if (!servicoVeiculos.equals(other.servicoVeiculos))
			return false;
		return true;
	}
	
}
