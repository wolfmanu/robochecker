import lejos.nxt.Button;
import lejos.nxt.Sound;


public class calibrateTest {

	public static void main(String[] args) throws Exception {
		ChechersNavigator navigator= ChechersNavigator.getInstance();
		navigator.setSpeed(500, 2000);
		navigator.calibrate();
		navigator.setSpeed(1000, 1000);
		int[] X = {0,1,2,3,4,5,6,7};
		int[] Y = {0,1,2,3,4,5,6,7};
		int x=5;
		int y=0;
		int i=0;
		navigator.goTo(x, y);
		Thread.sleep(5000);
		while (i<8){
			navigator.goTo(X[i], Y[i]);
			i++;
			
		/*while (true) {
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
			}*/
			
			if (Button.ESCAPE.isPressed()) 
				break;
			Thread.sleep(5000);
		}
	}

}
