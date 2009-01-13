package it.polito.BluetoothComm;


import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommException;
import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTInfo;


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

		do {
			System.out.println("Press 'S' to Start");
			System.out.println("Press 'M' to Move");
			System.out.println("Press 'E' to Exit");
			data = (char)System.in.read();
			dos.write(data);
			dos.flush();
			System.in.skip(2);
			reply = (char)dis.read();
			if(reply == CommProtocol.ACK)
				System.out.println("OK!");
			else
				System.out.println("SOMETHING WRONG!");
		} while (data != 'E');
		
		dos.close();
		dis.close();
		nxtComm.close();
		
	}
}