
import de.nordakademie.lejos.statemachine.*;
import lejos.nxt.*;
import lejos.navigation.*;
import it.polito.Checkers.*;
import it.polito.Navigation.*;
import it.polito.BluetoothComm.*;
import lejos.nxt.addon.*;
import it.polito.util.*;
public class RobotCheckers extends Statemachine {

	public RobotCheckers() {
		this(null);
	}
	public RobotCheckers(Statemachine parent) {
		super(parent);

		calibration.setImplicitTransitions(new ITransition[]{

		new AbstractTransition(home) {

		}

		});

		home.setTransitions(new ITransition[]{humanMoved});

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

		robotWins.setImplicitTransitions(new ITransition[]{

		new AbstractTransition(newName) {

		}

		});

		guessMoveFrom.setImplicitTransitions(new ITransition[]{

		new AbstractTransition(sensorRead) {
			public boolean guard() {
				return mosse == true;
			}

		}, new AbstractTransition(calculateMoves) {
			public boolean guard() {
				return mosse = false;
			}
			public int getDelay() {
				return 3000;
			}
		}

		});

		sensorRead.setImplicitTransitions(new ITransition[]{

		new AbstractTransition(guessMoveFrom) {
			public boolean guard() {
				return colore == CheckersConstants.BLACK && searchFrom == true;
			}

		}, new AbstractTransition(calculateTo) {
			public boolean guard() {
				return colore == CheckersConstants.EMPTY && searchFrom == true;
			}

		}, new AbstractTransition(guessMoveTo) {
			public boolean guard() {
				return colore == CheckersConstants.EMPTY && searchTo == true;
			}

		}, new AbstractTransition(updateBoard) {

		}

		});

		updateBoard.setImplicitTransitions(new ITransition[]{

		new AbstractTransition(think) {

		}

		});

		calculateTo.setImplicitTransitions(new ITransition[]{

		new AbstractTransition(guessMoveTo) {

		}

		});

		guessMoveTo.setImplicitTransitions(new ITransition[]{

		new AbstractTransition(sensorRead) {
			public boolean guard() {
				return mosse == true;
			}

		}, new AbstractTransition(calculateMoves) {
			public boolean guard() {
				return mosse == false;
			}
			public int getDelay() {
				return 3000;
			}
		}

		});

		think.setImplicitTransitions(new ITransition[]{

		new AbstractTransition(humanWins) {
			public boolean guard() {
				return humanWin == true;
			}

		}, new AbstractTransition(showFrom) {
			public boolean guard() {
				return humanWin == false;
			}

		}

		});

		humanWins.setImplicitTransitions(new ITransition[]{

		new AbstractTransition(newName) {

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

		setStartTransitions(new ITransition[]{new ITransition() {
			final public boolean guard() {
				return true;
			}
			final public IState getTarget() {
				return calibration;
			}
			final public int getDelay() {
				return 0;
			}
		}

		});
	}

	/**
	 * Variables of the statemachine
	 *
	 **/

	private static CheckersNavigator navigator = SimpleNavigator.getInstance();

	private static ColorSensor CS = new ColorSensor(SensorPort.S1);

	private static Board board = new Board();

	private static int pieceRobot = CheckersConstants.WHITE;

	private static int piecehuman = CheckersConstants.BLACK;

	private static int colore = CS.getColorNumber();

	private static boolean searchFrom = true;

	private static boolean searchTo = false;

	private static Square from = null;

	private static Square[] to = null;

	private ArmController arm = ArmController.getInstance();

	private static boolean humanWin = false;

	private static boolean robotWin = false;

	private final int depth = 3;

	private static Move nextMove = null;

	private int[] result = new int[4];;

	private static HumanInput HI = ButtonInput.getInstance();

	private static boolean mosse = true;

	/**
	 * Internal states of the statemachine including exit, excluding initial states
	 *
	 **/

	public IState calibration = new AbstractState(this) {

		public void doMethod() throws InterruptedException {
			navigator.calibrate();
		}

		public void exitMethod() {
			arm.up();
		}

		public void entryMethod() {
			HI.init();
			arm.down();
		}

		final public String getName() {
			return "Calibration";
		}
	};

	public IState home = new AbstractState(this) {

		public void doMethod() throws InterruptedException {
			try {
				navigator.goHome();
			} catch (Exception e) {
			}
			while (true) {
				if (Thread.interrupted())
					throw new InterruptedException();
				Thread.yield();
			}
		}

		public void entryMethod() {
			arm.up();
		}

		final public String getName() {
			return "Home";
		}
	};

	public IState calculateMoves = new AbstractState(this) {

		public void doMethod() throws InterruptedException {
			try {
				board.getPossibleMoves(piecehuman);
			} catch (cantMoveException e) {
				robotWin = true;
			}
		}

		public void exitMethod() {
			mosse = true;
		}

		final public String getName() {
			return "CalculateMoves";
		}
	};

	public IState robotWins = new AbstractState(this) {

		final public String getName() {
			return "RobotWins";
		}
	};

	public IState guessMoveFrom = new AbstractState(this) {

		public void doMethod() throws InterruptedException {
			try {
				navigator.goTo(from);
			} catch (Exception e) {
			}
		}

		public void entryMethod() {
			try {
				from = board.getPossibleMoveFrom();
			} catch (cantMoveException e) {
				mosse = false;
			}
		}

		final public String getName() {
			return "GuessMoveFrom";
		}
	};

	public IState sensorRead = new AbstractState(this) {

		public void doMethod() throws InterruptedException {
			try {
				colore = CS.getColorNumber();
			} catch (Exception e) {
				System.out.println("eccezione " + e.getMessage());
			}
		}

		public void exitMethod() {
			arm.up();
		}

		public void entryMethod() {
			arm.down();
		}

		final public String getName() {
			return "SensorRead";
		}
	};

	public IState updateBoard = new AbstractState(this) {

		public void doMethod() throws InterruptedException {
			board.makeMove(new Move(from, to));
		}

		final public String getName() {
			return "UpdateBoard";
		}
	};

	public IState calculateTo = new AbstractState(this) {

		public void entryMethod() {
			searchFrom = false;
			searchTo = true;
		}

		final public String getName() {
			return "CalculateTo";
		}
	};

	public IState guessMoveTo = new AbstractState(this) {

		public void doMethod() throws InterruptedException {
			try {
				navigator.goTo(to[to.length - 1]);
			} catch (Exception e) {
			}
		}

		public void entryMethod() {
			try {
				to = board.getPossibleMoveTo();
			} catch (cantMoveException e) {
				mosse = false;
			}
		}

		final public String getName() {
			return "GuessMoveTo";
		}
	};

	public IState think = new AbstractState(this) {

		public void doMethod() throws InterruptedException {
			Engine.MiniMax(board.getArrayBoard(), 0, depth, result, pieceRobot,
					new int[1]);
			try {
				nextMove = Move.fromArray(result);
			} catch (cantMoveException e) {
				humanWin = true;
			}
		}

		final public String getName() {
			return "Think";
		}
	};

	public IState humanWins = new AbstractState(this) {

		final public String getName() {
			return "HumanWins";
		}
	};

	public IState showFrom = new AbstractState(this) {

		public void doMethod() throws InterruptedException {
			arm.down();
		}

		public void exitMethod() {
			try {
				navigator.goTo(from);
			} catch (Exception e) {
			}
		}

		public void entryMethod() {
			arm.up();
		}

		final public String getName() {
			return "ShowFrom";
		}
	};

	public IState showTo = new AbstractState(this) {

		public void doMethod() throws InterruptedException {
			try {
				navigator.goTo(to[to.length - 1]);
			} catch (Exception e) {
			}
		}

		public void exitMethod() {
			arm.down();
		}

		public void entryMethod() {
			arm.up();
		}

		final public String getName() {
			return "ShowTo";
		}
	};

	public IState newName = new EndState(this);

	/**
	 * Transtions of the statemachine including transitions from start state
	 *
	 **/

	public ITransition humanMoved = new AbstractTransition(calculateMoves) {
		final public boolean guard() {
			return HI.waitForMove(false);
		}
		final public static String NAME = "HumanMoved";
	};

	/**
	 * set he Listeners thru the complete statemachine hierarchy
	 *
	 **/
	public void setListener(StateChangeListener[] listeners) {
		super.setListener(listeners);

	}

	/**
	 * main method to make the statemachine startable from outside
	 *
	 **/
	public static void main(String[] args) {
		RobotCheckers robotCheckers = new RobotCheckers();
		StateChangeListener[] allListeners = new StateChangeListener[]{
				new LCDChangeListener(), new BTChangeListener()};
		robotCheckers.setListener(allListeners);
		try {
			robotCheckers.doMethod();
		} catch (InterruptedException i) {
		}
		for (StateChangeListener l : allListeners) {
			l.stop();
		}
	}

	final public String getName() {
		return "RobotCheckers";
	}

}
