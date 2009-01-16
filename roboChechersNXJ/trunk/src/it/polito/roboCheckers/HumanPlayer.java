package it.polito.roboCheckers;

import it.polito.Checkers.Board;
import it.polito.Checkers.CheckersConstants;
import it.polito.Checkers.IllegalMoveException;
import it.polito.Checkers.Move;
import it.polito.Checkers.MovesCollections;
import it.polito.Checkers.Square;
import it.polito.Checkers.cantMoveException;
import it.polito.Navigation.CheckersNavigator;
import it.polito.Navigation.notCalibratedException;
import it.polito.util.HumanInput;
import it.polito.util.Vector;
import lejos.nxt.addon.ColorSensor;


public class HumanPlayer implements Player {
	private final int piece;
	private final int piecek;
	private final CheckersNavigator navigator = Factory.getCheckersNavigator();
	private final ColorSensor CS = Factory.getColorSensor();
	private final HumanInput HI = Factory.getHumanInput();
	
	public HumanPlayer(final int piece, final int piecek) {
		this.piece = piece;
		this.piecek = piecek;
	}

	public void startNewGame() {
	}

	public Move makeMove(Board board) throws cantMoveException, notCalibratedException, IllegalMoveException {
		Vector<MovesCollections> moves = myPossibleMoves(board);
		System.out.println("Waiting for human to move");
		HI.waitForMove(true);
		
		Square from = null;
		boolean foundFrom = false;
		MovesCollections m=null;
		for (int i=moves.size()-1; i>=0;i--) {
			m = moves.elementAt(i);
			from = m.getFrom();
			navigator.goTo(from);
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

	private Vector<MovesCollections> myPossibleMoves(Board board) throws cantMoveException {
		return board.getPossibleMoves(this.piece); 
	}
}
