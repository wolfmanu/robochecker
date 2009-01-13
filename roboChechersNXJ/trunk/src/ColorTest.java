import lejos.nxt.Button;
import lejos.nxt.SensorPort;
import lejos.nxt.addon.ColorSensor;


public class ColorTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ColorSensor CS = new ColorSensor(SensorPort.S1);
		while (Button.ESCAPE.isPressed()) {
			System.out.println("color: "+CS.getColorNumber());
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
			}
		}
	}

}
