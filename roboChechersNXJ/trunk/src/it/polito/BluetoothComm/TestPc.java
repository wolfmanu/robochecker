package it.polito.BluetoothComm;

import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommBluecove;
import lejos.pc.comm.NXTCommException;
import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTInfo;
import lejos.pc.tools.Console;


import java.io.*;

public class TestPc {
	
	public static void main(String [] args) throws Exception {
		
		char data;
		char reply;
		
		NXTComm nxtComm = null;
		nxtComm = NXTCommFactory.createNXTComm(NXTCommFactory.BLUETOOTH);

		
		NXTInfo[] nxtInfo = new NXTInfo[1];
		nxtInfo[0] = new NXTInfo(CommProtocol.BT_NAME,CommProtocol.BT_ADDR);
		System.out.println("Connecting to " + nxtInfo[0].name);

		boolean opened = false;
		try {
			opened = nxtComm.open(nxtInfo[0]); 
		} catch (NXTCommException e) {
			System.out.println("Exception from open");
		}
		if (!opened) {
			System.out.println("Failed to open " + nxtInfo[0].name);
			System.exit(1);
		}
		
		System.out.println("Connected to " + nxtInfo[0].name);
		
		InputStream is = nxtComm.getInputStream();
		OutputStream os = nxtComm.getOutputStream();
		DataInputStream dis = new DataInputStream(is);
		DataOutputStream dos = new DataOutputStream(os);

		Thread.sleep(5000);
		
		
		
		do {
			data = 'M';
			dos.write(data);
			dos.flush();
			reply = (char)dis.read();
		} while (data != 'E');
		
		dos.write(CommProtocol.START_CMD);
		dos.flush();
		data = (char)dis.read();
		System.out.println("On START_CMD :" + ((data == CommProtocol.ACK)? "ACK" : "NAK"));
		dos.write(CommProtocol.MOVE_CMD);
		dos.flush();
		data = (char)dis.read();
		System.out.println("On MOVE_CMD :" + ((data == CommProtocol.ACK)? "ACK" : "NAK"));
		dos.write('?');
		dos.flush();
		data = (char)dis.read();
		System.out.println("On '?' :" + ((data == CommProtocol.ACK)? "ACK" : "NAK"));
		dos.write(CommProtocol.EXIT_CMD);
		dos.flush();
		data = (char)dis.read();
		System.out.println("On EXIT_CMD :" + ((data == CommProtocol.ACK)? "ACK" : "NAK"));
		
		dos.close();
		dis.close();
		nxtComm.close();
		
	}
}