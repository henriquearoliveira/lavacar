package com.hrsoftware.business.view;

import com.hrsoftware.comum.BeanIdentificavel;

public interface ConfiguracoesView<T extends BeanIdentificavel> {
	
	public void configuraColunasTabela();

	public abstract void preencheCampos(T objeto);

}
