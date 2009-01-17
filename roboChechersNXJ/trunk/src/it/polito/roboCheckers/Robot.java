package it.polito.roboCheckers;

import it.polito.Checkers.CheckersConstants;
import it.polito.Navigation.CheckersNavigator;
import it.polito.util.HumanInput;
import lejos.nxt.addon.ColorSensor;

public class Robot {
	static private ColorSensor CS = Factory.getColorSensor();
	static private CheckersNavigator navigator = Factory.getCheckersNavigator();
	static private HumanInput HI = Factory.getHumanInput();

	public static ColorSensor getColorSensor() {
		return CS;
	}

	public static CheckersNavigator getNavigator() {
		return navigator;
	}

	public static void main(String[] args) throws Exception {
		System.out.println("Checkers GAME");
		System.out.println("Waiting for BT Conn.");
		HI.init();
		System.out.println("Waiting for START");
		HI.waitForStart(true);
		System.out.println("Calibrating Board");
		Player pl1 = new HumanPlayer(CheckersConstants.BLACK,
				CheckersConstants.BKING);
		Player pl2 = new ComputerPlayer(CheckersConstants.WHITE,
				CheckersConstants.WKING, 3);
		navigator.calibrate();
		navigator.goHome();
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
