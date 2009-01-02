import lejos.nxt.*;
public class ArmSuGiu {


		/**
		 * @param args
		 */
		public static void main(String[] args) throws Exception {

			while (true) {
				LCD.drawInt(Motor.C.getTachoCount(), 0, 0);
				Motor.C.setSpeed(200);
				if (Button.LEFT.isPressed()) {
					Motor.C.forward();
				}
				if (Button.RIGHT.isPressed()) {
					Motor.C.backward();
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

