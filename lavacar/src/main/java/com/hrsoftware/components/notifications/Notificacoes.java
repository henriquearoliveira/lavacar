package com.hrsoftware.components.notifications;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import com.hrsoftware.GerenciadorStage;

import javafx.application.Platform;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.stage.Screen;

public class Notificacoes {

	private static final Tooltip TOOLTIP_MENSAGEM = new Tooltip();

	private final List<Notificacao> lista = new ArrayList<>();

	private static final double SCREEN_WIDTH;
	private static final double SCREEN_HEIGHT;

	private static final Notificacoes instance = new Notificacoes();

	static {
		SCREEN_WIDTH = Screen.getPrimary().getBounds().getWidth();
		SCREEN_HEIGHT = Screen.getPrimary().getBounds().getHeight();

		TOOLTIP_MENSAGEM.setFont(Font.font("System", 13));
		TOOLTIP_MENSAGEM.setStyle("-fx-text-fill: white;");
		TOOLTIP_MENSAGEM.setWrapText(true);
	}

	public static Notificacoes getInstance() {
		return instance;
	}

	private Notificacoes() {
	}

	public void mostraCurta(URL urlIcone, String mensagem) {

		lista.add(new Notificacao(urlIcone, mensagem, 3000));

		if (!TOOLTIP_MENSAGEM.isShowing())
			Platform.runLater(() -> exibeMensagens());

	}

	public void mostraCurta(URL urlIcone, String mensagem, String param) {

		lista.add(new Notificacao(urlIcone, mensagem + param, 3000));

		if (!TOOLTIP_MENSAGEM.isShowing())
			Platform.runLater(() -> exibeMensagens());

	}

	public void mostraLonga(URL url, String mensagem) {

		lista.add(new Notificacao(url, mensagem, 5000));

		if (!TOOLTIP_MENSAGEM.isShowing())
			Platform.runLater(() -> exibeMensagens());

	}

	public void mostraLonga(URL url, String mensagem, String param) {

		lista.add(new Notificacao(url, mensagem + param, 5000));

		if (!TOOLTIP_MENSAGEM.isShowing())
			Platform.runLater(() -> exibeMensagens());

	}

	private void exibeMensagens() {

		if (lista.isEmpty()) {
			TOOLTIP_MENSAGEM.hide();
		} else {

			Notificacao notificacao = lista.get(0);

			URL url = notificacao.getIcone();
			String mensagem = notificacao.getMensagem();

			ImageView imageView = (url == null ? null : new ImageView(url.toExternalForm()));

			TOOLTIP_MENSAGEM.setGraphic(imageView);
			TOOLTIP_MENSAGEM.setText(mensagem);

			TOOLTIP_MENSAGEM.show(GerenciadorStage.getInstance().getStage());

			double largura = TOOLTIP_MENSAGEM.getWidth();
			TOOLTIP_MENSAGEM.setX(SCREEN_WIDTH / 2 - (largura / 2));
			TOOLTIP_MENSAGEM.setY(SCREEN_HEIGHT - 120);

			fadeIn();
		}
	}

	private void fadeIn() {

		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {

				Platform.runLater(() -> {

					double opacidade = TOOLTIP_MENSAGEM.getOpacity();
					opacidade = Double.valueOf(String.format(Locale.US, "%.1f", opacidade));

					if (opacidade >= 1.0) {
						timer.cancel();
						fadeOut();
					} else {
						opacidade = opacidade + 0.1;
						TOOLTIP_MENSAGEM.setOpacity(opacidade);
					}
				});

			}
		}, 0, 50);

	}

	private void fadeOut() {

		long duracao = (lista.isEmpty() ? 4000 : lista.get(0).getDuracao());

		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {

				Platform.runLater(() -> {

					double opacidade = TOOLTIP_MENSAGEM.getOpacity();
					opacidade = Double.valueOf(String.format(Locale.US, "%.1f", opacidade));

					if (opacidade < 0.1) {

						timer.cancel();

						if (!lista.isEmpty())
							lista.remove(0);

						exibeMensagens();

					} else {
						opacidade = opacidade - 0.1;
						TOOLTIP_MENSAGEM.setOpacity(opacidade);
					}

				});

			}
		}, duracao, 50);

	}

}
