package it.polito.roboCheckers;
import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.addon.ColorSensor;
import it.polito.BluetoothComm.NXTCommHandle;
import it.polito.Checkers.*;
import it.polito.Navigation.*;
import it.polito.util.*;

public class Robot {
	static private ColorSensor CS = new ColorSensor(SensorPort.S1);
	static private CheckersNavigator navigator = MathNavigator.getInstance();
	static private HumanInput HI = NXTCommHandle.getInstance();
	
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
	    HI.destroy();
	}
}
