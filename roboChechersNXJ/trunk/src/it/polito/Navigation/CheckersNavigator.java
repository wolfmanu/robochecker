package it.polito.Navigation;

import it.polito.Checkers.Square;
import lejos.nxt.Motor;

public interface CheckersNavigator {
	public int getX();
	public int getY();
	public Motor getMotorA();
	public Motor getMotorB();
	public boolean isCalibrated();
	public void goHome() throws notCalibratedException;
	public void goTo(Square dest) throws notCalibratedException;
	public void goTo(int newX, int newY) throws notCalibratedException;
	public boolean isMoving();
	public void setSpeed(int speedA, int speedB);
	public void calibrate();
}