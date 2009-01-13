package it.polito.Navigation;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;

public class ArmController extends Thread {

	private final int goDownRounds = -850;
	private Motor MC = null;
	private TouchSensor TS = null;
	private static ArmController controller = null;
	
	public static ArmController getInstance(){
		if (controller == null)
			controller = new ArmController(Motor.C,new TouchSensor(SensorPort.S2));
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
