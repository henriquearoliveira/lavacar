package com.hrsoftware.business.dao;

import java.util.LinkedHashMap;
import java.util.Map;

import com.hrsoftware.business.Usuario;
import com.hrsoftware.dao.DaoAbstrato;

public class UsuarioDAO extends DaoAbstrato<Usuario> {

	private final String GET_BY_USUARIO_SENHA = "SELECT u FROM Usuario u WHERE u.nome = :nome AND u.senha = :senha";

	// "SELECT s FROM Servico s WHERE s.businessClient.id = :idBusinessClient ORDER
	// BY s.descricao ASC";

	public UsuarioDAO() {
		super(Usuario.class);
	}

	public Usuario getPelo(String nome, String senha) {

		Map<String, Object> params = new LinkedHashMap<>();
		params.put("nome", nome);
		params.put("senha", senha);

		return super.getBy(GET_BY_USUARIO_SENHA, params);
	}

}
