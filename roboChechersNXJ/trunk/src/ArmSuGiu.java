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

					Motor.C.rotate(10);

				}
				if (Button.RIGHT.isPressed()) {
					Motor.C.rotate(-10);
				}
				if (Button.ESCAPE.isPressed()) {
					break;
				}
				//Thread.sleep(10);

			}
		}

	}

