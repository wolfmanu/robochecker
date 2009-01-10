
import de.nordakademie.lejos.statemachine.*;
import lejos.nxt.*;
import lejos.navigation.*;
public class RoboStateChckers {

	private boolean initialized = false;

	public IState calibration = new AbstractState() {

		public String getName() {
			return "Calibration";
		}
	};

	public IState home = new AbstractState() {

		public String getName() {
			return "Home";
		}
	};

	public IState moveTo = new AbstractState() {

		public String getName() {
			return "MoveTo";
		}
	};

	public IState think = new AbstractState() {

		public String getName() {
			return "Think";
		}
	};

	public IState guessMoveFrom = new AbstractState() {

		public String getName() {
			return "GuessMoveFrom";
		}
	};

	public IState guessMoveTo = new AbstractState() {

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

	public IState letturaSensore = new AbstractState() {

		public String getName() {
			return "LetturaSensore";
		}
	};

	public IState updateBoard = new AbstractState() {

		public String getName() {
			return "UpdateBoard";
		}
	};

	public static void main(String[] args) {
		RoboStateChckers roboStateChckers = new RoboStateChckers();
		roboStateChckers.arbitrate();
	}

	public synchronized void arbitrate() {
		init();
		BTChangeListener bTChangeListener = new BTChangeListener();

	}

	public String getName() {
		return "RoboStateChckers";
	}

	public boolean isRunning() {
		return initialized

		;
	}

	private void init() {
		if (!initialized) {

			moveTo.setImplicitTransitions(new ITransition[]{

			new AbstractTransition(letturaSensore) {

			}, new AbstractTransition(think) {

			}

			});

			think.setImplicitTransitions(new ITransition[]{

			new AbstractTransition(moveTo) {

			}, new AbstractTransition(moveTo) {

			}

			});

			guessMoveFrom.setImplicitTransitions(new ITransition[]{

			new AbstractTransition(moveTo) {

			}

			});

			guessMoveTo.setImplicitTransitions(new ITransition[]{

			new AbstractTransition(moveTo) {

			}

			});

			letturaSensore.setImplicitTransitions(new ITransition[]{

			new AbstractTransition(guessMoveFrom) {

			}, new AbstractTransition(guessMoveTo) {

			}, new AbstractTransition(updateBoard) {

			}

			});

			updateBoard.setImplicitTransitions(new ITransition[]{

			new AbstractTransition(think) {

			}

			});

			initialized = true;
		}
	}

}
