package it.polito.roboCheckers;
import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.SensorPort;
import lejos.nxt.addon.ColorSensor;


public class calibrateTest {

	public static void main(String[] args) throws Exception {
		boolean exit = false;
		CheckersNavigator navigator= SimpleNavigator.getInstance();
		ColorSensor CS = new ColorSensor(SensorPort.S1);
		navigator.setSpeed(300, 2000);
		navigator.calibrate();
		navigator.setSpeed(1000, 1000);
		int[] X = {0,1,2,3,4,5,6,7};
		int[] Y = {0,1,2,3,4,5,6,7};
		for (int i=0; i<8 && !exit; i++) {
			if (i%2==0) 
				for (int j=0; j<8 && !exit; j++) {
					navigator.goTo(X[j],Y[i]);
					LCD.drawString("color", 0, 7);
					LCD.drawInt((int)CS.getColorNumber(),7,7);
					LCD.refresh();
					Thread.sleep(5000);
					if (Button.ESCAPE.isPressed())
						exit = true;	
				}
			else
				for (int j=7; j>=0 && !exit; j--) {
					navigator.goTo(X[j],Y[i]);
					LCD.drawString("color", 0, 7);
					LCD.drawInt((int)CS.getColorNumber(),7,7);
					LCD.refresh();
					Thread.sleep(5000);
					if (Button.ESCAPE.isPressed())
						exit = true;	
				}
		}
		navigator.goHome();
	}

}
