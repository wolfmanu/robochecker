import it.polito.Navigation.ArmController;
import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.SensorPort;
import lejos.nxt.addon.ColorSensor;


public class ColorTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ArmController arm = ArmController.getInstance();
		arm.down();
		ColorSensor CS = new ColorSensor(SensorPort.S1);
		while (true) {
			LCD.clearDisplay();
			LCD.drawString("Red: "+CS.getRed(), 0, 0);
			LCD.drawString("Green: "+CS.getGreen(), 0, 0);
			LCD.drawString("Blue: "+CS.getBlue(), 0, 0);
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
			}
			if (Button.ESCAPE.isPressed())
				break;
		}
	}

}
