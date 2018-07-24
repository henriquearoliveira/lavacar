package com.hrsoftware.relatorios;

public class FTP {

	private String hostFTP, username, password;

	private int port;

	private PathReports pathReports;

	public String getHostFTP() {
		return hostFTP;
	}

	public void setHostFTP(String hostFTP) {
		this.hostFTP = hostFTP;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public PathReports getPathReports() {
		return pathReports;
	}

	public void setPathReports(PathReports pathReports) {
		this.pathReports = pathReports;
	}

}
