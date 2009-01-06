package it.polito.Checkers;
import it.polito.util.Vector;

class Engine {

	final static int INFINITY = Integer.MAX_VALUE;
	final static int CHECKER = 100; // one checker worth 100
	final static int POS = 1; // one position along the -j worth 1
	final static int KING = 200; // a king's worth
	final static int EDGE = 10; // effect of king being on the edge
	final static int RANDOM_WEIGHT = 10; // weight factor
	final static int LEGALMOVE = 1;
	final static int ILLEGALMOVE = 2;
	final static int INCOMPLETEMOVE = 3;

	static boolean noMovesLeft(int[][] position, int toMove) {
		for (int i = 0; i < 8; i++)
			for (int j = 0; j < 8; j++)
				if ((float) (i + j) / 2 != (i + j) / 2) {
					if (toMove == CheckersConstants.WHITE
							&& (position[i][j] == CheckersConstants.WHITE || position[i][j] == CheckersConstants.WKING)) {
						if (canWalk(position, i, j))
							return false;
						else if (canCapture(position, i, j))
							return false;
					} else if (toMove == CheckersConstants.BLACK
							&& (position[i][j] == CheckersConstants.BLACK || position[i][j] == CheckersConstants.BKING)) {
						if (canWalk(position, i, j))
							return false;
						else if (canCapture(position, i, j))
							return false;
					}
				} // if and for
		return true;
	}

	// ApplyMove checks if the move entered is legal, illegal,
	// or incomplete.

	// If IsMoveLegal returns INCOMPLETEMOVE, this means a capture has just been
	// made.
	// Call canCapture() to see if a further capture is possible.
	static int ApplyMove(int[][] position, int start_i, int start_j, int end_i,
			int end_j) {
		int result = IsMoveLegal(position, start_i, start_j, end_i, end_j,
				color(position[start_i][start_j]));
		if (result != ILLEGALMOVE) {
			if (Math.abs(end_i - start_i) == 1) {
				position[end_i][end_j] = position[start_i][start_j];
				position[start_i][start_j] = CheckersConstants.EMPTY;
			} else // capture
			{
				position[(start_i + end_i) / 2][(start_j + end_j) / 2] = CheckersConstants.EMPTY;
				position[end_i][end_j] = position[start_i][start_j];
				position[start_i][start_j] = CheckersConstants.EMPTY;
			}

			if (result == INCOMPLETEMOVE) {
				// if there are no further captures
				if (!(canCapture(position, end_i, end_j)))
					result = LEGALMOVE;
			}

			// check for new king
			if (position[end_i][end_j] == CheckersConstants.WHITE && end_j == 7)
				position[end_i][end_j] = CheckersConstants.WKING;
			else if (position[end_i][end_j] == CheckersConstants.BLACK && end_j == 0)
				position[end_i][end_j] = CheckersConstants.BKING;

		}

		return result;
	}

