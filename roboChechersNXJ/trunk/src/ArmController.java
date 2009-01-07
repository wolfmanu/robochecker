import lejos.nxt.Motor;
import lejos.nxt.TouchSensor;

public class ArmController {

	private final int goDownRounds = -850;
	private Motor MC = null;
	private TouchSensor TS = null;
	private static ArmController controller = null;
	
	public static ArmController getInstance(Motor MC, TouchSensor TS){
		if (controller == null)
			controller = new ArmController(MC,TS);
		return controller;
	}
	
	private ArmController (Motor MC, TouchSensor TS) {
		this.MC = MC;
		this.TS = TS;
	}
	
	public void up() {
		if (!TS.isPressed()) {
			MC.forward();
			
			while (!TS.isPressed()) {
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {	}
			}
			MC.stop();
			MC.resetTachoCount();
		}
	}
	public void down() {
		up();
		MC.rotate(goDownRounds);
	}
	
}
