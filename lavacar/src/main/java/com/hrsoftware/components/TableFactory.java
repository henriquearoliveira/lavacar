package com.hrsoftware.components;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.hrsoftware.GerenciadorStage;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;

public class TableFactory<S> {

	private ObjectProperty<TableRow<S>> lastSelectedRow = null;
	private TableView<S> tabela;
	private Comparator<S> comparator;

	private boolean existe;

	public TableFactory() {
		if (lastSelectedRow == null)
			lastSelectedRow = new SimpleObjectProperty<>();
	}

	/**
	 * @param values
	 * @param tabela
	 * @param classe
	 */
	public void createTable(List<S> values, TableView<S> tabela, Class<S> classe, Comparator<S> comparator) {
		this.tabela = tabela;
		this.comparator = comparator;

		configuraDescelecaoMesmaLinha(this.tabela);

		if (classe.getSuperclass() != null) {

			for (Field field : classe.getSuperclass().getDeclaredFields()) {

				configuraColunas(this.tabela, field);
			}

		}

		for (Field field : classe.getDeclaredFields()) {

			configuraColunas(this.tabela, field);

		}

		values.sort(comparator);

		ObservableList<S> list = FXCollections.observableArrayList(values);

		this.tabela.setItems(list);

	}

	public void adicionaItem(S item) {

		existe = false;

		saoIguais(item);

		if (existe)
			return;

		tabela.getItems().add(item);

		List<S> lista = new ArrayList<>(tabela.getItems());

		ordenaListaCadastrados(lista);

		clearTabela();

	}

	private void saoIguais(S item) {
		tabela.getItems().forEach(o -> {
			if (o.equals(item))
				existe = true;
		});
	}

	public void atualizaItem(S item, Integer index) {

		tabela.getItems().set(index, item);

		List<S> lista = new ArrayList<>(tabela.getItems());

		ordenaListaCadastrados(lista);

		clearTabela();

	}

	public void removeitem(int index) {

		tabela.getItems().remove(index);

		clearTabela();

	}

	public void clearTabela() {

		tabela.getSelectionModel().select(null);
		tabela.refresh();

	}

	private void ordenaListaCadastrados(List<S> lista) {

		lista.sort(comparator);

		ObservableList<S> ordering = FXCollections.observableArrayList(lista);

		tabela.setItems(ordering);

		tabela.refresh();

	}

	private void configuraDescelecaoMesmaLinha(TableView<S> tabela) {

		tabela.setRowFactory(new Callback<TableView<S>, TableRow<S>>() {
			@Override
			public TableRow<S> call(TableView<S> tableView2) {
				final TableRow<S> row = new TableRow<>();
				row.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						final int index = row.getIndex();
						if (index >= 0) {
							lastSelectedRow.set(row);
						}
						if (index >= 0 && index < tabela.getItems().size()
								&& tabela.getSelectionModel().isSelected(index)) {
							tabela.getSelectionModel().clearSelection();
							event.consume();
						}
					}
				});
				return row;
			}
		});

	}

	public void configuraDescelecaoClickFora(TableView<S> tabela) {

		// PEGO O STAGE PRINCIPAL
		Stage stage = (Stage) GerenciadorStage.getInstance().getRootLayout().getScene().getWindow();

		stage.getScene().addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {

				if (tabela.getSelectionModel().getSelectedItem() != null) {
					Bounds boundsOfSelectedRow = lastSelectedRow.get()
							.localToScene(lastSelectedRow.get().getLayoutBounds());
					if (boundsOfSelectedRow.contains(event.getSceneX(), event.getSceneY()) == false) {
						tabela.getSelectionModel().clearSelection();
					}
				}
			}
		});

	}

	/**
	 * @param <T>
	 * @param tabela
	 * @param field
	 */
	private static <S, T> void configuraColunas(TableView<S> tabela, Field field) {

		field.setAccessible(true);

		if (field.isAnnotationPresent(Tabela.class)) {

			Annotation annotation = field.getAnnotation(Tabela.class);

			Annotation annotationDate = field.getAnnotation(DateFormatter.class);

			Annotation annotationMoeda = field.getAnnotation(MoedaFormatter.class);

			Tabela annotationTabela = (Tabela) annotation;

			DateFormatter annotationDateFormatter = (DateFormatter) annotationDate;

			MoedaFormatter annotationMoedaFormatter = (MoedaFormatter) annotationMoeda;

			String name = annotationTabela.name();
			String formatoDate = null;
			String formatoMoeda = null;

			if (annotationDateFormatter != null)
				formatoDate = annotationDateFormatter.formatter();

			if (annotationMoedaFormatter != null)
				formatoMoeda = annotationMoedaFormatter.formatter().getDescricao();

			TableColumn<S, T> column = new TableColumn<>(name);

			column.setMinWidth(20);
			column.setCellValueFactory(new PropertyValueFactory<S, T>(field.getName()));
			column.setCellFactory(formatoDate == null ? getCustomCellFactoryMoeda(formatoMoeda)
					: getCustomCellFactoryDate(formatoDate));
			column.setResizable(false);

			tabela.getColumns().add(column);

		}
	}

	// FORMATA A DATA DO CAMPO
	private static <S, T> Callback<TableColumn<S, T>, TableCell<S, T>> getCustomCellFactoryDate(String formatter) {
		return new Callback<TableColumn<S, T>, TableCell<S, T>>() {

			@Override
			public TableCell<S, T> call(TableColumn<S, T> param) {
				TableCell<S, T> cell = new TableCell<S, T>() {

					@Override
					public void updateItem(final T item, boolean empty) {
						if (item != null) {

							if (formatter != null) {
								formataDate(item.toString());
							} else {
								setText(item.toString());
							}
						}
					}

					private void formataDate(String item) {

						DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

						LocalDateTime formatDateTime = LocalDateTime.parse(item);

						setText(formatDateTime.format(formatter));

					}
				};
				return cell;
			}
		};
	}

	// FORMATA A DATA DO CAMPO
	private static <S, T> Callback<TableColumn<S, T>, TableCell<S, T>> getCustomCellFactoryMoeda(String moeda) {
		return new Callback<TableColumn<S, T>, TableCell<S, T>>() {

			@Override
			public TableCell<S, T> call(TableColumn<S, T> param) {
				TableCell<S, T> cell = new TableCell<S, T>() {

					@Override
					public void updateItem(final T item, boolean empty) {
						if (item != null) {

							if (moeda != null) {
								setText(moeda + item.toString().replace(".", ","));
							} else {
								setText(item.toString());
							}
						}
					}
				};
				return cell;
			}
		};
	}

}
