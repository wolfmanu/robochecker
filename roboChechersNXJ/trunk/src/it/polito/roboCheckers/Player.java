package it.polito.roboCheckers;

import it.polito.Checkers.*;
import it.polito.Navigation.NotCalibratedException;

public interface Player {

	void startNewGame(); // TODO: Maybe useless... should remove?

	Move makeMove(final Board board) throws CantMoveException,
			IllegalMoveException, NotCalibratedException;

	int getPiece();

}
