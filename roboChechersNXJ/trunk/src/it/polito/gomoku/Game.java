package it.polito.gomoku;

public class Game {

	private final Player whitePlayer;
	private final Player blackPlayer;

	private final Board board;

	private boolean cancelled = false;

	public Game(Player whitePlayer, Player blackPlayer, byte rows, byte cols) {
		this.whitePlayer = whitePlayer;
		this.blackPlayer = blackPlayer;
		this.board = Board.create(rows, cols);
	}

	public Board getBoard() {
		return this.board;
	}

	public byte play() {
		Square lastMove = Square.create();

		this.cancelled = false;

		byte test;

		do {
			lastMove = this.whitePlayer.makeMove(this.board, lastMove);
			if (this.cancelled) {
				test = GomokuConstants.CANCELLED;
				break;
			}
			markMove(lastMove, this.whitePlayer.getPiece());

			test = (byte) board.terminalTest();
			if (test != GomokuConstants.NO_WIN) {
				break;
			}

			lastMove = this.blackPlayer.makeMove(this.board, lastMove);
			if (this.cancelled) {
				test = GomokuConstants.CANCELLED;
				break;
			}
			markMove(lastMove, this.blackPlayer.getPiece());

			test = (byte) board.terminalTest();
			if (test != GomokuConstants.NO_WIN) {
				break;
			}
		} while (true);

		return test;
	}

	public void cancel() {
		this.cancelled = true;
	}

	private void markMove(final Square move, byte piece) {
		board.setPiece(move, piece);
	}
}
