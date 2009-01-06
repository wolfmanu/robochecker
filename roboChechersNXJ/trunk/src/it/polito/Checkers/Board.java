package it.polito.Checkers;
import it.polito.util.Vector;

public class Board {

	private final int[][] pieces;

	private final short rows;
	private final short cols;

	public Board(final int rows, final int cols) {
		this.rows = (short) rows;
		this.cols = (short) cols;
		this.pieces = new int[rows][cols];
		initBoard();
	}
	public void initBoard() {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++)
				pieces[i][j] = CheckersConstants.EMPTY;

			for (int j = 0; j < 3; j++)
				if (possible_square(i, j))
					pieces[i][j] = CheckersConstants.WHITE;

			for (int j = 5; j < 8; j++)
				if (possible_square(i, j))
					pieces[i][j] = CheckersConstants.BLACK;
		}

	}
	private boolean possible_square(int i, int j) {
		return (i + j) % 2 == 1;
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

	public int getPiece(final int row, final int col) {
		try {
			return pieces[row][col];
		} catch (ArrayIndexOutOfBoundsException e) {
			return CheckersConstants.EMPTY;
		}
	}

	public void setPiece(final int row, final int col, final int piece) {
		pieces[row][col] = piece;
	}
/*
	public void printBoard() {

		System.out.println("\\ 01234567");
		for (int row = 0; row < rows; ++row) {
			System.out.print(""+row);
			System.out.print(" ");
			for (int col = 0; col < cols; ++col) {
				System.out.print(""+getPiece(row,col));
			}
			System.out.println("");
		}
	}
*/
	public int[][] getArrayBoard() {
		return pieces;
	}

	public void makeMove(Move lastMove) {
		Engine.move_board(getArrayBoard(), lastMove.toArray());		
	}
	
	public Move[] getPossibleMoves (int piece) {
		Vector<int[]> moves = Engine.generate_moves(getArrayBoard(), piece);
		Move[] movesArray = new Move[moves.size()];
		for (int i = 0; i<moves.size(); i++) {
			try { movesArray[i] = Move.fromArray(moves.elementAt(i)); }
			catch (cantMoveException e) {}
		}
		return movesArray;
	}
	
	public static final Board create(int rows, int cols) {
		return new Board(rows, cols);
	}

}