	// IsMoveLegal checks if the move entered is legal.
	// Returns ILLEGALMOVE or LEGALMOVE;
	// have to check with canCapture(int[][],int,int) to see
	// if there is another capture possible after the first capture
	// Returns INCOMPLETEMOVE if a capture has taken place.
	// Note: it does not check if a 2nd capture is possible!
	static int IsMoveLegal(int[][] position, int start_i, int start_j,
			int end_i, int end_j, int turn) {
		if (!(inRange(start_i, start_j) && inRange(end_i, end_j)))
			return ILLEGALMOVE;
		if (position[end_i][end_j] != CheckersConstants.EMPTY)
			return ILLEGALMOVE;

		int piece = position[start_i][start_j];
		if (Math.abs(start_i - end_i) == 1) {
			// first see if any captures are possible
			switch (piece) {
			case CheckersConstants.WHITE:
			case CheckersConstants.WKING:
				for (int i = 0; i < 8; i++)
					for (int j = 0; j < 8; j++) {
						if ((position[i][j] == CheckersConstants.WHITE || position[i][j] == CheckersConstants.WKING)
								&& canCapture(position, i, j))
							return ILLEGALMOVE;
					}
				break;
			case CheckersConstants.BLACK:
			case CheckersConstants.BKING:
				for (int i = 0; i < 8; i++)
					for (int j = 0; j < 8; j++) {
						if ((position[i][j] == CheckersConstants.BLACK || position[i][j] == CheckersConstants.BKING)
								&& canCapture(position, i, j))
							return ILLEGALMOVE;
					}
				break;
			} // switch

			switch (piece) {
			case CheckersConstants.WHITE:
				if (end_j - start_j == 1)
					return LEGALMOVE;
				break;
			case CheckersConstants.BLACK:
				if (end_j - start_j == -1)
					return LEGALMOVE;
				break;
			case CheckersConstants.WKING:
			case CheckersConstants.BKING:
				if (Math.abs(end_j - start_j) == 1)
					return LEGALMOVE;
				break;
			} // switch (piece)

			return ILLEGALMOVE;

		} // if ( (Math.abs(start_i - end_i) == 1 )

		else if (Math.abs(start_i - end_i) == 2) {
			int cap_i = (start_i + end_i) / 2;
			int cap_j = (start_j + end_j) / 2;
			int cap_piece = position[cap_i][cap_j];

			if (turn == CheckersConstants.WHITE) {
				if (!(cap_piece == CheckersConstants.BLACK || cap_piece == CheckersConstants.BKING))
					return ILLEGALMOVE;
			} else if (!(cap_piece == CheckersConstants.WHITE || cap_piece == CheckersConstants.WKING))
				return ILLEGALMOVE;

			switch (piece) {
			case CheckersConstants.WHITE:
				if (end_j - start_j != 2)
					return ILLEGALMOVE;
				break;
			case CheckersConstants.BLACK:
				if (end_j - start_j != -2)
					return ILLEGALMOVE;
				break;
			case CheckersConstants.WKING:
			case CheckersConstants.BKING:
				if (Math.abs(end_j - start_j) != 2)
					return ILLEGALMOVE;
			}

			return INCOMPLETEMOVE;
		}
		return ILLEGALMOVE;
	}

	static int IsWalkLegal(int[][] position, int start_i, int start_j,
			int end_i, int end_j, int turn) {
		if (!(inRange(start_i, start_j) && inRange(end_i, end_j)))
			return ILLEGALMOVE;
		if (position[end_i][end_j] != CheckersConstants.EMPTY)
			return ILLEGALMOVE;

		int piece = position[start_i][start_j];
		if (Math.abs(start_i - end_i) == 1) {
			switch (piece) {
			case CheckersConstants.WHITE:
				if (end_j - start_j == 1)
					return LEGALMOVE;
				break;
			case CheckersConstants.BLACK:
				if (end_j - start_j == -1)
					return LEGALMOVE;
				break;
			case CheckersConstants.WKING:
			case CheckersConstants.BKING:
				if (Math.abs(end_j - start_j) == 1)
					return LEGALMOVE;
				break;
			} // switch (piece)

			return ILLEGALMOVE;

		} // if ( (Math.abs(start_i - end_i) == 1 )

		return ILLEGALMOVE;
	}

	static boolean canCapture(int[][] position, int toMove) {
		for (int i = 0; i < 8; i++)
			for (int j = 0; j < 8; j++) {
				if (color(position[i][j]) == toMove
						&& canCapture(position, i, j))
					return true;
			}
		return false;
	}

