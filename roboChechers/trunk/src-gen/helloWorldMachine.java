
import de.nordakademie.lejos.statemachine.*;
import lejos.nxt.*;
import lejos.navigation.*;
public class helloWorldMachine {

	private boolean initialized = false;

	public IState welcome = new AbstractState() {

		public void doIt() throws InterruptedException {
			LCD.drawString("HelloWorld", 0, 0);
			LCD.refresh();
		}

		public String getName() {
			return "Welcome";
		}
	};

	public static void main(String[] args) {
		helloWorldMachine helloWorldMachine = new helloWorldMachine();
		helloWorldMachine.arbitrate();
	}

	public synchronized void arbitrate() {
		init();
		BTChangeListener bTChangeListener = new BTChangeListener();

		welcome.setListener(bTChangeListener);

		welcome.arbitrate(0);

	}

	public String getName() {
		return "helloWorldMachine";
	}

	public boolean isRunning() {
		return initialized && welcome.isRunning();
	}

	private void init() {
		if (!initialized) {

			initialized = true;
		}
	}

}
