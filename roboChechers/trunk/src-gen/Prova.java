
import de.nordakademie.lejos.statemachine.*;
import lejos.nxt.*;
import lejos.navigation.*;
public class Prova {

	private TouchSensor touch1 = new TouchSensor(SensorPort.S2);;

	private boolean initialized = false;

	public IState s1 = new AbstractState() {

		public void doIt() throws InterruptedException {
			System.out.println("State = S1");
		}

		public String getName() {
			return "S1";
		}
	};

	public IState s2 = new AbstractState() {

		public void doIt() throws InterruptedException {
			System.out.println("State = S2");
		}

		public String getName() {
			return "S2";
		}
	};

	public ITransition q = new AbstractTransition(s2) {
		public boolean guard() {
			return Button.ENTER.isPressed() == true;
		}
		public String getName() {
			return "q";
		}
	};

	public ITransition newEvent = new AbstractTransition(s1) {
		public boolean guard() {
			return Button.ENTER.isPressed() == false;
		}
		public String getName() {
			return "NewEvent";
		}
	};

	public static void main(String[] args) {
		Prova prova = new Prova();
		prova.arbitrate();
	}

	public synchronized void arbitrate() {
		init();
		BTChangeListener bTChangeListener = new BTChangeListener();

		s1.setListener(bTChangeListener);

		s1.arbitrate(0);

	}

	public String getName() {
		return "Prova";
	}

	public boolean isRunning() {
		return initialized && s1.isRunning();
	}

	private void init() {
		if (!initialized) {

			s1.setTransitions(new ITransition[]{q});

			s2.setTransitions(new ITransition[]{newEvent});

			initialized = true;
		}
	}

}
