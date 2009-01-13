import it.polito.Navigation.*;
import it.polito.Checkers.*;
import de.nordakademie.lejos.statemachine.*;
import lejos.nxt.*;
import lejos.nxt.addon.ColorSensor;
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

		public String getName() {
			return "Home";
		}
	};

	public IState think = new AbstractState() {

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

		public String getName() {
			return "RobotWins";
		}
	};

	public IState humanWins = new AbstractState() {

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

		public String getName() {
			return "ShowFrom";
		}
	};

	public IState showTo = new AbstractState() {

		public String getName() {
			return "ShowTo";
		}
	};

	public IState calculateMoves = new AbstractState() {

		public void doIt() throws InterruptedException {
			board.getPossibleMoves(piecehuman);
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

		calibration.setListener(bTChangeListener);

		calibration.arbitrate(0);

	}

	public String getName() {
		return "RoboStateChckers";
	}

	public boolean isRunning() {
		return initialized && calibration.isRunning();
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

			}, new AbstractTransition(humanWins) {

			}

			});

			guessMoveFrom.setImplicitTransitions(new ITransition[]{

			new AbstractTransition(sensorRead) {

			}, new AbstractTransition(robotWins) {

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

			}

			});

			calculateTo.setImplicitTransitions(new ITransition[]{

			new AbstractTransition(guessMoveTo) {

			}

			});

			initialized = true;
		}
	}

}
