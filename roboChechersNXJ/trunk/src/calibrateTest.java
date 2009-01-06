import lejos.nxt.Button;
import lejos.nxt.Sound;


public class calibrateTest {

	public static void main(String[] args) throws Exception {
		ChechersNavigator navigator= ChechersNavigator.getInstance();
		navigator.calibrate();
		navigator.setSpeed(1000, 1000);
		navigator.goTo(0, 0);
		int x=0;
		int y=0;
		while (true) {
			if (Button.LEFT.isPressed()) {
				x++;
				if (x > 7)
					x=0;
				navigator.goTo(x, y);
				
			}
			if (Button.RIGHT.isPressed()) {
				y++;
				if (y > 7)
					y=0;
				
				navigator.goTo(x, y);
			}
			if (Button.ESCAPE.isPressed()) {
				break;
			}
			Thread.sleep(100);
		}
	}

}
