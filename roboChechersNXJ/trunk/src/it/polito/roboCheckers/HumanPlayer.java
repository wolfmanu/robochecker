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
	private final int piecek;
	private final CheckersNavigator navigator;
	private final ColorSensor CS;
	public HumanPlayer(final int piece, final int piecek) {
		this.piece = piece;
		this.piecek = piecek;
		this.navigator = MathNavigator.getInstance();
		this.CS = Robot.getColorSensor();
	}

	public void startNewGame() {
	}

	public Move makeMove(Board board) throws cantMoveException, notCalibratedException, IllegalMoveException {
		Vector<MovesCollections> moves = myPossibleMoves(board);
		if (moves.size()==0)
			throw new cantMoveException();
		
		System.out.println("Waiting for human to move");
		Button.waitForPress();
		
		Square from = null;
		boolean foundFrom = false;
		MovesCollections m=null;
		for (int i=moves.size()-1; i>=0;i--) {
			m = moves.elementAt(i);
			from = m.getFrom();
			this.navigator.goTo(from);
			if (CS.getColorNumber() == CheckersConstants.EMPTY) {
				foundFrom = true;
				break;
			}
		}
		
		if (!foundFrom || m==null)
			throw new IllegalMoveException();
		
		Move theMove = null;
		Vector<Square[]> vTo = m.getTos();
		for (int i = 0; i < vTo.size(); i++) {
			Square[] s = vTo.elementAt(i);
			navigator.goTo(s[s.length - 1]);
			System.out.println("color: "+CS.getColorNumber());
			if (CS.getColorNumber() == piece || CS.getColorNumber() == piecek) {
				theMove = Move.create(from, s);
				break;
			}
		}
		
		if (theMove == null)
			throw new IllegalMoveException();
		
		return theMove;
	}

	public int getPiece() {
		return this.piece;
	}

	private Vector<MovesCollections> myPossibleMoves(Board board) {
		return board.getPossibleMoves(this.piece); 
	}
}
