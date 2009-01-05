import lejos.nxt.Button;
import lejos.nxt.ButtonListener;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.addon.ColorSensor;
/**
 * Move the robot over the select chechers's cell
 * @author davide
 * 
 */
public class ChechersNavigator {
	private int x;
	private int y;
	private int lashA, lashB;
	private Motor MA, MB;
	private ColorSensor CS = null;
	
	int[] posx={-7500, -5700, -4250, -2800, -1450, 0 , 1400, 3000};
	int[] posy={0, 1000, 2000, 3000, 4000, 5000 , 6000, 7000};
	int[] dely={1900,1200, 550, 150, 50, 0, 150, 500};
	
	private static ChechersNavigator navigator = null;
	
	public static ChechersNavigator getInstance(){
		if (navigator == null)
			navigator = new ChechersNavigator(Motor.A, Motor.B, new ColorSensor(SensorPort.S1));
		return navigator;
	}

	public static ChechersNavigator getInstance(Motor MA, Motor MB){
		if (navigator == null)
			navigator = new ChechersNavigator(MA,MB,new ColorSensor(SensorPort.S1));
		return navigator;
	}

	public static ChechersNavigator getInstance(Motor MA, Motor MB, SensorPort port){
		if (navigator == null)
			navigator = new ChechersNavigator(MA,MB,new ColorSensor(port));
		return navigator;
	}

	
	public static ChechersNavigator getInstance(Motor MA, Motor MB, ColorSensor CS){
		if (navigator == null)
			navigator = new ChechersNavigator(MA,MB,CS);
		return navigator;
	}
	
	private ChechersNavigator (Motor MA, Motor MB, ColorSensor CS) {
		this.MA = MA;
		this.MB = MB;
		this.CS = CS;
		this.MA.resetTachoCount();
		this.MB.resetTachoCount();
		this.x = 5;
		this.y = 0;
		this.lashA = 90;
		this.lashB = 230;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public Motor getMotorA () {
		return MA;
	}

	public Motor getMotorB () {
		return MB;
	}
	
	public void goTo(int newX, int newY) {
		int destAngleA, destAngleB;
		
		//controllo estremi
		if (newX<0) newX=0;
		if (newY<0) newY=0;
		if (newX>7) newX=7;
		if (newY>7) newY=7;
		//printing what i'm doing on lcd
		LCD.clear();
		
		LCD.drawString("from:", 0, 0);
		LCD.drawInt(this.x, 5, 0);
		LCD.drawInt(this.y, 7, 0);
		LCD.drawString("to:", 0, 1);
		LCD.drawInt(newX, 5, 1);
		LCD.drawInt(newY, 7, 1);
		LCD.refresh();
		
		destAngleA = posy[newY]+dely[newX];
		destAngleB = posx[newX];
				
		//controlla se deve recuperare il gioco
		if (destAngleA < this.MA.getTachoCount()) {
			destAngleA -= lashA;
			LCD.drawString("gioco A", 0, 2);
		}
		
		if (destAngleB < this.MB.getTachoCount()) {
			destAngleB -= lashB;
			LCD.drawString("gioco B", 0, 3);
		}
		LCD.refresh();	
		
		this.MA.rotateTo(destAngleA,true);
		this.MB.rotateTo(destAngleB,true);
		waitForMotors(new Motor[]{this.MA, this.MB});
		
		LCD.drawString("A:", 0, 4);
		LCD.drawInt(this.MA.getTachoCount(), 4, 4);
		LCD.drawString("B:", 0, 5);
		LCD.drawInt(this.MB.getTachoCount(), 4, 5);
		LCD.refresh();
		
		this.x=newX;
		this.y=newY;
	}

	public boolean isMoving() {
		return this.MA.isMoving() || this.MB.isMoving();
	}

	public void setSpeed(int speedA, int speedB) {
		this.MA.setSpeed(speedA);
		this.MB.setSpeed(speedB);
	}

	/***
	 * follows the black line until the grey point, then moves to (0,0)
	 * (please look at the img/grid.jpg file)
	 * TODO: IMPLEMENT
	 * @author Matteo
	 */
	public void calibrate(ColorSensor CS) {
		int color = -1;
		while (!Button.ESCAPE.isPressed() && color != 9) {
			color = (int)CS.getColorNumber();
			switch (color) {
				case 9: 
					MA.stop();
					MB.stop();
					break;
				case 0:
					MA.stop();
					MB.backward();
					break;
				default:
					MA.forward();
					MB.stop();
			}
			LCD.clear();
			LCD.drawString("color", 0, 0);
			LCD.drawInt((int)CS.getColorNumber(),7,0);
			LCD.refresh();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {}
		}
	}
	
	public void calibrate() {
		if (this.CS == null)
			calibrate(new ColorSensor(SensorPort.S1));
		else
			calibrate(CS);
	}
	
	/***
	 * Wait for motor(s) in motorList to stop
	 * @throws InterruptedException 
	 */
	private void waitForMotors(Motor[] motorList){
		//LCD.drawString("Waiting for motors",0,0);
		for (int i=0; i < motorList.length; i++){
			while (motorList[i].isMoving()){
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) { }
			}
		}
	}
	
}
