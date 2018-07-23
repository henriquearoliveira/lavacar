package com.hrsoftware.comum;

import java.time.LocalDateTime;

import com.hrsoftware.business.Usuario;
import com.hrsoftware.business.dao.UsuarioDAO;

public class AppManager {

	private Usuario usuario;

	private UsuarioDAO usuarioDAO = new UsuarioDAO();

	private static AppManager instance = null;

	public static AppManager getInstance() {

		return instance == null ? instance = new AppManager() : instance;
	}

	private AppManager() {
	}

	public LocalDateTime getDataHoraAtual() {
		return usuarioDAO.getDateTime();
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

}
