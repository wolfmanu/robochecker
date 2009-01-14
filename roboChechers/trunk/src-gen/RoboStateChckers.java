
import de.nordakademie.lejos.statemachine.*;
import lejos.nxt.*;
import lejos.navigation.*;
public class RoboStateChckers {

	private static CheckersNavigator navigator = SimpleNavigator.getInstance();

	private static ColorSensor CS = new ColorSensor(SensorPort.S1);

	private static Board board = new Board();;

	private static int pieceRobot = CheckersConstants.WHITE;;

	private static int piecehuman = CheckersConstants.BLACK;;

	private static int colore = CS.getColorNumber();;

	private static boolean searchFrom = true;;

	private static boolean searchTo = false;;

	private static Square from = null;;

	private static Square[] to = null;;

	private ArmController arm = ArmController.getInstance();;

	private static boolean humanWin = false;;

	private static boolean robotWin = false;;

	private final int depth = 3;;

	private boolean initialized = false;

	public IState calibration = new AbstractState() {

		public void doIt() throws InterruptedException {
			navigator.calibrate();
		}

		public void exit() {
			arm.up();
		}

		public void entry() {
			arm.down();
		}

		public String getName() {
			return "Calibration";
		}
	};

	public IState home = new AbstractState() {

		public void doIt() throws InterruptedException {
			try {
				navigator.goHome();
			} catch (Exception e) {
			}
		}

		public void entry() {
			arm.up();
		}

		public String getName() {
			return "Home";
		}
	};

	public IState think = new AbstractState() {

		private int[] result = new int[4];;

		private Move nextMove = null;

		public void doIt() throws InterruptedException {
			Engine.MiniMax(board.getArrayBoard(), 0, depth, result, pieceRobot,
					new int[1]);
			try {
				nextMove = Move.fromArray(result);
			} catch (cantMoveException e) {
				humanWin = true;
			}
		}

		public void exit() {
			from = nextMove.getFrom();
			to = nextMove.getTo();
		}

		public String getName() {
			return "Think";
		}
	};

	public IState guessMoveFrom = new AbstractState() {

		public void doIt() throws InterruptedException {
			try {
				navigator.goTo(from);
			} catch (Exception e) {
			}
		}

		public void entry() {
			from = board.getPossibleMoveFrom();
		}

		public String getName() {
			return "GuessMoveFrom";
		}
	};

	public IState guessMoveTo = new AbstractState() {

		public void doIt() throws InterruptedException {
			try {
				navigator.goTo(to[to.length - 1]);
			} catch (Exception e) {
			}
		}

		public void entry() {
			to = board.getPossibleMoveTo();
		}

		public String getName() {
			return "GuessMoveTo";
		}
	};

	public IState robotWins = new AbstractState() {

		public void doIt() throws InterruptedException {
			System.out.println("White wins");
		}

		public String getName() {
			return "RobotWins";
		}
	};

	public IState humanWins = new AbstractState() {

		public void doIt() throws InterruptedException {
			System.out.println("Black wins");
		}

		public String getName() {
			return "HumanWins";
		}
	};

	public IState sensorRead = new AbstractState() {

		public void doIt() throws InterruptedException {
			colore = CS.getColorNumber();
		}

		public void exit() {
			arm.up();
		}

		public void entry() {
			arm.down();
		}

		public String getName() {
			return "SensorRead";
		}
	};

	public IState updateBoard = new AbstractState() {

		public void doIt() throws InterruptedException {
			board.makeMove(new Move(from, to));
		}

		public String getName() {
			return "UpdateBoard";
		}
	};

	public IState showFrom = new AbstractState() {

		public void doIt() throws InterruptedException {
			arm.down();
		}

		public void entry() {
			try {
				navigator.goTo(from);
			} catch (Exception e) {
			}
		}

		public String getName() {
			return "ShowFrom";
		}
	};

	public IState showTo = new AbstractState() {

		public void doIt() throws InterruptedException {
			arm.down();
		}

		public void entry() {
			arm.up();
			try {
				navigator.goTo(to[to.length - 1]);
			} catch (Exception e) {
			}
		}

		public String getName() {
			return "ShowTo";
		}
	};

