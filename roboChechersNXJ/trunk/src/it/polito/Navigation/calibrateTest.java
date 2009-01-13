package it.polito.Navigation;
import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.SensorPort;
import lejos.nxt.addon.ColorSensor;


public class calibrateTest {

	public static void main(String[] args) throws Exception {
		/*
		double l = 16.0;
		double r = 11.0;
		double alpha = 0.2628;
		double betaarg = l/(2*r*java.lang.Math.sin(alpha/2));
		double beta = 2*java.lang.Math.acos(betaarg);
		double gamma = (java.lang.Math.PI - alpha - beta)/2;
		double Cx = r*java.lang.Math.sin((alpha+beta)/2);
		
		System.out.println("alpha: " + alpha);
		System.out.println("betaarg: " + betaarg);
		System.out.println("beta: " + beta);
		System.out.println("gamma: " + gamma);
		System.out.println("Cx: " + Cx);
		
		Button.waitForPress();
		
		*/
		
		boolean exit = false;
		CheckersNavigator navigator= MathNavigator.getInstance();
		ColorSensor CS = new ColorSensor(SensorPort.S1);
		navigator.setSpeed(300, 1000);
		navigator.calibrate();
		navigator.setSpeed(1000, 1000);
		int[] X = {0,1,2,3,4,5,6,7};
		int[] Y = {0,1,2,3,4,5,6,7};
		for (int i=0; i<8 && !exit; i++) {
			navigator.goTo(0,Y[i]);
			if (Button.ESCAPE.isPressed())
				exit = true;
		}
		for (int i=0; i<8 && !exit; i++) {
			navigator.goTo(X[i],7);
			if (Button.ESCAPE.isPressed())
				exit = true;
		}
		for (int i=7; i>=0 && !exit; i--) {
			navigator.goTo(7,Y[i]);
			if (Button.ESCAPE.isPressed())
				exit = true;
		}
		for (int i=7; i>=0 && !exit; i--) {
			navigator.goTo(X[i],0);
			if (Button.ESCAPE.isPressed())
				exit = true;
		}
		for (int i=0; i<8 && !exit; i++) {
			if (i%2==0) 
				for (int j=0; j<8 && !exit; j++) {
					navigator.goTo(X[j],Y[i]);
//					LCD.drawString("color", 0, 7);
//					LCD.drawInt((int)CS.getColorNumber(),7,7);
//					LCD.refresh();
					//Thread.sleep(1500);
					if (Button.ESCAPE.isPressed())
						exit = true;	
				}
			else
				for (int j=7; j>=0 && !exit; j--) {
					navigator.goTo(X[j],Y[i]);
//					LCD.drawString("color", 0, 7);
//					LCD.drawInt((int)CS.getColorNumber(),7,7);
//					LCD.refresh();
					//Thread.sleep(1500);
					if (Button.ESCAPE.isPressed())
						exit = true;	
				}
		}
		navigator.goHome();
	}

}
