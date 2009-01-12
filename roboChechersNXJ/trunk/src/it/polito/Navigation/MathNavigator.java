package it.polito.Navigation;

import it.polito.Checkers.Square;
import lejos.nxt.Button;
import lejos.nxt.Motor;
import lejos.nxt.MotorPort;
import lejos.nxt.SensorPort;
import lejos.nxt.TachoMotorPort;
import lejos.nxt.addon.ColorSensor;

public class MathNavigator implements CheckersNavigator {
	private static final int POLLING_PERIOD = 50, STOP_ROTATE = 2, STOP_MOVE = 5;
	private static final int lashA = 90, lashB = 230;
	private static CheckersNavigator navigator = null;
	private static final LashMotor MA = new LashMotor(MotorPort.A,lashA);
	private static final LashMotor MB = new LashMotor(MotorPort.B,lashB);
	private static final ColorSensor CS = new ColorSensor(SensorPort.S1);
	
	private boolean calibrated;
	private int x,y;
	private final double
		l = 16.0,
		r = 11.0,
		squareWidth = 2.0,
		squareOffset = 1.5,
		coeffB = 61142,
		coeffA = 510;
	private double alpha,beta,gamma,yOffset,Cx;
	
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
		MB.stop();
		//left(3*lashB); // Remove lash
		if (CS.getColorNumber()==STOP_ROTATE) {
			while (CS.getColorNumber()==STOP_ROTATE) {
				right();
				try {
					Thread.sleep(POLLING_PERIOD);
				} catch (InterruptedException e) {}
			}
			MB.stop();
		}
		MB.resetTachoCount();
		while (CS.getColorNumber()!=STOP_ROTATE) {
			right();
			try {
				Thread.sleep(POLLING_PERIOD);
				//System.out.println("Color: "+Integer.toString(CS.getColorNumber()));
			} catch (InterruptedException e) {}
		}		
		MB.stop();
		alpha = Math.abs((MB.getTachoCount()*2*java.lang.Math.PI)/coeffB);
		
		while (CS.getColorNumber()!=STOP_MOVE) {
			backward();
			try {
				Thread.sleep(POLLING_PERIOD);
				//System.out.println("Color: "+Integer.toString(CS.getColorNumber()));
			} catch (InterruptedException e) {}
		}
		MA.stop();
		//forward(lashA);
		//left(lashB);
		MA.resetTachoCount();
		MB.resetTachoCount();
		
		// Determine constants
		double betaarg = l/(2*r*java.lang.Math.sin(alpha/2));
		beta = 2*java.lang.Math.acos(betaarg);
		gamma = (java.lang.Math.PI - alpha - beta)/2;
		Cx = r*java.lang.Math.sin((alpha+beta)/2);
		yOffset = r*java.lang.Math.sin(java.lang.Math.acos(Cx/r));
			
		System.out.println("alpha: " + alpha);
		System.out.println("betaarg: " + betaarg);
		System.out.println("beta: " + beta);
		System.out.println("gamma: " + gamma);
		System.out.println("Cx: " + Cx);
		Button.waitForPress();
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
		Cy = Py - r*Math.sin(Math.acos((Cx-Px)/r)) + yOffset;
		
		limitAngleA = Math.round((float)(Cy*coeffA));
		limitAngleB = Math.round((float)((theta*coeffB)/(2*java.lang.Math.PI)));
//		System.out.println("Cy: " + Cy);
//		System.out.println("theta: " + theta);
//		System.out.println("Px: " + Px + " Py: " + Py);
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

	public void left() { MB.forward();	}
	public void right() { MB.backward(); }
	public void forward() { MA.forward(); }
	public void backward() { MA.backward(); }
	public void left(int angle) { MB.rotate(angle);	}
	public void right(int angle) { MB.rotate(-angle); }
	public void forward(int angle) { MA.rotate(angle); }
	public void backward(int angle) { MA.rotate(-angle); }	
	/***
	 * Wait for motor(s) in motorList to stop
	 * @throws InterruptedException 
	 */
	private void waitForMotors(Motor[] motorList){
		System.out.println("Length: " + motorList.length + motorList[0].isRotating() +motorList[0].isMoving());
		for (int i=0; i < motorList.length; i++){
			while (motorList[i].isRotating()){
				try {
					Thread.sleep(POLLING_PERIOD);
				} catch (InterruptedException e) { 
					System.out.println(e.getMessage());
					System.out.println("E!");
				}
			}
		}
	}
}
