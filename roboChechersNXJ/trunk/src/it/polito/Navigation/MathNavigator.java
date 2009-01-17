package it.polito.Navigation;

import it.polito.Checkers.*;
import it.polito.roboCheckers.Factory;
import lejos.nxt.Motor;
import lejos.nxt.MotorPort;
import lejos.nxt.SensorPort;
import lejos.nxt.addon.ColorSensor;

public class MathNavigator implements CheckersNavigator {
	private static final int POLLING_PERIOD = 10, STOP_ROTATE_L = 5,
			STOP_ROTATE_R = 0, STOP_MOVE = 2;
	private static final int lashA = 90, lashB = 230;
	private static MathNavigator navigator = null;
	private static final LashMotor MA = new LashMotor(MotorPort.A, lashA);
	private static final LashMotor MB = new LashMotor(MotorPort.B, lashB);
	private static final ColorSensor CS = Factory.getColorSensor();
	private static final ArmController arm = Factory.getArmController();

	private boolean calibrated;
	private int x, y;
	private final double offX = 0.5, l = 16.0, r = 12, squareWidth = 2.0,
			squareOffset = 0.8, coeffB = 60000, // 61142 // 60000
			coeffA = 510;
	private double alpha, beta, gamma, yOffset, Cx;
	private double[] correzioniY = new double[8];

	public static MathNavigator getInstance() {
		if (navigator == null)
			navigator = new MathNavigator();
		return navigator;
	}

	public MathNavigator() {
		this.calibrated = false;
	}

	public void calibrate() {
		setSpeed(500, 1000);
		arm.down();
		backward(2000);

		// Reaches LEFT RED BAND
		while (CS.getColorNumber() != STOP_ROTATE_L) {
			left();
			try {
				Thread.sleep(POLLING_PERIOD);
			} catch (InterruptedException e) {
			}
		}
		MB.stop();
		left(2 * lashB); // Remove lash

		// Recovers LEFT RED BAND BORDERS, moving to right
		if (CS.getColorNumber() == STOP_ROTATE_L) {
			int x = CS.getColorNumber();
			setSpeed(500, 200);
			while (CS.getColorNumber() == x) {
				right();
				try {
					Thread.sleep(POLLING_PERIOD);
				} catch (InterruptedException e) {
				}
			}
			MB.stop();
		}
		setSpeed(500, 1000);
		MB.resetTachoCount();

		// Reaches RIGHT BLACK BAND
		while (CS.getColorNumber() != STOP_ROTATE_R) {
			right();
			try {
				Thread.sleep(POLLING_PERIOD);
			} catch (InterruptedException e) {
			}
		}
		MB.stop();

		alpha = Math.abs(((MB.getTachoCount()) * 2 * java.lang.Math.PI)
				/ coeffB);

		// Reaches UPPER BLUE BAND
		while (CS.getColorNumber() != STOP_MOVE) {
			forward();
			try {
				Thread.sleep(POLLING_PERIOD);
			} catch (InterruptedException e) {
			}
		}
		MA.stop();
		left(lashB);
		MA.resetTachoCount();
		MB.resetTachoCount();

		// Determine constants
		double betaarg = l / (2 * r * java.lang.Math.sin(alpha / 2));
		beta = 2 * java.lang.Math.acos(betaarg);
		gamma = (java.lang.Math.PI - alpha - beta) / 2;
		Cx = r * java.lang.Math.sin((alpha + beta) / 2);
		yOffset = r * java.lang.Math.sin(java.lang.Math.acos(Cx / r));

		System.out.println("alpha: " + alpha);
		System.out.println("betaarg: " + betaarg);
		System.out.println("beta: " + beta);
		System.out.println("gamma: " + gamma);
		System.out.println("Cx: " + Cx);

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		}
		calibrateY();

		this.calibrated = true;
	}

	private void calibrateY() {
		double row = 20;
		setSpeed(1000, 2000);
		for (int i = 0; i < 8; i++) {
			moveTo((i * squareWidth) + squareOffset, row);
			setSpeed(200, 2000);
			switch (CS.getColorNumber()) {
			case CheckersConstants.EMPTY:
				correzioniY[i] = 0;
				break;
			case STOP_ROTATE_L:
				correzioniY[i] = MA.getTachoCount();
				while (CS.getColorNumber() == STOP_ROTATE_L) {
					backward();
				}
				correzioniY[i] -= MA.getTachoCount();
				break;
			case STOP_ROTATE_R:
				correzioniY[i] = MA.getTachoCount();
				while (CS.getColorNumber() != STOP_ROTATE_L) {
					forward();
				}
				correzioniY[i] -= MA.getTachoCount();
				break;
			default:
				correzioniY[i] = MA.getTachoCount();
				while (CS.getColorNumber() != STOP_ROTATE_L) {
					forward();
				}

				correzioniY[i] -= MA.getTachoCount();
				break;
			}
			correzioniY[i] /= -coeffA;
			System.out.print(Float.toString((float) correzioniY[i]) + " ");
		}
		System.out.println("");
		MA.stop();
		setSpeed(1000, 2000);
	}
	
	public void goHome() throws NotCalibratedException {
		if (!isCalibrated())
			throw new NotCalibratedException();
		rotateTo(0, 0);
	}

	public void goTo(Square dest) throws NotCalibratedException {
		goTo(dest.getRow(), dest.getCol());
	}

	public void goTo(int newX, int newY) throws NotCalibratedException {
		if (!isCalibrated())
			throw new NotCalibratedException();
		double Px, Py;
		
		// Check for bounds
		if (newX<0) newX=0;
		if (newY<0) newY=0;
		if (newX>7) newX=7;
		if (newY>7) newY=7;
		
		// Checkers Mapping
		Px = (newX * squareWidth) + squareOffset;
		Py = (newY * squareWidth) + squareOffset + correzioniY[newX];
		
		// Move
		moveTo(Px, Py);
		this.x = newX;
		this.y = newY;
	}

	private void moveTo(double x, double y) {
		double theta, Cy;
		int limitAngleA, limitAngleB;
		
		// Change coordinates
		theta = Math.acos((Cx - (x + offX)) / r) - gamma;
		Cy = y - r * Math.sin(Math.acos((Cx - x) / r)) + yOffset;

		limitAngleA = Math.round((float) (Cy * coeffA));
		limitAngleB = Math
				.round((float) ((theta * coeffB) / (2 * java.lang.Math.PI)));
		rotateTo(limitAngleA, limitAngleB);
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
		rotateTo(destAngleA, destAngleB, false);
	}

	private void rotateTo(int destAngleA, int destAngleB, boolean nonBlocking) {
		MA.rotateTo(destAngleA, true);
		MB.rotateTo(destAngleB, true);
		if (!nonBlocking)
			waitForMotors(new Motor[] { MA, MB });
	}

	public void left() {
		MB.forward();
	}

	public void right() {
		MB.backward();
	}

	public void forward() {
		MA.forward();
	}

	public void backward() {
		MA.backward();
	}

	public void left(int angle) {
		MB.rotate(angle);
	}

	public void right(int angle) {
		MB.rotate(-angle);
	}

	public void forward(int angle) {
		MA.rotate(angle);
	}

	public void backward(int angle) {
		MA.rotate(-angle);
	}

	/***************************************************************************
	 * Wait for motor(s) in motorList to stop
	 * @throws InterruptedException
	 */
	private void waitForMotors(Motor[] motorList) {
		for (int i = 0; i < motorList.length; i++) {
			while (motorList[i].isRotating()) {
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
