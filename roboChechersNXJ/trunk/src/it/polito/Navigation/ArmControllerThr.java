package it.polito.Navigation;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;

public class ArmControllerThr {
	private static final int STOP = 0, GOUP = 1, GODOWN = 2;
	private static int status = STOP;
	private final static int goDownRounds = -850;
	private static Motor MC = null;
	private static TouchSensor TS = null;
	private static ArmControllerThr controller = null;
	private static Thread t;
	
	public static ArmControllerThr getInstance(){
		if (controller == null)
			controller = new ArmControllerThr(Motor.C,new TouchSensor(SensorPort.S2));
		return controller;
	}
	
	private ArmControllerThr (Motor MC, TouchSensor TS) {
		this.MC = MC;
		this.TS = TS;
	}
	
	public synchronized static void stop() {
		t = new Thread() {
			public void run() {
				//System.out.println("STOP");
				MC.stop();
			}
		};
		t.start();
		status = STOP;
	}
	
	public synchronized void up() {
		t = new Thread() {
    		public void run() {
    				//System.out.println("UP");
    				//while (status != STOP) {
    				//	Thread.yield();
    				//}
    				//System.out.println("Passo il while 2");
					MC.forward();
					
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {}
									
					MC.stop();
					MC.resetTachoCount();
    				status = STOP;
    		}
        };
    	t.start();
		status = GOUP;
	}
	public synchronized void down() {
		t = new Thread() {
    		public void run() {
    			//System.out.println("DOWN");
				//while (status != STOP) {
				//	Thread.yield();
				//}
				//System.out.println("Passo il while 3");
				//up();
				MC.rotate(goDownRounds);
				status = STOP;
    		}
        };
    	t.start();
		status = GOUP;
	}
	
	
	
}
