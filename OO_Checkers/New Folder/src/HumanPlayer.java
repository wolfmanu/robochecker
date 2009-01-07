

public class HumanPlayer implements Player {
	private final int piece;

	public HumanPlayer(final int piece) {
		this.piece = piece;
	}

	public void startNewGame() {
	}

	public Move makeMove(Board board) throws cantMoveException {
		//TODO: detect where the human player place it
		return Move.create();
	}

	public int getPiece() {
		return this.piece;
	}

	private Move[] myPossibleMoves(Board board) {
		return board.getPossibleMoves(this.piece); 
	}
}
