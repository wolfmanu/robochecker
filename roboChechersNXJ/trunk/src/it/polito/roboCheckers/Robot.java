package it.polito.roboCheckers;
import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.addon.ColorSensor;
import it.polito.Checkers.*;

public class Robot {
	static private ColorSensor CS = new ColorSensor(SensorPort.S1);
	static private CheckersNavigator navigator = SimpleNavigator.getInstance(Motor.A, Motor.B, CS);
	
	public static ColorSensor getColorSensor() {
		return CS;
	}
	
	public static CheckersNavigator getNavigator() {
		return navigator;
	}
	
	public static void main(String[] args) throws Exception {
		LCD.drawString("Checkers GAME", 0, 0);
		LCD.refresh();
		Player pl1 = new HumanPlayer(CheckersConstants.BLACK);
	    Player pl2 = new ComputerPlayer(CheckersConstants.WHITE,3);
	    navigator.calibrate();
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
	    LCD.refresh();
	    Button.waitForPress();
	}
}
