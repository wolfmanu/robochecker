import it.polito.Checkers.*;
import it.polito.Navigation.MathNavigator;
import lejos.nxt.*;
import lejos.nxt.addon.*;


public class ColorCalibration {
	
	private static final ColorSensor CS = new ColorSensor(SensorPort.S1);

	public static void ColorCalibration() {
		Color c;
		
		//setto EMPTY
		c=Color.getInstance(Color.EMPTY);
		System.out.println("EMPTY...");
		Button.waitForPress();
		c.setBlue(CS.getBlue());
		c.setRed(CS.getRed());
		c.setGreen(CS.getGreen());
		c.setName("empty");
		c.setNumero(Color.EMPTY);
		System.out.println("SETTING EMPTY OK");	
		Button.waitForPress();
		
		//setto WHITE
		c=Color.getInstance(Color.WHITE);
		System.out.println("WHITE...");
		Button.waitForPress();
		c.setBlue(CS.getBlue());
		c.setRed(CS.getRed());
		c.setGreen(CS.getGreen());
		c.setName("white");
		c.setNumero(Color.WHITE);
		System.out.println("SETTING WHITE OK");	
		Button.waitForPress();
				
		//setto BLACK
		c=Color.getInstance(Color.BLACK);
		System.out.println("BLACK...");
		Button.waitForPress();
		c.setBlue(CS.getBlue());
		c.setRed(CS.getRed());
		c.setGreen(CS.getGreen());
		c.setName("black");
		c.setNumero(Color.BLACK);
		System.out.println("SETTING BLACK OK");	
		Button.waitForPress();
		
		//setto BKING
		c=Color.getInstance(Color.BKING);
		System.out.println("KING BLACK...");
		Button.waitForPress();
		c.setBlue(CS.getBlue());
		c.setRed(CS.getRed());
		c.setGreen(CS.getGreen());
		c.setName("king black");
		c.setNumero(Color.BKING);
		System.out.println("SETTING KBLACK OK");	
		Button.waitForPress();
		
		//setto WKING
		c=Color.getInstance(Color.WKING);
		System.out.println("KING WHITE...");
		Button.waitForPress();
		c.setBlue(CS.getBlue());
		c.setRed(CS.getRed());
		c.setGreen(CS.getGreen());
		c.setName("king white");
		c.setNumero(Color.WKING);
		System.out.println("SETTING KWHITE OK");	
		System.out.println("SETTING COLOR OK");		
		Button.waitForPress();
				
	}
	
	
	
	
}
