package it.polito.roboCheckers;

import it.polito.Checkers.Board;
import it.polito.Checkers.CheckersConstants;
import it.polito.Checkers.Move;
import it.polito.Checkers.cantMoveException;
import lejos.nxt.Button;

public class Game {
	private final int rows = 8, cols = 8;
	private final Player whitePlayer;
	private final Player blackPlayer;

	private final Board board;

	private boolean cancelled = false;

	public Game(Player whitePlayer, Player blackPlayer) {
		this.whitePlayer = whitePlayer;
		this.blackPlayer = blackPlayer;
		this.board = Board.create(this.rows, this.cols);
	}

	public Board getBoard() {
		return this.board;
	}

	public int play() throws notCalibratedException {
		Move lastMove = Move.create();
		this.cancelled = false;
		int test;
		do {
			try {
				lastMove = this.whitePlayer.makeMove(this.board);
				if (this.cancelled) {
					test = CheckersConstants.CANCELLED;
					break;
				}
				board.makeMove(lastMove);
			} catch (cantMoveException e) {
				test = opponentWins(this.whitePlayer.getPiece());
				break;
			}
			board.printBoard();
			Button.waitForPress();
			try {
				lastMove = this.blackPlayer.makeMove(this.board);
				if (this.cancelled) {
					test = CheckersConstants.CANCELLED;
					break;
				}
				board.makeMove(lastMove);
			} catch (cantMoveException e) {
				test = opponentWins(this.blackPlayer.getPiece());
				break;
			}
			board.printBoard();
			Button.waitForPress();
		} while (true);
		board.printBoard();
		Button.waitForPress();
		return test;
	}

	public void cancel() {
		this.cancelled = true;
	}

	private static int opponentWins(int piece) {
		switch (piece) {
		case CheckersConstants.WHITE:
			return CheckersConstants.BLACK_WIN;
		case CheckersConstants.BLACK:
			return CheckersConstants.WHITE_WIN;
		default:
			return CheckersConstants.NO_WIN;
		}
	}
}
