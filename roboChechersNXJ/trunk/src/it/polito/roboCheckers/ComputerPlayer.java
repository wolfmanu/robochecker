package it.polito.roboCheckers;
import lejos.nxt.Button;
import it.polito.Checkers.*;


public class ComputerPlayer implements Player {
	private int piece;
	private int depth;
	private int score;
	
	public ComputerPlayer(int piece, int depth) {
		this.piece = piece;
		this.depth = depth;
		this.score = 0;
	}

	public ComputerPlayer(int piece) {
		this(piece, 6);
	}
	
	public int getPiece() {
		return this.piece;
	}

	public Move makeMove(Board board) throws cantMoveException {
		int[] result = new int[4];
		int[] counter = new int[1];
		counter[0]=0;
		Move nextMove;
		System.out.println("thinking...");
		this.score = Engine.MiniMax(board.getArrayBoard(), 0, depth, result, this.piece, counter);
		nextMove = Move.fromArray(result);
		System.out.println(nextMove.toString());
		return nextMove;
	}

	public void startNewGame() {
		// TODO Auto-generated method stub

	}

	public int getScore() {
		return score;
	}

}
