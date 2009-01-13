package it.polito.Navigation;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;

public class ArmControllerThr extends Thread {
	private static final int STOP = 0, GOUP = 1, GODOWN = 2;
	private static int status = STOP;
	private final int goDownRounds = -850;
	private Motor MC = null;
	private TouchSensor TS = null;
	private static ArmControllerThr controller = null;
	
	public static ArmControllerThr getInstance(){
		if (controller == null)
			controller = new ArmControllerThr(Motor.C,new TouchSensor(SensorPort.S2));
		return controller;
	}
	
	private ArmControllerThr (Motor MC, TouchSensor TS) {
		this.MC = MC;
		this.TS = TS;
	}
	
	public void up() {
		status = GOUP;
		this.start();
	}
	public void down() {
		status = GODOWN;
		this.start();
	}
	
	public void run() {
		System.out.println("Entro in Run");
		switch (status) {
		case STOP:
			System.out.println("Entro in switch 1");
			MC.stop();
			break;
		case GOUP:
			System.out.println("Entro in switch 2");
			while (status != STOP) {
				Thread.yield();
			}
			System.out.println("Passo il while 2");
			if (!TS.isPressed()) {
				MC.forward();
				
				while (!TS.isPressed()) {
					Thread.yield();
				}
				MC.stop();
				MC.resetTachoCount();
			}
			status = STOP;
			break;
		case GODOWN:
			System.out.println("Entro in switch 3");
			while (status != STOP) {
				Thread.yield();
			}
			System.out.println("Passo il while 3");
			up();
			MC.rotate(goDownRounds);
			status = STOP;
			break;
		}
	}
	
}
