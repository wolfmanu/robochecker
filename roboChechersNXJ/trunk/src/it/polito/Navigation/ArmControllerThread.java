package it.polito.Navigation;

import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;

public class ArmControllerThread {
	
	private final int goDownRounds = -550;
	private Motor MC = null;
	private TouchSensor TS = null;
	private static ArmControllerThread controller = null;
	private final ArmRegulator armRegulator = new ArmRegulator();
	private static final int UP = 0, DOWN = 1, GOUP = 2, GODOWN = 3;
	private int state = DOWN;
	
	public static ArmControllerThread getInstance(){
		if (controller == null)
			controller = new ArmControllerThread(Motor.C,new TouchSensor(SensorPort.S2));
		return controller;
	}
	
	private ArmControllerThread (Motor MC, TouchSensor TS) {
		this.MC = MC;
		this.TS = TS;
		this.armRegulator.setDaemon(true);
		this.armRegulator.start();
	}
	
	public void calibrate() {
		// calibration is a blocking up
		up(false);
	}
	
	public void up() {
		up(false);
	}
	
	public void up(boolean immediateReturn) {
		if (state == UP || state == GOUP)
			return;
		// wait for stop
		while (state != DOWN)
			Thread.yield();
		synchronized (armRegulator) {
			if (!TS.isPressed()) {
				state = GOUP;
				MC.forward();
			}
		}
		if (immediateReturn)
			return;
		while (MC.isMoving()) //should be equivalent to (state != UP) but maybe more secure
			Thread.yield();
	}

	public void down() {
		down(false);
	}
	
	public void down(boolean immediateReturn) {
		if (state == DOWN || state == GODOWN)
			return;
		// wait for stop
		while (state != UP)
			Thread.yield();
		synchronized (armRegulator) {
			state = GODOWN;
		}
		MC.rotateTo(goDownRounds,immediateReturn);
	}
	
	public class ArmRegulator extends Thread {
		 public void run() {
			 while (true) {
				 synchronized (this) {
					switch (state) {
					case GOUP:
						if (TS.isPressed()) {
							MC.stop();
							MC.resetTachoCount();
							state = UP;
						}
						break;
					case GODOWN:
						if (!MC.isMoving()) {
							state = DOWN;
						}
						break;
					} // end switch
				 } // end synchronized
				 try {sleep(1);} catch(InterruptedException ie ) {}
			 } // end while
		 }
	}
	
}