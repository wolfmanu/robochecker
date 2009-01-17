package it.polito.Navigation;
import lejos.nxt.Button;
import lejos.nxt.Motor;
import lejos.nxt.MotorPort;
import lejos.nxt.addon.ColorSensor;
import it.polito.Checkers.Square;
import it.polito.roboCheckers.Factory;
/**
 * Move the robot over the select chechers's cell
 * @author davide
 * 
 */
public class SimpleNavigator implements CheckersNavigator {
	private int x;
	private int y;
	private boolean calibrated;
	private static final int lashA = 90, lashB = 230;
	private static final LashMotor MA = new LashMotor(MotorPort.A, lashA);
	private static final LashMotor MB = new LashMotor(MotorPort.B, lashB);
	private static final ColorSensor CS = Factory.getColorSensor();
	private static final ArmController arm = Factory.getArmController();


	// Calibration Color Constants
	private static final int GO = 17, // White
					STOP = 9,	// Red
					ROTATE = 0,
					POLLING_PERIOD = 10;	// Black
	
	// Board Mapping
	private final int offA = 1500, offB = 11000;
	private final int[] posx={-9000, -7000, -5500, -3900, -2000, -300, 1400, 3200};
	private final int[] posy={     0,  1020,  2040,  3060,  4080, 5100, 6120, 7140};
	private final int[] dely={  1900,  1000,   750,   150,   -50,  -60,  300,  700};
	private final int homeX=-3000, homeY=-4500;
	
	private static SimpleNavigator navigator = null;
	public static SimpleNavigator getInstance(){
		if (navigator == null)
			navigator = new SimpleNavigator();
		return navigator;
	}
	
	private SimpleNavigator () {
		this.calibrated = false;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public LashMotor getMotorA () {
		return MA;
	}

	public LashMotor getMotorB () {
		return MB;
	}
	
	public boolean isCalibrated () {
		return calibrated;
	}
	
	public void goHome () throws NotCalibratedException {
		this.rotateTo(homeX,homeY);
	}
	
	public void goTo(Square dest) throws NotCalibratedException {
		goTo(dest.getRow(),dest.getCol());
	}
	
	public void goTo(int newX, int newY) throws NotCalibratedException {

		if (!isCalibrated()) {
			throw new NotCalibratedException();
		}
		int destAngleA, destAngleB;

		//Check for bounds
		if (newX<0) newX=0;
		if (newY<0) newY=0;
		if (newX>7) newX=7;
		if (newY>7) newY=7;
	
		destAngleA = offA+posy[newY]+dely[newX];
		destAngleB = offB+posx[newX];
				
		rotateTo(destAngleA,destAngleB);
	
		this.x=newX;
		this.y=newY;
	}

	public boolean isMoving() {
		return MA.isMoving() || MB.isMoving();
	}

	public void setSpeed(int speedA, int speedB) {
		MA.setSpeed(speedA);
		MB.setSpeed(speedB);
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

	/***
	 * follows the black line until the red point
	 * (please look at the img/grid.jpg file)
	 * @author Matteo
	 */
	public void calibrate() {
		setSpeed(500, 500);
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
			try { Thread.sleep(POLLING_PERIOD); } catch (InterruptedException e) {}
		}
		MB.rotate(lashB);
		MA.resetTachoCount(); MB.resetTachoCount();
		this.calibrated = true;
		setSpeed(1000, 2000);
	}
	
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
