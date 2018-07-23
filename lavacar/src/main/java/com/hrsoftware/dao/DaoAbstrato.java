package com.hrsoftware.dao;

import java.time.LocalDateTime;
import java.util.List;

import com.hrsoftware.comum.BeanIdentificavel;

public class DaoAbstrato<T extends BeanIdentificavel> extends DAO<T> implements IDAO<T> {
	
	// IMPLEMENTAR O BEAN IDENTIFICAVEL PRA CONSEGUIR NA ATUALIZACAO VERIFICAR QUE ELE EXISTE.

	public DaoAbstrato(Class<T> objectClass) {
		super(objectClass);
	}

	@Override
	public T atualizaObjeto(T objeto) {
		
		objeto.setDataHoraCriacao(objeto.getDataHoraCriacao());
		objeto.setDataHoraAtualizacao(LocalDateTime.now());
		
		return super.atualiza(objeto);
		
	}

	@Override
	public T getBy(Long id) {
		return getByID(id);
	}

	@Override
	public T insere(T objeto) {
		
		objeto.setDataHoraCriacao(LocalDateTime.now());
		
		return insereObjeto(objeto);
		
	}

	@Override
	public List<T> seleciona() {
		return selecionaCompleto();
	}

	@Override
	public List<T> seleciona(String descricao) {
		return selecionaPela(descricao);
	}
	
	@Override
	public boolean remove(Long id) {
		return removeObjeto(id);
	}

}
