package it.polito.roboCheckers;

import it.polito.Checkers.*;
import it.polito.Navigation.ArmController;
import it.polito.Navigation.CheckersNavigator;
import it.polito.Navigation.NotCalibratedException;
import it.polito.util.HumanInput;
import it.polito.util.Vector;
import lejos.nxt.addon.ColorSensor;

public class HumanPlayer implements Player {
	private final int piece;
	private final int piecek;
	private final CheckersNavigator navigator = Factory.getCheckersNavigator();
	private final ColorSensor CS = Factory.getColorSensor();
	private final HumanInput HI = Factory.getHumanInput();
	private final ArmController arm = Factory.getArmController();

	public HumanPlayer(final int piece, final int piecek) {
		this.piece = piece;
		this.piecek = piecek;
	}

	public void startNewGame() {
	}

	public Move makeMove(Board board) throws CantMoveException,
			NotCalibratedException, IllegalMoveException {
		board.initPossibleMoves(getPiece(),navigator.homePosition());
		System.out.println("Waiting for human to move");
		HI.waitForMove(true);
		arm.down(true);
		Square from = null;

		while(true) {
			from = board.getPossibleMoveFrom();
			navigator.goTo(from);
			if (CS.getColorNumber() == CheckersConstants.EMPTY) {
				break;
			}
		}

		Move theMove = null;
		while (true) {
			Square[] s = board.getPossibleMoveTo();
			navigator.goTo(s[s.length - 1]);
			if (CS.getColorNumber() == piece || CS.getColorNumber() == piecek) {
				theMove = Move.create(from, s);
				break;
			}
		}

		arm.up(true);
		return theMove;
	}

	public int getPiece() {
		return this.piece;
	}

}
