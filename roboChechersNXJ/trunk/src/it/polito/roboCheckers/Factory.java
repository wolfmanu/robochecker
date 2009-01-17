package it.polito.roboCheckers;

import lejos.nxt.SensorPort;
import lejos.nxt.addon.ColorSensor;
import it.polito.Navigation.ArmController;
import it.polito.Navigation.CheckersNavigator;
import it.polito.Navigation.MathNavigator;
import it.polito.util.ButtonInput;
import it.polito.util.HumanInput;

public class Factory {
	private static CheckersNavigator navigator = MathNavigator.getInstance();
	private static HumanInput HI = ButtonInput.getInstance();
	private static ColorSensor CS = new ColorSensor(SensorPort.S1);
	private static ArmController arm = ArmController.getInstance();

	public static CheckersNavigator getCheckersNavigator() {
		return navigator;
	}

	public static HumanInput getHumanInput() {
		return HI;
	}

	public static ColorSensor getColorSensor() {
		return CS;
	}

	public static ArmController getArmController() {
		return arm;
	}
}
