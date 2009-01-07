package it.polito.testControlUsb;
import lejos.nxt.*;
import lejos.nxt.addon.ColorSensor;
import lejos.nxt.comm.*;
import lejos.nxt.remote.*;

import java.io.*;

public class TestPc {
	public static void main(String [] args) throws Exception {

		USBConnection mUSBConn = new USBConnection(USBConnection.LCP);
		
		DataInputStream dis = mUSBConn.openDataInputStream();
		DataOutputStream dos = mUSBConn.openDataOutputStream();
		
		System.out.println("Inviato: 5");
		dos.writeInt(5);
		int retVal = dis.readInt();
		
		System.out.println("Sensore: " + retVal);

		dis.close();
		dos.close();
  }


}
