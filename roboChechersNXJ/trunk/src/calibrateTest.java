import lejos.nxt.Sound;


public class calibrateTest {

	public static void main(String[] args) throws Exception {
		ChechersNavigator navigator= ChechersNavigator.getInstance();
		navigator.calibrate();
		navigator.setSpeed(1000, 1000);
		navigator.goTo(0, 0);
		navigator.goTo(7, 7);
		navigator.goTo(3, 3);
		navigator.goTo(7, 0);
		navigator.goTo(0, 7);
		navigator.goTo(5, 0);
	}

}
