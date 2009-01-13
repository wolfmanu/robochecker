package it.polito.roboCheckers;
import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.addon.ColorSensor;
import it.polito.BluetoothComm.NXTCommHandle;
import it.polito.Checkers.*;
import it.polito.Navigation.CheckersNavigator;
import it.polito.Navigation.MathNavigator;
import it.polito.Navigation.SimpleNavigator;

public class Robot {
	static private ColorSensor CS = new ColorSensor(SensorPort.S1);
	static private CheckersNavigator navigator = MathNavigator.getInstance();
	
	public static ColorSensor getColorSensor() {
		return CS;
	}
	
	public static CheckersNavigator getNavigator() {
		return navigator;
	}
	
	public static void main(String[] args) throws Exception {
		System.out.println("Checkers GAME");
		System.out.println("Waiting for BT Conn.");
		NXTCommHandle.getInstance().connect();
		System.out.println("Waiting for START");
		NXTCommHandle.getInstance().waitForStart();
		System.out.println("Calibrating Board");
		Player pl1 = new HumanPlayer(CheckersConstants.BLACK, CheckersConstants.BKING);
	    Player pl2 = new ComputerPlayer(CheckersConstants.WHITE, CheckersConstants.WKING,3);
		navigator.setSpeed(300, 1000);
		navigator.calibrate();
		navigator.setSpeed(1000, 1000);
	    Game game = new Game(pl1,pl2);
	    int result = game.play();
	    switch(result){
	    	case CheckersConstants.WHITE_WIN:
	    		System.out.println("White wins");
	    		break;
	    	case CheckersConstants.BLACK_WIN:
	    		System.out.println("Black wins");
	    		break;
	    }
	    NXTCommHandle.getInstance().waitForMove();
	    NXTCommHandle.getInstance().disconnect();
	}
}
