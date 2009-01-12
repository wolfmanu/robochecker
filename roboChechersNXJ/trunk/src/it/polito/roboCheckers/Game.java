package it.polito.roboCheckers;

import it.polito.Checkers.*;
import it.polito.Navigation.notCalibratedException;
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
		int test=CheckersConstants.NO_WIN;
		Player player = this.whitePlayer;
		do {
			for (boolean legalMove = false; !legalMove;) {
				legalMove = true;
				try {
					lastMove = player.makeMove(this.board);
					if (this.cancelled) { //TODO should use an exception
						test = CheckersConstants.CANCELLED;
						break;
					}
					board.makeMove(lastMove);
				} catch (cantMoveException e) {
					test = opponentWins(player.getPiece());
					System.out.println("Can't Move Exc");
					break;
				} catch (IllegalMoveException e) {
					legalMove = false;
					System.out.println("Illegal Move Exc");
				}
			}
			board.printBoard();
			Button.waitForPress();
			player = (player == blackPlayer) ? whitePlayer : blackPlayer;
		} while (test==CheckersConstants.NO_WIN);
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
