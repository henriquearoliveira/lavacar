package com.hrsoftware.business.view;

import java.util.Comparator;
import java.util.List;

import com.hrsoftware.business.dao.StatusException;
import com.hrsoftware.components.TableFactory;
import com.hrsoftware.components.notifications.Notificacoes;
import com.hrsoftware.comum.BeanIdentificavel;
import com.hrsoftware.dao.DaoAbstrato;

import javafx.application.Platform;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;

public abstract class ViewAbstract<T extends BeanIdentificavel> implements ConfiguracoesView<T> {

	private DaoAbstrato<T> dao;
	private TableView<T> tableCadastrados;
	private TableFactory<T> factory;

	private Class<T> classe;
	private Integer index;

	public void configuraViewAbstract(DaoAbstrato<T> dao, TableView<T> tableCadastrados, TableFactory<T> factory,
			Class<T> classe) {
		this.dao = dao;
		this.tableCadastrados = tableCadastrados;
		this.factory = factory;
		this.classe = classe;
	}

	public void configuraViewAbstract(DaoAbstrato<T> dao, Class<T> classe) {
		this.dao = dao;
		this.classe = classe;
	}

	public void preencheTabela(List<T> lista, Comparator<T> comparator) {

		Platform.runLater(() -> {
			factory.createTable(lista, tableCadastrados, classe, comparator);
			configuraColunasTabela();

			tableCadastrados.setOnKeyPressed(k -> {

				if (k.getCode().equals(KeyCode.UP)) {
					acaoTabelaCadastrados();
				} else if (k.getCode().equals(KeyCode.DOWN)) {
					acaoTabelaCadastrados();
				}

			});
		});

	}

	public abstract void configuraColunasTabela();

	public abstract void preencheCampos(T objeto);

	public void acaoTabelaCadastrados() {
		this.index = tableCadastrados.getSelectionModel().getSelectedIndex();

		if (tableCadastrados.getSelectionModel().getSelectedItem() == null)
			return;

		preencheCampos(tableCadastrados.getSelectionModel().getSelectedItem());

	}

	public boolean gravaOuAtualiza(T objeto) {

		if (objeto.getId() == null) {

			if (grava(objeto)) {
				return true;
			} else
				return false;

		} else {

			if (atualiza(objeto)) {
				return true;
			} else
				return false;
		}

	}

	private boolean grava(T objeto) {

		if (dao.insere(objeto) != null) {

			Notificacoes.getInstance().mostraCurta(null, "Gravado com sucesso.");

			if (factory != null) {
				Platform.runLater(() -> {
					factory.adicionaItem(objeto);
				});
			}
			return true;

		} else {
			Notificacoes.getInstance().mostraCurta(null, "Erro ao gravar, por favor tente mais tarde.");
			return false;
		}

	}

	private boolean atualiza(T objeto) {

		if (dao.atualizaObjeto(objeto) != null) {

			Notificacoes.getInstance().mostraCurta(null, "Atualizado com sucesso.");

			if (factory != null) {
				Platform.runLater(() -> {
					factory.atualizaItem(objeto, index);
				});
			}

			return true;
		} else {
			Notificacoes.getInstance().mostraCurta(null, "Erro ao atualizar, por favor tente mais tarde.");
			return false;
		}

	}

	public boolean exclui(T objeto) {

		if (dao.remove(objeto.getId())) {

			if (factory != null)
				factory.removeitem(index);

			// EXIBE NOTIFICAÇÃO
			Notificacoes.getInstance().mostraCurta(null, "Excluido com sucesso");

			return true;

		} else {

			// EXIBE NOTIFICAÇÃO
			Notificacoes.getInstance().mostraCurta(null,
					dao.getStatusException().equals(StatusException.DEPENDENCIA)
							? "Impossível excluir, outro registro depende dessa \n informação"
							: "Impossível excluir, por favor tente mais tarde");
			return false;
		}

	}

}
