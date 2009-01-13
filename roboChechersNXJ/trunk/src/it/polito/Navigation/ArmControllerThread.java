package it.polito.Navigation;

import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;

public class ArmControllerThread implements Runnable{
	
	private final int goDownRounds = -850;
	private final Integer UP = 1;
	private final Integer DOWN = 2;
	private final Integer NONE = 0;
	private Thread t;
	private static ArmControllerThread controller;
	private Integer action=NONE;
	private Integer direction=NONE;
	private Motor MC = null;
	private TouchSensor TS = null;

	
	public ArmControllerThread(Motor MC, TouchSensor TS) {
		this.MC = MC;
		this.TS = TS;
		this.t = new Thread(this);
	}
	
	public static ArmControllerThread getInstance(){
		if (controller == null)
			controller = new ArmControllerThread(Motor.C,new TouchSensor(SensorPort.S2));
		return controller;
	}
	
	public void run() {
		
		while (true){
			synchronized (action) {
				if (!TS.isPressed()) {
					
				}
			}
		}
		// up
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
		//down
		up();
		MC.rotate(goDownRounds);
	}

}
