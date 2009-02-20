package it.polito.roboCheckers;

import it.polito.Checkers.CheckersConstants;
import it.polito.Navigation.ArmController;
import it.polito.Navigation.CheckersNavigator;
import it.polito.util.HumanInput;
import lejos.nxt.addon.ColorSensor;

public class Robot {
	static private ColorSensor CS = Factory.getColorSensor();
	static private CheckersNavigator navigator = Factory.getCheckersNavigator();
	static private HumanInput HI = Factory.getHumanInput();
	static private ArmController arm = Factory.getArmController();

	public static ColorSensor getColorSensor() {
		return CS;
	}

	public static CheckersNavigator getNavigator() {
		return navigator;
	}

	public static void main(String[] args) throws Exception {
		System.out.println("Checkers GAME");
		System.out.println("Waiting for Human Input.");
		HI.init();
		System.out.println("Waiting for START");
		HI.waitForStart(true);
		
		// Init players
		Player pl1 = new HumanPlayer(CheckersConstants.BLACK,
				CheckersConstants.BKING);
		Player pl2 = new ComputerPlayer(CheckersConstants.WHITE,
				CheckersConstants.WKING, 4);
		
		// Do calibration
		System.out.println("Calibrating Board");
		arm.calibrate();
		arm.down();
		navigator.calibrate();
		
		// Go Home
		arm.up(true);
		navigator.goHome();
		
		// Start new game
		Game game = new Game(pl1, pl2);
		int result = game.play();
		switch (result) {
		case CheckersConstants.WHITE_WIN:
			System.out.println("White wins");
			break;
		case CheckersConstants.BLACK_WIN:
			System.out.println("Black wins");
			break;
		}
		HI.destroy();
	}
}
