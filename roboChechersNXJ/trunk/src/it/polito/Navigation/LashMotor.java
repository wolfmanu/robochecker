package it.polito.Navigation;

import lejos.nxt.Motor;
import lejos.nxt.TachoMotorPort;

public class LashMotor extends Motor {
	private int lash = 0;
	public LashMotor(TachoMotorPort port, int lash) {
		super(port);
		this.lash = lash;
	}
	public LashMotor(TachoMotorPort port) {
		super(port);
	}
	public void setLash (int lash) {
		this.lash = lash;
	}
	public void rotateTo(int limitAngle) {
		rotateTo(limitAngle,false);
	}
	public void rotateTo(int limitAngle, boolean nonBlocking) {
		if (limitAngle < super.getTachoCount())
			limitAngle -= lash;
		super.rotateTo(limitAngle, nonBlocking);
	}

}