import lejos.nxt.SensorPort;
import lejos.nxt.addon.ColorSensor;
import it.polito.Navigation.*;
import it.polito.Checkers.*;

public class Wrapper {
	private static CheckersNavigator navigator = MathNavigator.getInstance();
	private static ArmController arm = ArmController.getInstance();
	private static Board board = new Board();
	private static int pieceHuman = CheckersConstants.BLACK;
	private static int pieceRobot = CheckersConstants.WHITE;
	private static Square from = null, Square[] = null;
	
	public void goToFrom () {
		try {
			navigator.goTo(from);
		} catch (notCalibratedException e) {
			// TODO Auto-generated catch block
		}
	}
	
}
