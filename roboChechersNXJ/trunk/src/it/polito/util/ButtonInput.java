package it.polito.util;

import it.polito.util.ButtonInput;
import it.polito.util.HumanInput;
import lejos.nxt.Button;

public class ButtonInput implements HumanInput {
	static HumanInput HI = null;
	
	public boolean waitForMove(boolean locking) {
		if(locking){
			while( !Button.ENTER.isPressed() );
			return true;
		} else {
			return Button.ENTER.isPressed();
		}
	}

	public boolean waitForStart(boolean locking) {
		if(locking){
			while( !Button.ENTER.isPressed() );
			return true;
		} else {
			return Button.ENTER.isPressed();
		}
	}

	
	public void init() {}
	public void destroy() {}

	public static HumanInput getInstance() {
		if (HI == null)
			HI = new ButtonInput();
		return HI;
		
	}

}
