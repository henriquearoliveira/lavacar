package com.hrsoftware.cryptography;

public interface EncoderCryptography {
	
	public String encode(String rawString);
	
	public boolean matches(String rawString, String encodedString);

}
