package it.polito.roboCheckers;

import lejos.nxt.Button;
import lejos.nxt.addon.ColorSensor;
import it.polito.Checkers.*;
import it.polito.Navigation.CheckersNavigator;
import it.polito.Navigation.MathNavigator;
import it.polito.Navigation.notCalibratedException;


public class HumanPlayer implements Player {
	private final int piece;
	private final CheckersNavigator navigator;
	private final ColorSensor CS;
	public HumanPlayer(final int piece) {
		this.piece = piece;
		this.navigator = MathNavigator.getInstance();
		this.CS = Robot.getColorSensor();
	}

	public void startNewGame() {
	}

	public Move makeMove(Board board) throws cantMoveException, notCalibratedException, IllegalMoveException {
		Move[] moves = myPossibleMoves(board);
		if (moves.length==0)
			throw new cantMoveException();
		
		System.out.println("Waiting for human to move");
		Button.waitForPress();
		
		Square from = null;
		boolean foundFrom = false;
		for (Move m:moves) {
			from = m.getFrom();
			this.navigator.goTo(from);
			if (CS.getColorNumber() == CheckersConstants.EMPTY) {
				foundFrom = true;
				break;
			}
		}
		if (!foundFrom)
			throw new IllegalMoveException();
		
		Move theMove = null;
		for (Move m:moves) {
			if (m.getFrom().equals(from)) {
				navigator.goTo(m.getLastTo());
				if (CS.getColorNumber() == piece) {
					theMove = m;
				}
			}
		}
		if (theMove == null)
			throw new IllegalMoveException();
		
		return theMove;
	}

	public int getPiece() {
		return this.piece;
	}

	private Move[] myPossibleMoves(Board board) {
		return board.getPossibleMoves(this.piece); 
	}
}
