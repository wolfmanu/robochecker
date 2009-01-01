
public class Board {
	int[][] chessBoard;

	public Board () {
		new Board(8,8);
	}
	private Board (int rows, int cols) {
		chessBoard = new int[rows][cols];
		initBoard();
	}

	public void initBoard() {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++)
				chessBoard[i][j] = Checkers.EMPTY;

			for (int j = 0; j < 3; j++)
				if (possible_square(i, j))
					chessBoard[i][j] = Checkers.WHITE;

			for (int j = 5; j < 8; j++)
				if (possible_square(i, j))
					chessBoard[i][j] = Checkers.BLACK;
		}

	}

	private boolean possible_square(int i, int j) {
		return (i + j) % 2 == 1;
	}
	
	public boolean doMove(Move nxtmv) {
		// TODO Auto-generated method stub
		return false;
	}
	
}