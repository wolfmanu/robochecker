package it.polito.Navigation;
import lejos.nxt.*;
public class ArmSuGiu {


		/**
		 * @param args
		 */
		public static void main(String[] args) throws Exception {
			ArmControllerThr arm = ArmControllerThr.getInstance();
			final Motor MA = new Motor(MotorPort.A);
		
			MA.forward();
			
			while (true) {
				LCD.drawInt(Motor.C.getTachoCount(), 0, 0);
				Motor.C.setSpeed(500);
				if (Button.LEFT.isPressed()) {
					arm.up();
				}
				if (Button.RIGHT.isPressed()) {
					arm.down();
				}
				if (Button.ESCAPE.isPressed()) {
					break;
				}
				if (Button.ENTER.isPressed()) {
					Motor.C.stop();
				}
				//Thread.sleep(10);
			}
		}

	}

