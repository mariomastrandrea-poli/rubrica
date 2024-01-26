package it.mariomastrandrea.testrubrica.models;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class User {
	private static final String hashAlgorithm = "SHA-256";
	
	private Integer id;
	private String username;
	private String passwordHash;

	public User(Integer id, String username, String passwordHash) {
		this.id = id;
		this.username = username;
		this.passwordHash = passwordHash;	
	}
	
	public User(String username, String password) {
		this.id = null;
		this.username = username;
		
		// compute password hash
		this.passwordHash = this.computeHashOf(password);
		System.out.println(this.passwordHash);
	}
	
	public Integer getId() {
		return this.id;
	}
	
	public String getUsername() {
		return this.username;
	}
	
	public String getPasswordhash() {
		return this.passwordHash;
	}
	
	private String computeHashOf(String string) {
		try {
            // create the binary digest
            MessageDigest md = MessageDigest.getInstance(hashAlgorithm);
            byte[] hashBytes = md.digest(string.getBytes(StandardCharsets.UTF_8));

        	// create hash string in hexadecimal format
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }

            return sb.toString();
        } 
		catch (Exception e) {
            e.printStackTrace();
            return null;
        }
	}
	
	public User clone() {
		return new User(this.id, this.username, this.passwordHash);
	}
}
