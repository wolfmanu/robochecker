package it.polito.testControlUsb;
import lejos.nxt.*;
import lejos.nxt.addon.ColorSensor;
import lejos.nxt.comm.*;
import java.io.*;

public class TestNxt {
static ColorSensor CS = null;
	  public static void main(String [] args) throws Exception {
		    String connected = "Connected";
		    String waiting = "Waiting...";
		    String closing = "Closing...";
		    CS = new ColorSensor(SensorPort.S1);


		      LCD.drawString(waiting,0,0);
		      NXTConnection connection = USB.waitForConnection(); 
		      LCD.clear();
		      LCD.drawString(connected,0,0);

		      DataInputStream dis = connection.openDataInputStream();
		      DataOutputStream dos = connection.openDataOutputStream();
		      
		      int num = dis.readInt();		      
		      LCD.clear();
		      LCD.drawInt(num,0,0);
		      
		      int color = (int)CS.getColorNumber();
		      LCD.drawInt(color,0,1);
		      dos.write(color);
		      
		      dis.close();
		      dos.close();
	  }

}
