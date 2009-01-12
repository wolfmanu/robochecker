package it.polito.Checkers;
import it.polito.util.Vector;

public class Board {

	private final int[][] pieces;
	private int indice;
	private final short rows;
	private final short cols;
	private MovesCollections[] mosse;

	public Board(final int rows, final int cols) {
		this.rows = (short) rows;
		this.cols = (short) cols;
		this.pieces = new int[rows][cols];
		initBoard();
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
	
	public MovesCollections[] getPossibleMoves (int piece) {
		Vector<int[]> moves = Engine.generate_moves(getArrayBoard(), piece);
		MovesCollections[] movesArray = new MovesCollections[moves.size()];
		int flag;
		for (int i = 0; i<moves.size(); i++) {
			try { 
				 flag=0;
				 Move m=Move.fromArray(moves.elementAt(i));
				 for (int j=i; j>0 && flag==0; j--) 
				 {
					if (movesArray[j].equals(m))
					 {
					  movesArray[j].SetTo(m); 
					  flag=1;
					 }
				 }

				if (flag==1)
				 {
					movesArray[i]=new MovesCollections(m);
				 } 
				 
				}
			catch (cantMoveException e) {}
		}
		indice =0;
		mosse=movesArray;
		return movesArray;
	}
	
	public Square getPossibleMove() {
		return mosse[indice++].getFrom();
	}

}
