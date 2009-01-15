package it.polito.Checkers;
import lejos.nxt.Button;
import it.polito.util.*;

public class Board {

	private final int[][] pieces;
	private int indice;
	private int indice2;
	private final short rows;
	private final short cols;
	private Vector<MovesCollections> mosse;

	public Board(final int rows, final int cols) {
		this.rows = (short) rows;
		this.cols = (short) cols;
		this.pieces = new int[rows][cols];
		initBoard();
	}

	public Board() {
		this(8,8);
	}
	
	public static final Board create(int rows, int cols) {
		return new Board(rows, cols);
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

	public void printBoard() {

		System.out.println("\\ 01234567");
		for (int row = 0; row < rows; ++row) {
			System.out.print(""+row);
			System.out.print(" ");
			for (int col = 0; col < cols; ++col) {
				if (getPiece(row,col) == CheckersConstants.WHITE)
					System.out.print("R");
				else if (getPiece(row,col) == CheckersConstants.BLACK)
					System.out.print("N");
				else if (getPiece(row,col) == CheckersConstants.WKING)
					System.out.print("G");
				else if (getPiece(row,col) == CheckersConstants.BKING)
					System.out.print("B");
				else
					System.out.print(" ");
			}
			System.out.println("");
		}
	}

	public int[][] getArrayBoard() {
		return pieces;
	}

	public void makeMove(Move lastMove) {
		Engine.move_board(getArrayBoard(), lastMove.toArray());		
	}
	
	public Vector<MovesCollections> getPossibleMoves(int piece) throws cantMoveException {
		Vector<int[]> moves = Engine.generate_moves(getArrayBoard(), piece);
		Vector<MovesCollections> movesArray = new Vector<MovesCollections>();
		int flag;
		if (moves.size()==0)
		   throw new cantMoveException();	
		for (int i = 0; i < moves.size(); i++) {
			try {
				flag = 0;
				Move m = Move.fromArray(moves.elementAt(i));
				for (int j = 0; j < movesArray.size() && flag == 0; j++) {
					if (movesArray.elementAt(j).equals(m)) {
						movesArray.elementAt(j).SetTo(m);
						flag = 1;
					}
				}
				if (flag == 0) {
					movesArray.addElement(new MovesCollections(m));
				}

			} catch (cantMoveException e) {
			}

		}
		indice = 0;
		mosse = movesArray;
		// System.out.println("movesArray.size = " + movesArray.size());
		// Button.waitForPress();
		return movesArray;
		/*
		 * } catch (Exception ne) { System.out .println(ne.getClass().toString()
		 * + " " + ne.getMessage()); Button.waitForPress(); return mosse; }
		 */

	}
	
	public Square getPossibleMoveFrom() throws cantMoveException {
		indice2 = 0;
		if (indice == mosse.size())
			throw new cantMoveException();
		return mosse.elementAt(indice++).getFrom();
	}

	public Square[] getPossibleMoveTo() throws cantMoveException {
		if (indice2 == mosse.elementAt(indice).getTos().size())
			throw new cantMoveException();
		Square[] s = mosse.elementAt(indice).getTos().elementAt(indice2++);
		return s;
	}

}
