package it.polito.Navigation;

import it.polito.Checkers.Square;
import lejos.nxt.Motor;
import lejos.nxt.MotorPort;
import lejos.nxt.SensorPort;
import lejos.nxt.TachoMotorPort;
import lejos.nxt.addon.ColorSensor;

public class MathNavigator implements CheckersNavigator {
	private static final int POLLING_PERIOD = 50, STOP_ROTATE = 0, STOP_MOVE = 1;
	private static final int lashA = 90, lashB = 230;
	private static CheckersNavigator navigator = null;
	private static final LashMotor MA = new LashMotor(MotorPort.A,lashA);
	private static final LashMotor MB = new LashMotor(MotorPort.B,lashB);
	private static final ColorSensor CS = new ColorSensor(SensorPort.S1);
	
	private boolean calibrated;
	private int x,y;
	private final double
		l = 16.0,
		r=11.0,
		squareWidth = 2.0,
		squareOffset = 1.0,
		coeffB=1,
		coeffA=1;
	private double alpha,beta,gamma,Cx;
	
	public static CheckersNavigator getInstance(){
		if (navigator == null)
			navigator = new MathNavigator();
		return navigator;
	}


	public MathNavigator() {
		this.calibrated = false;
	}

	public void calibrate() {
		while (CS.getColorNumber()!=STOP_ROTATE) {
			left();
			try {
				Thread.sleep(POLLING_PERIOD);
			} catch (InterruptedException e) {}
		}
		left(3*lashB); // Remove lash
		while (CS.getColorNumber()==STOP_ROTATE) {
			right();
			try {
				Thread.sleep(POLLING_PERIOD);
			} catch (InterruptedException e) {}
		}
		MB.resetTachoCount();
		while (CS.getColorNumber()!=STOP_ROTATE) {
			right();
			try {
				Thread.sleep(POLLING_PERIOD);
			} catch (InterruptedException e) {}
		}		
		alpha = MB.getTachoCount()/coeffB;
		while (CS.getColorNumber()!=STOP_MOVE) {
			backward();
			try {
				Thread.sleep(POLLING_PERIOD);
			} catch (InterruptedException e) {}
		}
		forward(lashA);
		left(lashB);
		MA.resetTachoCount();
		MB.resetTachoCount();
		
		// Determine constants
		beta = 2*Math.acos(l/(2*r*Math.sin(alpha/2)));
		gamma = (Math.PI - alpha - beta)/2;
		Cx = r*Math.sin((alpha+beta)/2);
		this.calibrated = true;
	}

	public void goHome() throws notCalibratedException {
		if (!isCalibrated())
			throw new notCalibratedException();
		rotateTo(0,0);
	}

	public void goTo(Square dest) throws notCalibratedException {
		goTo(dest.getRow(),dest.getCol());
	}

	public void goTo(int newX, int newY) throws notCalibratedException {
		if (!isCalibrated())
			throw new notCalibratedException();
		
		double theta, Cy, Px, Py;
		int limitAngleA, limitAngleB;
		
		Px = (newX * squareWidth)+squareOffset;
		Py = (newY * squareWidth)+squareOffset;
		theta = Math.acos((Cx-Px)/r) - gamma;
		Cy = Py - r*Math.sin(Math.acos((Cx-Px)/r));
		
		limitAngleA = Math.round((float)(Cy*coeffA));
		limitAngleB = Math.round((float)(theta*coeffB));
		rotateTo(limitAngleA,limitAngleB);
		
		this.x = newX;
		this.y = newY;
	}

	public boolean isCalibrated() {
		return calibrated;
	}

	public boolean isMoving() {
		return MA.isMoving() || MB.isMoving();
	}

	public void setSpeed(int speedA, int speedB) {
		MA.setSpeed(speedA);
		MB.setSpeed(speedB);
	}

	public Motor getMotorA() {
		return MA;
	}

	public Motor getMotorB() {
		return MB;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	private void rotateTo(int destAngleA, int destAngleB) {
		rotateTo(destAngleA,destAngleB,false);
	}
	
	private void rotateTo(int destAngleA, int destAngleB, boolean nonBlocking) {
		MA.rotateTo(destAngleA, true);
		MB.rotateTo(destAngleB, true);
		if (!nonBlocking)
			waitForMotors(new Motor[] {MA,MB});
	}

	private void left() { MB.forward();	}
	private void right() { MB.backward(); }
	private void forward() { MA.forward(); }
	private void backward() { MA.backward(); }
	private void left(int angle) { MB.rotate(angle);	}
	private void right(int angle) { MB.rotate(-angle); }
	private void forward(int angle) { MA.rotate(angle); }
	private void backward(int angle) { MA.rotate(-angle); }	
	/***
	 * Wait for motor(s) in motorList to stop
	 * @throws InterruptedException 
	 */
	private void waitForMotors(Motor[] motorList){
		for (int i=0; i < motorList.length; i++){
			while (motorList[i].isMoving()){
				try {
					Thread.sleep(POLLING_PERIOD);
				} catch (InterruptedException e) { }
			}
		}
	}
}
