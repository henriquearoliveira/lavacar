package com.hrsoftware.business.view;

import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

import com.hrsoftware.GerenciadorStage;
import com.hrsoftware.business.Cliente;
import com.hrsoftware.business.dao.ClienteDAO;
import com.hrsoftware.components.LoadingDialog;
import com.hrsoftware.components.TableFactory;
import com.hrsoftware.components.alerts.AlertDialog;
import com.hrsoftware.comum.AppManager;
import com.hrsoftware.services.UrlConnect;
import com.hrsoftware.services.http.ServicosRest;
import com.hrsoftware.utilitario.Ferramentas;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class ClienteController extends ViewAbstract<Cliente> implements Initializable {

	@FXML
	private JFXTextField txtNome;

	@FXML
	private JFXComboBox<String> cbxSexo;

	@FXML
	private JFXTextField txtTelefone;

	@FXML
	private JFXTextField txtCep;

	@FXML
	private JFXTextField txtEndereco;

	@FXML
	private JFXTextField txtNumero;

	@FXML
	private JFXTextField txtCidade;

	@FXML
	private JFXTextField txtEstado;

	@FXML
	private JFXTextField txtBairro;

	@FXML
	private TableView<Cliente> tableCadastrados;

	private TableFactory<Cliente> factoryCliente = new TableFactory<>();

	@FXML
	private Button btnExcluir;

	@FXML
	private Button btnGravar;

	@FXML
	private Button btnNovo;

	private Cliente cliente = new Cliente();

	private ClienteDAO clienteDAO = new ClienteDAO();

	private LoadingDialog loadingDialog = null;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		super.configuraViewAbstract(clienteDAO, tableCadastrados, factoryCliente, Cliente.class);

		loadingDialog = new LoadingDialog();
		loadingDialog.start(() -> {

			preencheTabela();

			alimentaCombos();

			limitaCaracters();

			cepRequest();

		});

	}

	/**
	 * 
	 */
	private void alimentaCombos() {

		cbxSexo.setItems(FXCollections.observableArrayList("M", "F"));

	}

	/**
	 * 
	 */
	private void cepRequest() {

		txtCep.focusedProperty().addListener((change, oldValue, newValue) -> {

			if (oldValue) {
				preecheEnderecos(txtCep.getText());
			}
		});

	}

	/**
	 * CEP
	 */
	private void preecheEnderecos(String cep) {

		if (cepIncorreto(cep)) {
			txtCep.setText(null);
			txtCep.requestFocus();
			return;
		}

		System.out.println("AQUI");
		ServicosRest<Cliente> services = new ServicosRest<Cliente>(UrlConnect.BAIRRO, null, cep);

		services.doRequest(shttp -> {
			preencheCamposCEP(shttp.getObject());
		});

	}

	/**
	 * @param cliente
	 */
	private void preencheCamposCEP(Cliente cliente) {

		txtNumero.setText(cliente == null ? null : cliente.getNumero() == null ? null : cliente.getNumero());
		txtEndereco.setText(cliente == null ? null : cliente.getLogradouro() == null ? null : cliente.getLogradouro());
		txtBairro.setText(cliente == null ? null : cliente.getBairro() == null ? null : cliente.getBairro());
		txtCidade.setText(cliente == null ? null : cliente.getCidade() == null ? null : cliente.getCidade());
		txtEstado.setText(cliente == null ? null : cliente.getEstado() == null ? null : cliente.getEstado());

	}

	private boolean cepIncorreto(String cep) {

		if (cep == null || cep.isEmpty()) {
			AlertDialog.exibe(AlertType.WARNING, "CEP", "CEP não pode ser vazio");
			return true;
		}

		if (Ferramentas.contemLetras(cep)) {
			AlertDialog.exibe(AlertType.WARNING, "CEP", "CEP não pode conter letras");
			return true;
		}

		if (cep.length() != 8) {
			AlertDialog.exibe(AlertType.WARNING, "CEP", "Preencha com um cep válido");
			return true;
		}

		return false;

	}

	/**
	 * @param cliente
	 */
	@Override
	public void preencheCampos(Cliente cliente) {

		txtNome.setText(cliente == null ? null : cliente.getNome() == null ? null : cliente.getNome());
		cbxSexo.setValue(cliente == null ? null : cliente.getSexo() == null ? null : cliente.getSexo());
		txtTelefone.setText(cliente == null ? null : cliente.getTelefone() == null ? null : cliente.getTelefone());
		txtCep.setText(cliente == null ? null : cliente.getCep() == null ? null : cliente.getCep());
		txtNumero.setText(cliente == null ? null : cliente.getNumero() == null ? null : cliente.getNumero());

		txtEndereco.setText(cliente == null ? null : cliente.getLogradouro() == null ? null : cliente.getLogradouro());
		txtBairro.setText(cliente == null ? null : cliente.getBairro() == null ? null : cliente.getBairro());
		txtCidade.setText(cliente == null ? null : cliente.getCidade() == null ? null : cliente.getCidade());
		txtEstado.setText(cliente == null ? null : cliente.getEstado() == null ? null : cliente.getEstado());

		btnExcluir.setDisable(cliente == null ? true : cliente.getNome() == null ? true : false);
		btnGravar.setText(cliente == null ? "Gravar" : cliente.getNome() == null ? "Gravar" : "Editar");
	}

	/**
	 * 
	 */
	private void limitaCaracters() {

		Ferramentas.limitaCaracteres(txtTelefone, 11);

		Ferramentas.limitaCaracteres(txtCep, 8);

		/*
		 * txtTelefone.textProperty().addListener((observable, oldValue, newValue) -> {
		 * if (newValue != null) { if (newValue.length() > 11) {
		 * txtTelefone.setText(oldValue); } } });
		 * 
		 * txtCep.textProperty().addListener((observable, oldValue, newValue) -> { if
		 * (newValue != null) { if (newValue.length() > 8) {
		 * txtTelefone.setText(oldValue); } } });
		 */
	}

	/**
	 * CRIA E ALIMENTA AS TABELAS
	 */
	private void preencheTabela() {

		List<Cliente> clientes = clienteDAO.seleciona(AppManager.getInstance().getUsuario().getBusinessClient());

		preencheTabela(clientes, Comparator.comparing(Cliente::getNome));

	}

	/**
	 * CONFIGURAÇÃO DAS COLUNAS
	 */
	@Override
	public void configuraColunasTabela() {

		tableCadastrados.getColumns().get(0).setPrefWidth(130);
		tableCadastrados.getColumns().get(0).setStyle("-fx-alignment: CENTER");
		tableCadastrados.getColumns().get(1).setPrefWidth(350);
		tableCadastrados.getColumns().get(2).setStyle("-fx-alignment: CENTER");
		tableCadastrados.getColumns().get(2).setPrefWidth(113);

	}

	@FXML
	public void onClose(ActionEvent event) {
		GerenciadorStage.getInstance().getStage().hide();
	}

	@FXML
	void acaoTabelaCliente(MouseEvent event) {
		cliente = tableCadastrados.getSelectionModel().getSelectedItem();
		acaoTabelaCadastrados();

	}

	@FXML
	public void onMinimize(ActionEvent event) {

		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.setIconified(true);
	}

	@FXML
	public void acaoBotaoGravar(ActionEvent event) {

		loadingDialog = new LoadingDialog();
		loadingDialog.start(() -> {
			gravaOuAtualiza();
		});

	}

	/**
	 * GRAVA &/OR ATUALIZA
	 */
	private void gravaOuAtualiza() {

		String nome = txtNome.getText();
		String sexo = cbxSexo.getSelectionModel().getSelectedItem(); // GET VALUE ACHO QUE PEGA QUALQUER COISA
		String telefone = txtTelefone.getText();
		String cep = txtCep.getText();
		String endereco = txtEndereco.getText();
		String numero = txtNumero.getText();
		String bairro = txtBairro.getText();
		String cidade = txtCidade.getText();
		String estado = txtEstado.getText();

		if (existeCamposNulos(nome, sexo, telefone, cep, endereco, bairro, cidade, estado))
			return;

		nome = trocaParaMaiusculaPrimeiraLetraNome(nome);

		if (cliente.getId() == null) {
			cliente = new Cliente(nome, sexo, telefone, cep, endereco, numero, bairro, cidade, estado);
		} else {
			atualizaCliente(nome, sexo, telefone, cep, endereco, numero, bairro, cidade, estado);
		}

		cliente.setBusinessClient(AppManager.getInstance().getUsuario().getBusinessClient());

		if (gravaOuAtualiza(cliente)) {
			Platform.runLater(() -> {
				acaoBotaoNovo(null);
			});
		}

	}

	private void atualizaCliente(String nome, String sexo, String telefone, String cep, String endereco, String numero,
			String bairro, String cidade, String estado) {

		cliente.setNome(nome);
		cliente.setSexo(sexo);
		cliente.setTelefone(telefone);
		cliente.setCep(cep);
		cliente.setLogradouro(endereco);
		cliente.setNumero(numero);
		cliente.setBairro(bairro);
		cliente.setCidade(cidade);
		cliente.setEstado(estado);

	}

	private String trocaParaMaiusculaPrimeiraLetraNome(String nome) {

		String primeiraLetraNome = nome.substring(0, 1);
		return nome.replaceFirst(primeiraLetraNome, primeiraLetraNome.toUpperCase());

	}

	/**
	 * @param nome
	 * @param sexo
	 * @param telefone
	 * @param cep
	 * @param endereco
	 * @param bairro
	 * @param cidade
	 * @param estado
	 * @return
	 */
	private boolean existeCamposNulos(String nome, String sexo, String telefone, String cep, String endereco,
			String bairro, String cidade, String estado) {

		if (nome == null || sexo == null || telefone == null || endereco == null || bairro == null || cidade == null
				|| estado == null) {

			Platform.runLater(() -> {
				AlertDialog.exibe(AlertType.ERROR, "Obrigatórios", "Campos obrigatórios não preenchidos");
			});

			return true;
		}

		return false;
	}

	@FXML
	public void acaoBotaoNovo(ActionEvent event) {

		preencheCampos(null);
		btnGravar.setText("Gravar");

		cliente = new Cliente();

		factoryCliente.clearTabela();

	}

	@FXML
	public void acaoBotaoExcluir(ActionEvent event) {

		if (cliente == null)
			return;

		if (!AlertDialog.exibe(AlertType.CONFIRMATION, "Excluir", "Deseja realmente excluir o Cliente selecionado ?"))
			return;

		if (exclui(cliente))
			acaoBotaoNovo(null);

	}

}
