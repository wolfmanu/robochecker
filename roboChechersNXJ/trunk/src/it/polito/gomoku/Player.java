package it.polito.gomoku;

public interface Player {

	void startNewGame();

	Square makeMove(final Board board, final Square opponentMove);

	byte getPiece();

}
