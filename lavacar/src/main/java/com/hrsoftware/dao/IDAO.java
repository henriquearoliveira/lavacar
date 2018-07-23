package com.hrsoftware.dao;

import java.util.List;

public interface IDAO<T> {
	
	public T atualizaObjeto(T objeto);
	
	public T getBy(Long id);

	public T insere(T objeto);
	
	public List<T> seleciona();
	
	public List<T> seleciona(String descricao);
	
	public boolean remove(Long id);
	
}
