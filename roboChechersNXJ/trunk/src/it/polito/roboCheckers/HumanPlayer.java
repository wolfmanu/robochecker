package it.polito.roboCheckers;

import lejos.nxt.Button;
import lejos.nxt.addon.ColorSensor;
import it.polito.Checkers.*;
import it.polito.Navigation.CheckersNavigator;
import it.polito.Navigation.MathNavigator;
import it.polito.Navigation.notCalibratedException;
import it.polito.util.Vector;


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
		MovesCollections[] moves = myPossibleMoves(board);
		if (moves.length==0)
			throw new cantMoveException();
		
		System.out.println("Waiting for human to move");
		Button.waitForPress();
		
		Square from = null;
		MovesCollections f=null;
		boolean foundFrom = false;
		for (MovesCollections m:moves) {
			from = m.getFrom();
			f=m;
			this.navigator.goTo(from);
			if (CS.getColorNumber() == CheckersConstants.EMPTY) {
				foundFrom = true;
				break;
			}
		}
		
		if (!foundFrom)
			throw new IllegalMoveException();
		
		Move theMove = null;
		Vector<Square[]> vTo = f.getTos();
		for (int i=0; i<vTo.size(); i++)
			{
			 Square[] s = vTo.elementAt(i);
			 navigator.goTo(s[s.length-1]);
			 if (CS.getColorNumber() == piece) 
				 theMove = Move.create(from, s);
		}
		
		if (theMove == null)
			throw new IllegalMoveException();
		
		return theMove;
	}

	public int getPiece() {
		return this.piece;
	}

	private MovesCollections[] myPossibleMoves(Board board) {
		return board.getPossibleMoves(this.piece); 
	}
}
