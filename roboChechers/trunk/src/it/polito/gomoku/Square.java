package it.polito.gomoku;

public class Square {

	private byte row;
	private byte col;

	Square(final int row, final int col) {
		setRowCol(row, col);
	}

	public int getRow() {
		return this.row;
	}

	public int getCol() {
		return this.col;
	}

	public void setRowCol(final int row, final int col) {
		this.row = (byte) row;
		this.col = (byte) col;
	}

	public final void copyPosition(final Square from) {
		setRowCol(from.getRow(), from.getCol());
	}

	public final void init() {
		setRowCol(-1, -1);
	}

	public String toString() {
		return "[" + getRow() + ":" + getCol() + "]";
	}

	public static final Square create() {
		return create(-1, -1);
	}

	public static final Square create(int row, int col) {
		return new Square(row, col);
	}
}