	// examines a board position to see if the piece indicated at (x,y)
	// can make a(nother) capture
	static boolean canCapture(int[][] position, int i, int j) {
		switch (position[i][j]) {
		case CheckersConstants.WHITE:
			if (i + 2 < 8 && j + 2 < 8)
				if ((position[i + 1][j + 1] == CheckersConstants.BLACK || position[i + 1][j + 1] == CheckersConstants.BKING)
						&& (position[i + 2][j + 2] == CheckersConstants.EMPTY))
					return true;
			if (i - 2 > -1 && j + 2 < 8)
				if ((position[i - 1][j + 1] == CheckersConstants.BLACK || position[i - 1][j + 1] == CheckersConstants.BKING)
						&& position[i - 2][j + 2] == CheckersConstants.EMPTY)
					return true;
			break;
		case CheckersConstants.BLACK:
			if (i + 2 < 8 && j - 2 > -1)
				if ((position[i + 1][j - 1] == CheckersConstants.WHITE || position[i + 1][j - 1] == CheckersConstants.WKING)
						&& position[i + 2][j - 2] == CheckersConstants.EMPTY)
					return true;
			if (i - 2 > -1 && j - 2 > -1)
				if ((position[i - 1][j - 1] == CheckersConstants.WHITE || position[i - 1][j - 1] == CheckersConstants.WKING)
						&& position[i - 2][j - 2] == CheckersConstants.EMPTY)
					return true;
			break;
		case CheckersConstants.WKING:
			if (i + 2 < 8) {
				if (j + 2 < 8)
					if ((position[i + 1][j + 1] == CheckersConstants.BLACK || position[i + 1][j + 1] == CheckersConstants.BKING)
							&& position[i + 2][j + 2] == CheckersConstants.EMPTY)
						return true;
				if (j - 2 > -1)
					if ((position[i + 1][j - 1] == CheckersConstants.BLACK || position[i + 1][j - 1] == CheckersConstants.BKING)
							&& position[i + 2][j - 2] == CheckersConstants.EMPTY)
						return true;
			}
			if (i - 2 > -1) {
				if (j + 2 < 8)
					if ((position[i - 1][j + 1] == CheckersConstants.BLACK || position[i - 1][j + 1] == CheckersConstants.BKING)
							&& position[i - 2][j + 2] == CheckersConstants.EMPTY)
						return true;
				if (j - 2 > -1)
					if ((position[i - 1][j - 1] == CheckersConstants.BLACK || position[i - 1][j - 1] == CheckersConstants.BKING)
							&& position[i - 2][j - 2] == CheckersConstants.EMPTY)
						return true;
			}
			break;
		case CheckersConstants.BKING:
			if (i + 2 < 8) {
				if (j + 2 < 8)
					if ((position[i + 1][j + 1] == CheckersConstants.WHITE || position[i + 1][j + 1] == CheckersConstants.WKING)
							&& position[i + 2][j + 2] == CheckersConstants.EMPTY)
						return true;
				if (j - 2 > -1)
					if ((position[i + 1][j - 1] == CheckersConstants.WHITE || position[i + 1][j - 1] == CheckersConstants.WKING)
							&& position[i + 2][j - 2] == CheckersConstants.EMPTY)
						return true;
			}
			if (i - 2 > -1) {
				if (j + 2 < 8)
					if ((position[i - 1][j + 1] == CheckersConstants.WHITE || position[i - 1][j + 1] == CheckersConstants.WKING)
							&& position[i - 2][j + 2] == CheckersConstants.EMPTY)
						return true;
				if (j - 2 > -1)
					if ((position[i - 1][j - 1] == CheckersConstants.WHITE || position[i - 1][j - 1] == CheckersConstants.WKING)
							&& position[i - 2][j - 2] == CheckersConstants.EMPTY)
						return true;
			}
			break;
		} // switch
		return false;
	} // canCapture()

	// canWalk() returns true if the piece on (i,j) can make a
	// legal non-capturing move
	static boolean canWalk(int[][] position, int i, int j) {
		switch (position[i][j]) {
		case CheckersConstants.WHITE:
			if (isEmpty(position, i + 1, j + 1)
					|| isEmpty(position, i - 1, j + 1))
				return true;
			break;
		case CheckersConstants.BLACK:
			if (isEmpty(position, i + 1, j - 1)
					|| isEmpty(position, i - 1, j - 1))
				return true;
			break;
		case CheckersConstants.WKING:
		case CheckersConstants.BKING:
			if (isEmpty(position, i + 1, j + 1)
					|| isEmpty(position, i + 1, j - 1)
					|| isEmpty(position, i - 1, j + 1)
					|| isEmpty(position, i - 1, j - 1))
				return true;
		} // switch
		return false;
	} // canWalk()

	private static boolean isEmpty(int[][] position, int i, int j) {
		if (i > -1 && i < 8 && j > -1 && j < 8)
			if (position[i][j] == CheckersConstants.EMPTY)
				return true;
		return false;
	} // isEmpty()
/*
	private static boolean isWhite(int[][] position, int i, int j) {
		if (i > -1 && i < 8 && j > -1 && j < 8)
			if (color(position[i][j]) == CheckersConstants.WHITE)
				return true;
		return false;
	} // isWhite()

	private static boolean isBlack(int[][] position, int i, int j) {
		if (i > -1 && i < 8 && j > -1 && j < 8)
			if (color(position[i][j]) == CheckersConstants.BLACK)
				return true;
		return false;
	} // isBlack()
*/
	// returns the color of a piece
	static int color(int piece) {
		switch (piece) {
		case CheckersConstants.WHITE:
		case CheckersConstants.WKING:
			return CheckersConstants.WHITE;
		case CheckersConstants.BLACK:
		case CheckersConstants.BKING:
			return CheckersConstants.BLACK;
		}
		return CheckersConstants.EMPTY;
	}

