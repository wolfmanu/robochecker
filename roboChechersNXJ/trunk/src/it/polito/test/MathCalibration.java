package it.polito.test;

import it.polito.Navigation.MathNavigator;
import lejos.nxt.Button;
import lejos.nxt.SensorPort;
import lejos.nxt.Sound;
import lejos.nxt.addon.ColorSensor;

public class MathCalibration {

	private static final ColorSensor CS = new ColorSensor(SensorPort.S1);
	private static final int WHITE = 17;
	private static final int BLACK = 0;
	
	public static void main(String[] args) throws Exception {
		MathNavigator navigator = MathNavigator.getInstance();
		
		navigator.setSpeed(1000, 1000);
		
		while(!Button.ENTER.isPressed()){
			navigator.left();
			Thread.sleep(100);
		}
		
		while(CS.getColorNumber()!= WHITE){
			System.out.println("Need WHITE!");
			Sound.buzz();
			Thread.sleep(100);
		}
		
		System.out.println("Reaching BLACK...");
		
		while(CS.getColorNumber()!= BLACK){
			navigator.right();
			Thread.sleep(100);
		}
		
		navigator.getMotorB().stop();
		navigator.setSpeed(200, 200);
		navigator.getMotorB().resetTachoCount();
		Thread.sleep(100);
		System.out.println("START!");
		
		do {
			navigator.right();
			Thread.sleep(100);
		} while(CS.getColorNumber()!= WHITE);

		System.out.println("BLACK ENDED.");
		Thread.sleep(100);
		
		do {
			navigator.right();
			Thread.sleep(100);
		} while(CS.getColorNumber()!= BLACK);

		System.out.println("WHITE ENDED.");
		Thread.sleep(100);
		
		do {
			navigator.right();
			Thread.sleep(100);
		} while(CS.getColorNumber()!= WHITE);
		
		System.out.println("BLACK ENDED.");
		navigator.getMotorB().stop();
		Thread.sleep(100);
		
		
		int angle180 = navigator.getMotorB().getTachoCount();
		
		System.out.println("180� Angle: " + angle180);
		
		Button.waitForPress();
		
	}
	
}