	public IState calculateMoves = new AbstractState() {

		public void doIt() throws InterruptedException {
			try {
				board.getPossibleMoves(piecehuman);
			} catch (cantMoveException e) {
				robotWin = true;
			}
		}

		public void exit() {
			searchFrom = true;
		}

		public String getName() {
			return "CalculateMoves";
		}
	};

	public IState calculateTo = new AbstractState() {

		public void entry() {
			searchFrom = false;
			searchTo = true;
		}

		public String getName() {
			return "CalculateTo";
		}
	};

	public IState calibrateColor = new AbstractState() {

		public void doIt() throws InterruptedException {
			Color.calibrate();
		}

		public String getName() {
			return "CalibrateColor";
		}
	};

	public ITransition humanMoved = new AbstractTransition(calculateMoves) {
		public boolean guard() {
			return false;//Insert java condition here;
		}
		public String getName() {
			return "HumanMoved";
		}
	};

	public static void main(String[] args) {
		RoboStateChckers roboStateChckers = new RoboStateChckers();
		roboStateChckers.arbitrate();
	}

	public synchronized void arbitrate() {
		init();
		BTChangeListener bTChangeListener = new BTChangeListener();

		calibrateColor.setListener(bTChangeListener);

		calibrateColor.arbitrate(0);

	}

	public String getName() {
		return "RoboStateChckers";
	}

	public boolean isRunning() {
		return initialized && calibrateColor.isRunning();
	}

	private void init() {
		if (!initialized) {

			calibration.setImplicitTransitions(new ITransition[]{

			new AbstractTransition(home) {

			}

			});

			home.setTransitions(new ITransition[]{humanMoved});

			think.setImplicitTransitions(new ITransition[]{

			new AbstractTransition(showFrom) {
				public boolean guard() {
					return humanWin == false;
				}

			}, new AbstractTransition(humanWins) {
				public boolean guard() {
					return humanWin == true;
				}

			}

			});

			guessMoveFrom.setImplicitTransitions(new ITransition[]{

			new AbstractTransition(sensorRead) {

			}

			});

			guessMoveTo.setImplicitTransitions(new ITransition[]{

			new AbstractTransition(sensorRead) {

			}

			});

			sensorRead.setImplicitTransitions(new ITransition[]{

			new AbstractTransition(guessMoveFrom) {
				public boolean guard() {
					return colore == CheckersConstants.BLACK
							&& searchFrom == true;
				}

			}, new AbstractTransition(calculateTo) {
				public boolean guard() {
					return colore == CheckersConstants.WHITE
							&& searchFrom == true;
				}

			}, new AbstractTransition(updateBoard) {
				public boolean guard() {
					return colore == CheckersConstants.BLACK
							&& searchTo == true;
				}

			}, new AbstractTransition(guessMoveTo) {
				public boolean guard() {
					return colore == CheckersConstants.WHITE
							&& searchTo == true;
				}

			}

			});

			updateBoard.setImplicitTransitions(new ITransition[]{

			new AbstractTransition(think) {

			}

			});

			showFrom.setImplicitTransitions(new ITransition[]{

			new AbstractTransition(showTo) {
				public int getDelay() {
					return 3000;
				}
			}

			});

			showTo.setImplicitTransitions(new ITransition[]{

			new AbstractTransition(home) {
				public int getDelay() {
					return 3000;
				}
			}

			});

			calculateMoves.setImplicitTransitions(new ITransition[]{

			new AbstractTransition(guessMoveFrom) {
				public boolean guard() {
					return robotWin == false;
				}

			}, new AbstractTransition(robotWins) {
				public boolean guard() {
					return robotWin == true;
				}

			}

			});

			calculateTo.setImplicitTransitions(new ITransition[]{

			new AbstractTransition(guessMoveTo) {

			}

			});

			calibrateColor.setImplicitTransitions(new ITransition[]{

			new AbstractTransition(calibration) {

			}

			});

			initialized = true;
		}
	}

}
