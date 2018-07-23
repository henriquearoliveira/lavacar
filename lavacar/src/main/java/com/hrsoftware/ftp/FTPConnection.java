package com.hrsoftware.ftp;

public interface FTPConnection {

    boolean download(String arquivo);

    boolean  upload();

}
