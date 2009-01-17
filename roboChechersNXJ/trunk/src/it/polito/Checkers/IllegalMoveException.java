package it.polito.Checkers;

public class IllegalMoveException extends Exception {
	public IllegalMoveException() {
		super();
	}

	public IllegalMoveException(String message) {
		super(message);
	}
}
