package br.com.jonmarques.demoparkapi.exception;

public class UsernameUniqueViolationException extends RuntimeException {
	
	public UsernameUniqueViolationException(String message) {
		super(message);
	}

}
