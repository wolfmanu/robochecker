import lejos.nxt.LCD;
import lejos.nxt.Motor;
/**
 * Move the robot over the select chechers's cell
 * @author davide
 * 
 */
public class ChechersNavigator {

	private int x=5;
	private int y=0;
	int[] posx={-7400, -5500, -4250, -2800, -1500, 0 , 1400, 2600};
	int[] posy={0, 1000, 2000, 3000, 4000, 5000 , 6000, 7000};
	int[] dely={2000,1000, 500, 100, 0, 0, 0, 200};

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void goTo(int newX, int newY) {
		//controllo estremi
		if (newX<0) newX=0;
		if (newY<0) newY=0;
		if (newX>7) newX=7;
		if (newY>7) newY=0;
		//printing what i'm doing on lcd
		LCD.clear();
		
		LCD.drawString("from:", 0, 0);
		LCD.drawInt(this.x, 5, 0);
		LCD.drawInt(this.y, 7, 0);
		LCD.drawString("to:", 0, 1);
		LCD.drawInt(newX, 5, 1);
		LCD.drawInt(newY, 7, 1);
		LCD.refresh();
		
		//controlla se deve recuperare il gioco
		if (newX < this.x){
			Motor.B.rotateTo(posx[x]-1000,true);
			LCD.drawString("Gioco X", 0, 2);
		}else{
			Motor.B.rotateTo(posx[x],true);
		}
		if (newY < this.y){
			Motor.A.rotateTo(posy[y]+dely[x]-1000,true);
			LCD.drawString("Gioco Y", 0, 3);
		}else{
			Motor.A.rotateTo(posy[y]+dely[x],true);
		}
		waitForMotors(new Motor[]{Motor.A, Motor.B});
		
		Motor.A.rotateTo(posy[y]+dely[x],true);
		Motor.B.rotateTo(posx[x],true);
		
		waitForMotors(new Motor[]{Motor.A, Motor.B});
		
	}

	public boolean isMoving() {
		return Motor.A.isMoving() || Motor.B.isMoving();
	}

	public void setSpeed(int speedA, int speedB) {
		Motor.A.setSpeed(speedA);
		Motor.B.setSpeed(speedB);
	}
	
	/***
	 * Wait for motor(s) in motorList to stop
	 * TODO: CHECK IF WORK
	 * @throws InterruptedException 
	 */
	private void waitForMotors(Motor[] motorList){
		System.out.println("Waiting for motors");
		for (int i=0; i < motorList.length; i++){
			while (motorList[i].isMoving()){
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					return;
				}
			}
		}
	}

}
