package it.polito.Checkers;
import lejos.nxt.Button;
import lejos.nxt.LCD;

public class test {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		LCD.drawString("Checkers GAME", 0, 0);
		LCD.refresh();
		Player pl1 = new ComputerPlayer(CheckersConstants.WHITE,2);
	    Player pl2 = new ComputerPlayer(CheckersConstants.BLACK,1);
	    Game game = new Game(pl1,pl2);
	    int result = game.play();
	    switch(result){
	    	case CheckersConstants.WHITE_WIN:
	    		LCD.drawString("White wins", 0, 1);
	    		break;
	    	case CheckersConstants.BLACK_WIN:
	    		LCD.drawString("Black wins", 0, 1);
	    		break;
	    }
	    LCD.refresh();
	    Button.waitForPress();
	}
}
