package it.polito.roboCheckers;

import java.io.IOException;

import lejos.nxt.Button;
import lejos.nxt.addon.ColorSensor;
import it.polito.BluetoothComm.NXTCommHandle;
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
		Color c = new Color();
		System.out.println("Waiting for human to move");
		try {
			NXTCommHandle.getInstance().waitForMove();
		} catch (IOException e) {
			System.out.println("IOException");
			System.out.println("Use NXT Button");
			Button.waitForPress();
		}
		
		Square from = null;
		boolean foundFrom = false;
		MovesCollections m=null;
		for (int i=moves.size()-1; i>=0;i--) {
			m = moves.elementAt(i);
			from = m.getFrom();
			this.navigator.goTo(from);
			c.setColor(CS.getRed(), CS.getGreen(), CS.getBlue());
			if (c.equals(Color.getInstance(Color.EMPTY))) {
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
			c.setColor(CS.getRed(), CS.getGreen(), CS.getBlue());
			if (c.equals(Color.getInstance(piece)) || c.equals(Color.getInstance(piecek))) {
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
