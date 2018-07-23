package com.hrsoftware.cryptography;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Sha512Cryptography implements EncoderCryptography {

	@Override
	public String encode(String rawString) {
		return getSha512Crptography(rawString);
	}

	public static String getSha512Cryptography(String rawString) {
		return new Sha512Cryptography().encode(rawString);
	}

	@Override
	public boolean matches(String rawString, String encodedString) {
		return getSha512Crptography(rawString).equals(encodedString);
	}

	private String getSha512Crptography(String rawString) {

		String generatedPassword = null;

		try {

			MessageDigest md = MessageDigest.getInstance("SHA-512");
			byte[] bytes = md.digest(rawString.getBytes());
			StringBuilder sb = new StringBuilder();

			for (int i = 0; i < bytes.length; i++) {
				sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
			}

			generatedPassword = sb.toString();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return generatedPassword;
	}

}
