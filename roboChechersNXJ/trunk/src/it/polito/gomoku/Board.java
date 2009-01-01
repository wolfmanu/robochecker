package it.polito.gomoku;

public class Board {

	private final byte[] pieces;

	private final short rows;
	private final short cols;

	public Board(final int rows, final int cols) {
		this.rows = (short) rows;
		this.cols = (short) cols;
		this.pieces = new byte[rows * cols];
	}

	public int getRows() {
		return this.rows;
	}

	public int getCols() {
		return this.cols;
	}

	public Board copy() {
		Board newBoard = new Board(getRows(), getCols());
		System.arraycopy(this.pieces, 0, newBoard.pieces, 0, pieces.length);
		return newBoard;
	}

	public byte getPiece(final int row, final int col) {
		try {
			return pieces[toPosition(row, col)];
		} catch (ArrayIndexOutOfBoundsException e) {
			return GomokuConstants.NO_PIECE;
		}
	}

	public void setPiece(final int row, final int col, final byte piece) {
		pieces[toPosition(row, col)] = piece;
	}
	/*
	private void clear() {
		for (int i = 0, max = pieces.length; i < max; ++i) {
			pieces[i] = GomokuConstants.NO_PIECE;
		}
	} */

	public void printBoard() {
		System.out.println("\\ 01234567");
		for (int row = 0; row < rows; ++row) {
			System.out.print(row);
			System.out.print(" ");
			for (int col = 0; col < cols; ++col) {
				System.out.print(getPiece(row,col));
			}
			System.out.println();
		}
	}

	public void setPiece(final Square square, final byte piece) {
		setPiece(square.getRow(), square.getCol(), piece);
	}

	public int terminalTest() {
		int maxRow = getRows();
		int maxCol = getCols();
		int numOfEmptySquares = maxRow * maxCol;

		for (int row = 0; row < maxRow; ++row) {
			for (int col = 0; col < maxCol; ++col) {
				if (getPiece(row, col) != GomokuConstants.NO_PIECE) {
					--numOfEmptySquares;
					switch (victory(row, col)) {
					case GomokuConstants.WHITE_WIN:
						return GomokuConstants.WHITE_WIN;

					case GomokuConstants.BLACK_WIN:
						return GomokuConstants.BLACK_WIN;

					default:
						break;
					}
				}
			}
		}

		return numOfEmptySquares == 0 ? GomokuConstants.DRAW : GomokuConstants.NO_WIN;
	}

	public int victory(final int row, final int col) {
//		final boolean victory = false;

		final int playerPiece = getPiece(row, col);

		if (playerPiece == GomokuConstants.NO_PIECE) {
			return GomokuConstants.NO_WIN;
		}

		if (inARowAt(row, col, 5, 2) > 0) {
			return playerPiece == GomokuConstants.WHITE_PIECE ? GomokuConstants.WHITE_WIN : GomokuConstants.BLACK_WIN;
		}

		return GomokuConstants.NO_WIN;
	}

	private int inARowAt(final int row, final int col,
			final int num, final int maxcaps) {

		int count = 0;
		int r;
		int c;

		final int playerPiece = getPiece(row, col);
		final int opponentPiece = playerPiece == GomokuConstants.WHITE_PIECE
		? GomokuConstants.BLACK_PIECE : GomokuConstants.WHITE_PIECE;

		if (playerPiece == GomokuConstants.NO_PIECE) {
			return 0;
		}

		// Check right.
		if (rows - col >= num) {
			boolean inarow = true;

			for (c = col + 1; c < col + num && inarow; ++c) {
				if (getPiece(row, c) != playerPiece) {
					inarow = false;
				}
			}

			if (inarow) {
				int capCount = 0;

				if (col == 0) {
					capCount++;
				} else {
					if (getPiece(row, col - 1) == opponentPiece) {
						capCount++;
					}
				}

				if (col + num == cols) {
					capCount++;
				} else {
					if (getPiece(row, col + num) == opponentPiece) {
						capCount++;
					}
				}

				if (capCount <= maxcaps) {
					count++;
				}
			}
		}

		// Check down.
		if (rows - row >= num) {
			boolean inarow = true;

			for (r = row + 1; r < row + num && inarow; ++r) {
				if (getPiece(r, col) != playerPiece) {
					inarow = false;
				}
			}

			if (inarow) {
				int capCount = 0;
				if (row == 0) {
					capCount++;
				} else {
					if (getPiece(row - 1, col) == opponentPiece) {
						capCount++;
					}
				}

				if (row + num == rows) {
					capCount++;
				}

				if (getPiece(row + num, col) == opponentPiece) {
					capCount++;
				}

				if (capCount <= maxcaps) {
					count++;
				}
			}
		}

		// Check down-right
		if (rows - row >= num && cols - col >= num) {
			boolean inarow = true;

			for (r = row + 1, c = (col + 1);
			r < row + num && c < col + num && inarow; r++, c++) {
				if (getPiece(r, c) != playerPiece) {
					inarow = false;
				}
			}

			if (inarow) {
				int capCount = 0;

				// Upper-left bounds.
				if (row == 0 || col == 0) {
					capCount++;
				} else {
					if (getPiece(row - 1, col - 1) == opponentPiece) {
						capCount++;
					}
				}

				// Lower-right bounds.
				if (row + num == rows || col + num == cols) {
					capCount++;
				} else {
					if (getPiece(row + num, col + num) == opponentPiece) {
						capCount++;
					}
				}

				if (capCount <= maxcaps) {
					count++;
				}
			}
		}

		// Check down-left
		if (rows - row >= num && col >= num - 1) {
			boolean inarow = true;

			for (r = row + 1, c = col - 1;
			r < row + num && c >= 0 && inarow; r++, c--) {
				if (getPiece(r, c) != playerPiece) {
					inarow = false;
				}
			}

			if (inarow) {
				int capCount = 0;

				// Lower-left bounds.
				if (row == rows - 1 || col - num == 0) {
					capCount++;
				} else {
					if (getPiece(row + 1, col - 1) == opponentPiece) {
						capCount++;
					}
				}

				// Upper-right bounds.
				if (row == 0 || col == cols - 1) {
					capCount++;
				} else {
					if (getPiece(row - 1, col + 1) == opponentPiece) {
						capCount++;
					}
				}

				if (capCount <= maxcaps) {
					count++;
				}

			}
		}

		return count;
	}

	public int inARow(final int num, final int maxcaps, final int piece) {
		int count = 0;

		for (int row = 0; row < rows; ++row) {
			for (int col = 0; col < cols; ++col) {
				if (getPiece(row, col) == piece && inARowAt(row, col, num, maxcaps) != 0) {
					count++;
				}
			}
		}

		return count;
	}

	public boolean hasAdjacentPieces(final int row, final int col) {
		for (int r = row - 1; r <= row + 1; ++r) {
			for (int c = col - 1; c <= col + 1; ++c) {
				if (r == row && c == col) {
					continue;
				}

				if (r >= 0 && r < rows && c >= 0 && c < cols) {
					if (getPiece(r, c) != GomokuConstants.NO_PIECE) {
						return true;
					}
				}
			}
		}

		return false;
	}

	private int toPosition(final int row, final int col) {
		return row * cols + col;
	}

	public static final Board create(int rows, int cols) {
		return new Board(rows, cols);
	}
}
