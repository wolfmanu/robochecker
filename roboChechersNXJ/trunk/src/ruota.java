import it.polito.roboCheckers.ChechersNavigator;
import lejos.nxt.*;


public class ruota  {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		
		int x=0;
		int y=0;
		ChechersNavigator navigator= ChechersNavigator.getInstance();
		
		navigator.setSpeed(1000,1000);
		// TODO Auto-generated method stub
		Button.waitForPress();
		navigator.goTo(0,0);
		
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
	
	/***
	 * Wait for motor(s) passed to stop
	 * TODO: CHECK IF WORK
	 * @author davide
	 * @throws InterruptedException 
	 */
	public static void waitForMotors(Motor[] motorList) throws InterruptedException{
		LCD.drawString("Waiting for motors",0,0);
		for (int i=0; i < motorList.length; i++){
			while (motorList[i].isMoving()){
				Thread.sleep(50);
			}
		}
	}

}
