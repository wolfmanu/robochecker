package it.polito.roboCheckers;

import it.polito.Checkers.Board;
import it.polito.Checkers.IllegalMoveException;
import it.polito.Checkers.Move;
import it.polito.Checkers.cantMoveException;


public interface Player {

	void startNewGame(); // TODO: Maybe useless... should remove?

	Move makeMove(final Board board) throws cantMoveException, IllegalMoveException, notCalibratedException;

	int getPiece();

}
