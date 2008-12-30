import lejos.navigation.Pilot;
import lejos.navigation.TachoNavigator;
import lejos.nxt.*;


public class ruota  {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		
		int x=5;
		int y=0;
		int[] posx={-7400, -5500, -4250, -2800, -1500, 0 , 1400, 2600};
		int[] posy={0, 1000, 2000, 3000, 4000, 5000 , 6000, 7000};
		int[] dely={2000,1000, 500, 100, 0, 0, 0, 200};
		
		Motor.B.setSpeed(1000);
		// TODO Auto-generated method stub
		while (true) {
			LCD.drawInt(Motor.A.getTachoCount(),0,0);
			LCD.drawInt(x, 0, 1);
			LCD.drawInt(posx[x], 0, 2);
			LCD.drawInt(posy[y], 0, 3);
			LCD.drawInt(posy[y]+dely[x], 0, 4);
			LCD.refresh();
			if (Button.LEFT.isPressed()) {
				x++;
				if (x > 7)
					x=0;
				
				Motor.A.rotateTo(posy[y]+dely[x],true);
				Motor.B.rotateTo(posx[x],true);
				waitForMotors(new Motor[]{Motor.A, Motor.B});
			}
			if (Button.RIGHT.isPressed()) {
				y++;
				if (y > 7)
					y=0;
				
				Motor.A.rotateTo(posy[y]+dely[x],true);
				Motor.B.rotateTo(posx[x],true);
				waitForMotors(new Motor[]{Motor.A, Motor.B});
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
		System.out.println("Waiting for motors");
		for (int i=0; i < motorList.length; i++){
			while (motorList[i].isMoving()){
				Thread.sleep(50);
			}
		}
	}

}
