package it.polito.test;

import it.polito.Navigation.ArmController;
import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Motor;

public class ArmSuGiu {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		ArmController arm = ArmController.getInstance();
		arm.calibrate();
		while (true) {
			Motor.A.forward();
			if (Button.LEFT.isPressed()) {
				arm.up(true);
			}
			if (Button.RIGHT.isPressed()) {
				arm.down(true);
			}
			if (Button.ESCAPE.isPressed()) {
				break;
			}

		}
	}

}
