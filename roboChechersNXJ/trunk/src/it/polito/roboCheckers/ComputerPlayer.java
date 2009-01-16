package it.polito.roboCheckers;
import it.polito.Checkers.*;
import it.polito.Navigation.ArmController;
import it.polito.Navigation.CheckersNavigator;
import it.polito.Navigation.MathNavigator;
import it.polito.Navigation.SimpleNavigator;
import it.polito.Navigation.notCalibratedException;


public class ComputerPlayer implements Player {
	private final int piece;
	private final int piecek;
	private int depth;
	private int score;
	private final CheckersNavigator navigator = Factory.getCheckersNavigator();
	private final ArmController arm = Factory.getArmController();
		
	public ComputerPlayer(final int piece, final int piecek, int depth) {
		this.piece = piece;
		this.piecek = piecek;
		this.depth = depth;
		this.score = 0;
	}

	public ComputerPlayer(final int piece, final int piecek) {
		this(piece, piecek, 6);
	}
	
	public int getPiece() {
		return this.piece;
	}

	public Move makeMove(Board board) throws cantMoveException, notCalibratedException {
		int[] result = new int[4];
		int[] counter = new int[1];
		counter[0]=0;
		Move nextMove;
		System.out.println("thinking...");
		this.score = Engine.MiniMax(board.getArrayBoard(), 0, depth, result, this.piece, counter);
		nextMove = Move.fromArray(result);
		System.out.println("MyMove:" + nextMove.toString());
		navigator.goTo(nextMove.getFrom());
		arm.down();
		try {	Thread.sleep(800);	} catch (InterruptedException e) {}
		arm.up();
		navigator.goTo(nextMove.getLastTo());
		arm.down();
		try {	Thread.sleep(800);	} catch (InterruptedException e) {}
		navigator.goHome(); //TODO: go home and recheck vertical alignment
		return nextMove;
	}

	public void startNewGame() {
	}

	public int getScore() {
		return score;
	}

}
