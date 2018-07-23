package com.hrsoftware.ftp;

public class FTPRetrieveFile extends FTPAbstract {

    @Override
    public boolean download(String arquivo) {
        return downloadArquivo(arquivo);
    }

    @Override
    public boolean upload() {
        return false;
    }
}
