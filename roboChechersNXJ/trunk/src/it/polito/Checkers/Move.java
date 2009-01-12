	package it.polito.Checkers;

import it.polito.util.*;

public class Move {

	private Square from;
	private Square[] to;

	public Move (Square from, Square to) {
		this.from = from;
		
		/*this.to = new Square[1];
		this.to[0] = to;*/
	}

	public Move (Square from, Square[] to) {
		this.from = from;
		this.to=to;
	}
	
	public void setFrom (Square from) {
		this.from = from;
	}

	public void setTo (Square to) {
		this.to = new Square[1];
		this.to[0] = to;
	}
	
	public void setTo (Square[] to) {
		this.to = to;
	}
	
	public Square getFrom () {
		return this.from;
	}
	public Square[] getTo () {
		return this.to;
	}
	public Square getLastTo () {
		return this.to[this.to.length-1];
	}
	
	public void addTo (Square to) {
		int prevLen = this.to.length;
		Square[] newTo = new Square[prevLen+1];
		System.arraycopy(this.to, 0, newTo, 0, prevLen);
		newTo[prevLen] = to;
		this.to = newTo;
	}

	public boolean isCapture () { return (Math.abs(from.getRow()-to[0].getRow())>1); }
	public boolean isWalk () { return (Math.abs(from.getRow()-to[0].getRow())==1); }
	public boolean multipleCapture () { return (this.to.length > 1); }
	
	public static final Move create() {
		return null;
	}
	public static final Move create(Square from, Square to) {
		return new Move(from, to);
	}
	public static final Move create(Square from, Square[] to) {
		return new Move(from, to);
	}
	public static final Move fromArray(int[] arrayMove) throws cantMoveException {
		int startx = arrayMove[0];
		int starty = arrayMove[1];
		if (startx == 0 && starty == 0)
			throw new cantMoveException();
		int endx = arrayMove[2];
		int endy = arrayMove[3];
		Move newMove = new Move(new Square(startx, starty), new Square(endx % 10, endy % 10));
		for (endx /= 10, endy /= 10 ; endx > 0 || endy > 0; endx /= 10, endy /= 10) {
			newMove.addTo(new Square(endx,endy));
		}
		return newMove;
	}
	public int[] toArray() {
		int[] arrayMove = new int[4];
		arrayMove[0] = this.from.getRow();
		arrayMove[1] = this.from.getCol();
		arrayMove[2] = arrayMove[3] = 0;
		for (Square s:to) {
			arrayMove[2] *= 10;
			arrayMove[2] += s.getRow();
			arrayMove[3] *= 10;
			arrayMove[3] += s.getCol();
		}
		return arrayMove; 
	}
	
	public String toString() {
		StringBuffer output = new StringBuffer(from.toString());
		for (Square s:to) {
			output.append(" -> " + s.toString());
		}
		return output.toString();
	}
}