	// checkers that i and j are between 0 and 7 inclusive
	private static boolean inRange(int i, int j) {
		return (i > -1 && i < 8 && j > -1 && j < 8);
	}

	// given a board, generates all the possible moves depending on whose turn
	static Vector<int[]> generate_moves(int[][] board, int turn) {
		Vector<int[]> moves_list = new Vector<int[]>();
		int move;

		for (int i = 7; i >= 0; i--)
			for (int j = 0; j < 8; j++)
				if (turn == color(board[i][j])) {
					if (canCapture(board, turn)) {
						for (int k = -2; k <= 2; k += 4)
							for (int l = -2; l <= 2; l += 4) {
								move = IsMoveLegal(board, i, j, i + k, j + l,
										turn);
								if (move == INCOMPLETEMOVE) {
									int[] int_array = new int[4];
									int_array[0] = i;
									int_array[1] = j;
									int_array[2] = i + k;
									int_array[3] = j + l;
									int[][] temp_board = Engine
											.copy_board(board);
									move = ApplyMove(temp_board, i, j, i
											+ k, j + l);
									if (move == INCOMPLETEMOVE)/*
																 * (canCapture(temp_board
																 * ,i+k,j+l))
																 */
									{
										forceCaptures(temp_board, turn,
												int_array, moves_list, 10);
									} else {
										moves_list.addElement(int_array);
									}

								} // if (move)
							} // if (inRange)
					} else {
						for (int k = -1; k <= 2; k += 2)
							for (int l = -1; l <= 2; l += 2) {
								if (inRange(i + k, j + l)) {
									move = IsWalkLegal(board, i, j, i + k, j
											+ l, turn);
									if (move == LEGALMOVE) {
										int[] int_array = new int[4];
										int_array[0] = i;
										int_array[1] = j;
										int_array[2] = i + k;
										int_array[3] = j + l;
										// a walk has taken place
										// add the new array to the Vector
										// moves_list
										moves_list.addElement(int_array);
									} // if (move)
								} // if (inRange)
							} // end for k,l
					}
				}// end if
		return moves_list;

	}// generate_moves

	// "apply move" in the Minimax. simply moves the board give moves
	static void move_board(int[][] board, int[] move) {
		int startx = move[0];
		int starty = move[1];
		int endx = move[2];
		int endy = move[3];
		while (endx > 0 || endy > 0) {
			ApplyMove(board, startx, starty, endx % 10, endy % 10);
			startx = endx % 10;
			starty = endy % 10;
			endx /= 10;
			endy /= 10;
		}

	}

	// for an initial capture represented by move, sees if there are more
	// captures
	// until there is none. If there are multiple capture configurations,
	// add all of them to moves_list
	private static void forceCaptures(int[][] board, int turn, int[] move, Vector<int []> moves_list, int inc) {
		int newx = move[2], newy = move[3];

		while (newx > 7 || newy > 7) {
			newx /= 10;
			newy /= 10;
		}// end while
		for (int i = -2; i <= 2; i += 4)
			for (int j = -2; j <= 2; j += 4)
				if (inRange(newx + i, newy + j)) {
					int[][] tempPosition = Engine.copy_board(board);
					int moveResult = ApplyMove(tempPosition, newx, newy, newx
							+ i, newy + j);
					if (moveResult == LEGALMOVE) {
						int[] new_move = new int[4];
						new_move[0] = move[0];
						new_move[1] = move[1];
						new_move[2] = move[2] + (newx + i) * inc;
						new_move[3] = move[3] + (newy + j) * inc;
						moves_list.addElement(new_move);
					} // if (moveResult == LEGALMOVE)
					else if (moveResult == INCOMPLETEMOVE) {
						int[] new_move = new int[4];
						new_move[0] = move[0];
						new_move[1] = move[1];
						new_move[2] = move[2] + (newx + i) * inc;
						new_move[3] = move[3] + (newy + j) * inc;

						forceCaptures(tempPosition, turn, new_move, moves_list,
								inc * 10);
					}
				} // end if for

	}// forceCaptures

	
	public static int Evaluation(int[][] position) {

		int score = 0;

		for (int i = 0; i < 8; i++)
			for (int j = 0; j < 8; j++) {
				if (position[i][j] == CheckersConstants.WHITE) {
					score -= CHECKER;
					score -= POS * j * j;
				}

				else if (position[i][j] == CheckersConstants.WKING) {
					score -= KING;
					if (i == 0 || i == 7)
						score += EDGE;
					if (j == 0 || j == 7)
						score += EDGE;
				}

				else if (position[i][j] == CheckersConstants.BKING) {
					score += KING;
					if (i == 0 || i == 7)
						score -= EDGE;
					if (j == 0 || j == 7)
						score -= EDGE;
				}

				else if (position[i][j] == CheckersConstants.BLACK) {
					score += CHECKER;
					score += POS * (7 - j) * (7 - j);
				}
			}// end for
		score += (int) (Math.random() * RANDOM_WEIGHT);
		return score;
	}// end Evaluation

