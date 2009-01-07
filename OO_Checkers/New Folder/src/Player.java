

public interface Player {

	void startNewGame();

	Move makeMove(final Board board) throws cantMoveException;

	int getPiece();

}
