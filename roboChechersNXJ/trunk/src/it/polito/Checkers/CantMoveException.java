package it.polito.Checkers;

public class CantMoveException extends Exception {

	public CantMoveException() {
	}

	public CantMoveException(String message) {
		super(message);
	}
}
