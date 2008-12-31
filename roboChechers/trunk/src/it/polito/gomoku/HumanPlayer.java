package it.polito.gomoku;

import java.io.*;
import java.util.StringTokenizer;

public class HumanPlayer implements Player {
	private final byte piece;

	public HumanPlayer(final byte piece) {
		this.piece = piece;
	}

	public void startNewGame() {
	}

	public Square makeMove(Board board, Square opponentMove) {
		board.printBoard();
		System.out.print("where to put: ");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try {
			String in = br.readLine();
			StringTokenizer st = new StringTokenizer(in,",");
			int r = Integer.parseInt(st.nextToken());
			int c = Integer.parseInt(st.nextToken());
			return Square.create(r, c);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return null;
		}
	}

	public byte getPiece() {
		return this.piece;
	}

}