	static int opponent(int turn) {
		return turn == CheckersConstants.BLACK ? CheckersConstants.WHITE : CheckersConstants.BLACK;
	}

	static int which_turn(int turn) {
		return color(turn) == CheckersConstants.BLACK ? -INFINITY : INFINITY;
	}

	public static int MiniMax(int[][] board, int depth, int max_depth,
			int[] the_move, int toMove, int[] counter) {
		return MiniMax(board, depth, max_depth, the_move, toMove, counter,
				INFINITY, -INFINITY);
	}

	static int MiniMax(int[][] board, int depth, int max_depth, int[] the_move,
			int turn, int[] counter, int white_best, int black_best) {
		int the_score = 0;
		int[][] new_board = new int[8][8];
		int best_score;
		int[] best_move = new int[4];

		Vector<int[]> moves_list = new Vector<int[]>(); // vector of 4x1 arrays
		// assumes that depth is never equal to max_depth to begin with since
		// chosen_move is not set here
		if (depth == max_depth) {
			best_score = Evaluation(board);
			counter[0]++;
		} else {
			moves_list = generate_moves(board, turn);
			best_score = which_turn(turn);
			switch (moves_list.size()) {
			case 0:
				counter[0]++;
				return best_score;
			case 1:
				if (depth == 0) {
					// forced move: immediately return control
					best_move = (int[]) moves_list.elementAt(0);
					for (int k = 0; k < 4; k++)
						the_move[k] = best_move[k];
					return 0;
				} else {
					// extend search since there is a forcing move
					max_depth += 1;
				}
			}

			for (int i = 0; i < moves_list.size(); i++) {
				new_board = copy_board(board); // board need not be touched
				move_board(new_board, (int[]) moves_list.elementAt(i)); // returns
																				// new_board
				int temp[] = new int[4];
				the_score = MiniMax(new_board, depth + 1, max_depth, temp,
						opponent(turn), counter, white_best, black_best);

				if (turn == CheckersConstants.BLACK && the_score > best_score) {
					best_move = (int[]) moves_list.elementAt(i);
					best_score = the_score;
					if (best_score > black_best) {
						if (best_score >= white_best)
							break; /* alpha_beta cutoff */
						else
							black_best = best_score; // the_score
					}
				}

				else if (turn == CheckersConstants.WHITE && the_score < best_score) {
					best_move = (int[]) moves_list.elementAt(i);
					best_score = the_score;
					if (best_score < white_best) {
						if (best_score <= black_best)
							break; /* alpha_beta cutoff */
						else
							white_best = best_score; // the_score
					}
				}

			} // end for

		}// end else
		for (int k = 0; k < 4; k++)
			the_move[k] = best_move[k];
		return best_score;
	} // end minimax

	static int[][] copy_board(int[][] board) {
		int[][] copy = new int[8][8];

		for (int i = 0; i < 8; i++)
			System.arraycopy(board[i], 0, copy[i], 0, 8);
		return copy;
	}// end copy_board

	static boolean better(int the_score, int best, int turn) {
		if (turn == CheckersConstants.BLACK)
			return the_score > best;
		return the_score < best;
	}// end better
}// end class engine

