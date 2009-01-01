package it.polito.gomoku;

public class HumanPlayer implements Player {
	private final byte piece;

	public HumanPlayer(final byte piece) {
		this.piece = piece;
	}

	public void startNewGame() {
	}

	public Square makeMove(Board board, Square opponentMove) {
		board.printBoard();
		//TODO: detect where the human player place it
		int r = 0;
		int c = 0;
		return Square.create(r, c);
	}

	public byte getPiece() {
		return this.piece;
	}

}
