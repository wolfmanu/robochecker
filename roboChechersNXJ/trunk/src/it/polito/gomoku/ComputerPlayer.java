package it.polito.gomoku;

public final class ComputerPlayer implements Player {
	private boolean alphaBetaPruningEnabled;
	private int searchDepth;
	private int numPositionsEvaluated;

	private byte piece;

	private static final int MAXWIN = 10000;
	private static final int MINWIN = -10000;

	public ComputerPlayer(byte piece) throws Exception {
		if (piece != GomokuConstants.BLACK_PIECE)
			throw new Exception("AI Can only play as black !!");
		this.piece = piece;
		numPositionsEvaluated = 0;
		searchDepth = 2;
		alphaBetaPruningEnabled = false;
	}

	public final byte getPiece() {
		return this.piece;
	}

	public Square makeMove(final Board board, final Square opponentMove) {
		final Square computerMove = Square.create();

		// Do a computer makeMove.
		int alpha = MINWIN - 1;
		int beta = MAXWIN + 1;
		numPositionsEvaluated = 0;
		move(0, searchDepth, board, computerMove, this.piece, alpha, beta);
		//System.out.print("my move:");
		//System.out.println(computerMove.toString());
		return computerMove;
	}

	private int move(final int curdepth, final int maxdepth,
			final Board board,
			final Square squareOut, final byte turn, int alpha, int beta) {
		if (curdepth == maxdepth) {
			squareOut.init();
			++numPositionsEvaluated;
			return eval(board);
		}

		int max = MINWIN - 1;
		int min = MAXWIN + 1;

		final Square potentialSquare = Square.create();

		int moveVal;
		Square s = Square.create();

		while (getNextPossibleMove(s, board)) {
			final Board b = board.copy();
			b.setPiece(s, turn);

			if (turn == GomokuConstants.BLACK_PIECE) {
				switch (b.terminalTest()) {
				case GomokuConstants.WHITE_WIN:
					moveVal = MINWIN;
					break;
				case GomokuConstants.BLACK_WIN:
					moveVal = MAXWIN;
					break;
				default:
					moveVal = move(curdepth + 1, maxdepth, b, potentialSquare,
							GomokuConstants.WHITE_PIECE, alpha, beta);
				break;
				}

				if (moveVal > max) {
					squareOut.copyPosition(s);
					max = moveVal;
				}

				if (alphaBetaPruningEnabled) {
					alpha = alpha > moveVal ? alpha : moveVal;
					if (alpha >= beta) {
						return beta;
					}
				}
			} else {
				switch (b.terminalTest()) {
				case GomokuConstants.WHITE_WIN:
					moveVal = MINWIN;
					break;
				case GomokuConstants.BLACK_WIN:
					moveVal = MAXWIN;
					break;
				default:
					moveVal = move(curdepth + 1, maxdepth, b, potentialSquare,
							GomokuConstants.BLACK_PIECE, alpha, beta);
				break;
				}

				if (moveVal < min) {
					squareOut.copyPosition(s);
					min = moveVal;
				}

				if (alphaBetaPruningEnabled) {
					beta = beta < moveVal ? beta : moveVal;
					if (beta <= alpha) {
						return alpha;
					}
				}
			}
		}

		if (alphaBetaPruningEnabled) {
			return turn == GomokuConstants.BLACK_PIECE ? alpha : beta;
		}


		return turn == GomokuConstants.BLACK_PIECE ? max : min;
	}

	private static boolean getNextPossibleMove(final Square square, final Board parentBoard) {
		byte row = (byte) square.getRow();
		byte col = (byte) square.getCol();

		if (row == -1 || col == -1) {
			row = 0;
			col = 0;
		} else {
			++col;
			if (col == parentBoard.getCols()) {
				++row;
				col = 0;
			}
		}

		while (row < parentBoard.getRows()) {
			while (col < parentBoard.getCols()) {
				if (parentBoard.getPiece(row, col) == GomokuConstants.NO_PIECE
						&& parentBoard.hasAdjacentPieces(row, col)) {
					square.setRowCol(row, col);
					return true;
				}

				++col;
			}

			col = 0;
			++row;
		}

		return false;
	}

	private static int eval(final Board b) {
		final Stats c = new Stats(b, GomokuConstants.BLACK_PIECE);
		final Stats u = new Stats(b, GomokuConstants.WHITE_PIECE);

		int retVal = 0;

		if (u.uncapped4 > 0) {
			return MINWIN;
		}


		if (c.uncapped4 > 0) {
			return MAXWIN;
		}

		retVal += c.capped2 * 5;
		retVal -= u.capped2 * 5;

		retVal += c.uncapped2 * 10;
		retVal -= u.uncapped2 * 10;

		retVal += c.capped3 * 20;
		retVal -= u.capped3 * 30;

		retVal += c.uncapped3 * 100;
		retVal -= u.uncapped3 * 120;

		retVal += c.capped4 * 500;
		retVal -= u.capped4 * 500;

		return Math.max(MINWIN, Math.min(MAXWIN, retVal));
	}

	public void startNewGame() {
		numPositionsEvaluated = 0;
	}

	void setSearchDepth(final int newDepth) {
		searchDepth = newDepth;
	}

	int getSearchDepth() {
		return searchDepth;
	}

	void setAlphaBetaPruningEnabled(final boolean abOn) {
		alphaBetaPruningEnabled = abOn;
	}

	int getNumPositionsEvaluated() {
		return numPositionsEvaluated;
	}

	static final class Stats {
		Stats(final Board board, final byte piece) {
			uncapped2 = board.inARow(2, 0, piece);
			capped2 = board.inARow(2, 1, piece);

			uncapped3 = board.inARow(3, 0, piece);
			capped3 = board.inARow(3, 1, piece);

			uncapped4 = board.inARow(4, 0, piece);
			capped4 = board.inARow(4, 1, piece);
		}

		final int capped2;
		final int uncapped2;

		final int capped3;
		final int uncapped3;

		final int capped4;
		final int uncapped4;
	}
}
