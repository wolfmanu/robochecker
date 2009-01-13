package it.polito.Checkers;

import lejos.nxt.Button;
import lejos.nxt.SensorPort;
import lejos.nxt.addon.ColorSensor;

public class Color {
	private String name;
	private int Red, Green, Blue, Numero;
	private final static int k = 5;
	public final static int EMPTY = 0;
	public final static int WHITE = 1;
	public final static int BLACK = 2;
	public final static int WKING = 3;
	public final static int BKING = 4;
	public final static int BORDO3 = 5;
	
	private static Color[] vett=new Color[6];
	
	public Color() {
		this.name=null;
		this.Numero=0;
		this.Red=0;
		this.Green=0;
		this.Blue=0;
	}

	public Color(int n) {
		this.name=null;
		this.Numero=n;
		this.Red=0;
		this.Green=0;
		this.Blue=0;
	}
	
	public Color(int R, int G, int B) {
		this.name=null;
		this.Numero=0;
		this.Red=R;
		this.Green=G;
		this.Blue=B;
	}
	
	
	public void setColor(int R, int G, int B) {
		this.Red=R;
		this.Green=G;
		this.Blue=B;
	}
	
	static public Color getInstance(int col)
	{
		if (vett[col] == null)
			vett[col]= new Color(col);
		return vett[col];
	}
	
	public int getNumero() {
		return Numero;
	}

	public void setNumero(int numero) {
		Numero = numero;
	}

	public boolean equals(Object obj) {
		
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		
		Color other = (Color) obj;
		if (Blue < (other.Blue-k)  || Blue > (other.Blue+k)  || 
			Green < (other.Green-k)  || Green > (other.Green+k) ||
			Red < (other.Red-k)  || Red > (other.Red+k))
			
			return false;

		return true;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getRed() {
		return Red;
	}

	public void setRed(int red) {
		Red = red;
	}

	public int getGreen() {
		return Green;
	}

	public void setGreen(int green) {
		Green = green;
	}

	public int getBlue() {
		return Blue;
	}

	public void setBlue(int blue) {
		Blue = blue;
	}
	
	public int toInt() {
		return Numero;
	}
	public static void calibrate() {
		ColorSensor CS = new ColorSensor(SensorPort.S1);
		Color c;
		
		//setto EMPTY
		c=Color.getInstance(Color.EMPTY);
		System.out.println("EMPTY...");
		Button.waitForPress();
		c.setBlue(CS.getBlue());
		c.setRed(CS.getRed());
		c.setGreen(CS.getGreen());
		c.setName("empty");
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
		System.out.println("SETTING KWHITE OK");	
		
		//setto BORDO3
		c=Color.getInstance(Color.BORDO3);
		System.out.println("BORDO3 WHITE...");
		Button.waitForPress();
		c.setBlue(CS.getBlue());
		c.setRed(CS.getRed());
		c.setGreen(CS.getGreen());
		c.setName("bordo");
		System.out.println("SETTING BORDO3 OK");	
		
		System.out.println("SETTING COLOR OK");		
		Button.waitForPress();
				
	}
	
}
