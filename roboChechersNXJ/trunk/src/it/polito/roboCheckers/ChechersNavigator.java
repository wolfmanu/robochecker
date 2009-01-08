package it.polito.roboCheckers;
import lejos.nxt.Button;
import lejos.nxt.ButtonListener;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.addon.ColorSensor;
import it.polito.Checkers.Square;
/**
 * Move the robot over the select chechers's cell
 * @author davide
 * 
 */
public class ChechersNavigator {
	private int x;
	private int y;
	private boolean calibrated;
	private Motor MA, MB;
	private ColorSensor CS = null;
	
	private static int lashA = 90,
					lashB = 230;
	// Calibration Color Constants
	private static int GO = 17, // White
					STOP = 9,	// Red
					ROTATE = 0;	// Black
	// Board Mapping
	private int offA = 1500, offB = 11000;//imperfetto
	private int[] posx={-10700, -7700, -5700, -3900, -2000, -300, 1400, 3200, -3000};
	private int[] posy={     0,  1020,  2040,  3060,  4080, 5100, 6120, 7140, -4500};
	private int[] dely={  2800,  1400,   750,   150,   -50,  -60,  300,  700, 0};
	
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
		this.calibrated = false;
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
	
	public boolean isCalibrated () {
		return calibrated;
	}
	public void goHome () throws notCalibratedException {
		this.goTo(8,8);
	}
	public void goTo(Square dest) throws notCalibratedException {
		goTo(dest.getRow(),dest.getCol());
	}
	public void goTo(int newX, int newY) throws notCalibratedException {

		if (!isCalibrated()) {
			throw new notCalibratedException();
		}
		int destAngleA, destAngleB;

		//controllo estremi
		/*
		if (newX<0) newX=0;
		if (newY<0) newY=0;
		if (newX>7) newX=7;
		if (newY>7) newY=7;
		*/
		//printing what i'm doing on lcd
		LCD.clear();
		
		LCD.drawString("from:", 0, 0);
		LCD.drawInt(this.x, 5, 0);
		LCD.drawInt(this.y, 7, 0);
		LCD.drawString("to:", 0, 1);
		LCD.drawInt(newX, 5, 1);
		LCD.drawInt(newY, 7, 1);
		LCD.refresh();
		
		destAngleA = offA+posy[newY]+dely[newX];
		destAngleB = offB+posx[newX];
				
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
	 * follows the black line until the red point, then moves to (0,0)
	 * (please look at the img/grid.jpg file)
	 * TODO: IMPLEMENT
	 * @author Matteo
	 */
	public void calibrate(ColorSensor CS) {
		int color = GO;
		while (!Button.ESCAPE.isPressed()) {
			color = (int)CS.getColorNumber();
			if (color == STOP) { 
				MA.stop();
				MB.stop();
				break;
			} else if (color == ROTATE) {
				MA.stop();
				MB.backward();
			} else {
				MA.forward();
				MB.stop();
			}
			LCD.clear();
			LCD.drawString("color", 0, 0);
			LCD.drawInt((int)CS.getColorNumber(),7,0);
			LCD.refresh();
			try { Thread.sleep(100); } catch (InterruptedException e) {}
		}
		MB.rotate(lashB);
		MA.resetTachoCount(); MB.resetTachoCount();
		this.calibrated = true;
		/*
		Button.ENTER.addButtonListener(new ButtonListener() {
			public void buttonPressed(Button b) { Motor.A.forward(); }
			public void buttonReleased(Button b) { Motor.A.stop(); } });
		Button.LEFT.addButtonListener(new ButtonListener() {
			public void buttonPressed(Button b) { Motor.B.forward(); }
			public void buttonReleased(Button b) { Motor.B.stop(); } });
		Button.RIGHT.addButtonListener(new ButtonListener() {
			public void buttonPressed(Button b) { Motor.B.backward(); }
			public void buttonReleased(Button b) { Motor.B.stop(); } });
		while (true) {
			if (Button.ESCAPE.isPressed())	break;
			LCD.clear();
			LCD.drawString("A:", 0, 4); LCD.drawInt(this.MA.getTachoCount(), 4, 4);
			LCD.drawString("B:", 0, 5);	LCD.drawInt(this.MB.getTachoCount(), 4, 5);
			LCD.refresh();
			try { Thread.sleep(100); } catch (InterruptedException e) {}
		}
		*/
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
