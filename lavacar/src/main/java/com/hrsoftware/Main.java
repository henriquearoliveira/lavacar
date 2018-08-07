package com.hrsoftware;

import java.io.IOException;

import com.hrsoftware.relatorios.RelatorioBuilder;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

	@Override
	public void start(Stage stageRoot) throws IOException {

		/*lançaJPA();*/

		RelatorioBuilder.deletaRelatoriosTemp();

		FXMLLoader loader = new FXMLLoader(getClass().getResource("login/view/Login.fxml"));

		Parent root = loader.load();

		GerenciadorStage.getInstance().setStage(stageRoot);

		root.setStyle("-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.4), 10, 0.5, 0.0, 0.0);"
				+ "-fx-background-color: #fafafa;");
		Scene scene = new Scene(root);

		stageRoot.setScene(scene);
		stageRoot.setResizable(false);
		stageRoot.setTitle("Login");
		stageRoot.initStyle(StageStyle.UNDECORATED);
		stageRoot.show();
		
		/*System.out.println("começou");
		
		CompletableFuture.runAsync(() -> {
			System.out.println("COMEÇO");
			travaThread();
		}).exceptionally(ex -> {
			System.out.println("erro");
			return null;
		}).thenAccept(sti -> System.out.println("final"));*/
		
		/*CompletableFuture<String> c1 = new CompletableFuture<>();
        new Thread(() -> {
        	try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        	
        	c1.complete("Ola Mundo");
        	System.out.println("aqui");
        	
        }).start();
        c1.thenAccept(str -> System.out.println(str));*/
		
		/*System.out.println("TErminou");*/

	}
	
	/*private void travaThread() {
		try {
			System.out.println("THREAD");
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}

	private void novaThreadComplete(String resultado) {
		
		Executor executor = Executors.newFixedThreadPool(10);
		CompletableFuture<String> teste = CompletableFuture.supplyAsync(() -> {
			
				System.out.println("teste1");
				System.out.println("teste2");
				System.out.println("teste3");
				System.out.println("teste4");
				System.out.println("teste5");
			
			System.out.println("DENTRO");
			
			return "Deu bom até";
		}, executor);
		

		try {
			resultado = teste.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}

	private void novaThreadFuture(String resultado) {
		ExecutorService executor = Executors.newFixedThreadPool(3);
		Future<String> future = executor.submit(() -> {
			
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			System.out.println("DENTRO");
			
			return "Deu bom";
		});
		
		try {
			resultado = future.get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		
		System.out.println(resultado);
	}*/

	/*private void lançaJPA() {
		TesteDBDAO dao = new TesteDBDAO();
		dao.seleciona();
	}*/

	public static void main(String[] args) {
		launch(args);
	}
}
