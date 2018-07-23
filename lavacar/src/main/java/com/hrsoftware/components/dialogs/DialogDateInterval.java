package com.hrsoftware.components.dialogs;

import java.time.LocalDate;
import java.util.Optional;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

public class DialogDateInterval {

	public static Optional<DateInterval> dateInterval = null;

	public static Optional<DateInterval> exibeDialogDateInterval(String title, String body) {

		dateInterval = Optional.empty();

		Dialog<Pair<LocalDate, LocalDate>> dialog = new Dialog<>();
		dialog.setTitle(title);
		dialog.setHeaderText(body);

		ButtonType loginButtonType = new ButtonType("Gerar", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 11, 10, 10));

		DatePicker inicio = new DatePicker();
		DatePicker termino = new DatePicker();
		termino.setPromptText("Término");

		grid.add(new Label("Inicio:"), 0, 0);
		grid.add(inicio, 1, 0);
		grid.add(new Label("Término:"), 0, 1);
		grid.add(termino, 1, 1);

		dialog.getDialogPane().setContent(grid);

		Platform.runLater(() -> inicio.requestFocus());

		dialog.setResultConverter(dialogButton -> {

			if (dialogButton == loginButtonType) {

				if (inicio.getValue() == null || termino.getValue() == null) {
					return null;
				}

				return new Pair<>(inicio.getValue(), termino.getValue());
			}

			return null;
		});

		Optional<Pair<LocalDate, LocalDate>> result = dialog.showAndWait();

		result.ifPresent(period -> {

			DateInterval interval = new DateInterval();
			interval.setInicio(period.getKey());
			interval.setTermino(period.getValue());

			dateInterval = Optional.ofNullable(interval);

		});

		return dateInterval;

	}
}
