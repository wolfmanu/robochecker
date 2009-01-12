package it.polito.roboCheckers;
import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.addon.ColorSensor;
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
		LCD.drawString("Checkers GAME", 0, 0);
		LCD.refresh();
		Player pl1 = new HumanPlayer(CheckersConstants.BLACK);
	    Player pl2 = new ComputerPlayer(CheckersConstants.WHITE,3);
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
	    LCD.refresh();
	    Button.waitForPress();
	}
}
