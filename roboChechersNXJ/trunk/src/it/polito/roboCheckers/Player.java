package it.polito.roboCheckers;

import it.polito.Checkers.Board;
import it.polito.Checkers.Move;
import it.polito.Checkers.cantMoveException;


public interface Player {

	void startNewGame();

	Move makeMove(final Board board) throws cantMoveException, notCalibratedException;

	int getPiece();

}
