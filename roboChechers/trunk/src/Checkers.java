
public class Checkers {
	static final int WHITE = 1;
	static final int BLACK = 2;
	static final int WKING = 3;
	static final int BKING = 4;
	static final int EMPTY = 0;
	int toMove;
	int whiteDepth;
	int blackDepth;
	Board CB;
	
	public Checkers (int firstPlayer, int whiteDepth, int blackDepth) {
		this.CB = new Board();
		this.whiteDepth = whiteDepth;
		this.blackDepth = blackDepth;
		this.toMove = firstPlayer;
	}

	public Checkers () {
		new Checkers(WHITE, 6, 6); 
	}
	
	public boolean nextMove (Move nxtmv) {
		nxtmv.setPlayer(toMove);
		if (CB.doMove (nxtmv)) {
			switchPlayer();
			return true;
		} else {
			return false;
		}
	}
	
	private void switchPlayer () {
		this.toMove = (this.toMove == WHITE) ? BLACK : WHITE;
	}
}
