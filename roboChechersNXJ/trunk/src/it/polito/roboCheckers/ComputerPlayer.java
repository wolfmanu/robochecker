package it.polito.roboCheckers;
import lejos.nxt.Button;
import it.polito.Checkers.*;
import it.polito.Navigation.ArmController;
import it.polito.Navigation.CheckersNavigator;
import it.polito.Navigation.MathNavigator;
import it.polito.Navigation.notCalibratedException;


public class ComputerPlayer implements Player {
	private int piece;
	private int depth;
	private int score;
	private CheckersNavigator navigator;
	private ArmController arm;
		
	public ComputerPlayer(int piece, int depth) {
		this.piece = piece;
		this.navigator = MathNavigator.getInstance();
		this.arm = ArmController.getInstance();
		this.depth = depth;
		this.score = 0;
	}

	public ComputerPlayer(int piece) {
		this(piece, 6);
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
		System.out.println(nextMove.toString());
		navigator.goTo(nextMove.getFrom());
		arm.down();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {}
		arm.up();
		navigator.goTo(nextMove.getLastTo());
		arm.down();
		return nextMove;
	}

	public void startNewGame() {
		// TODO Auto-generated method stub

	}

	public int getScore() {
		return score;
	}

}
