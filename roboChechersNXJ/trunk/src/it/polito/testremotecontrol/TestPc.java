package it.polito.testremotecontrol;
import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTInfo;

import java.io.*;

public class TestPc {
	public static void main(String [] args) throws Exception {
		
		char data;
		
		NXTComm nxtComm = NXTCommFactory.createNXTComm(NXTCommFactory.BLUETOOTH);

		NXTInfo nxtInfo = new NXTInfo(TestProtocol.BT_NAME, TestProtocol.BT_ADDR);
		
		nxtComm.open(nxtInfo);
		
		InputStream is = nxtComm.getInputStream();
		OutputStream os = nxtComm.getOutputStream();
		DataInputStream dis = new DataInputStream(is);
		DataOutputStream dos = new DataOutputStream(os);
		
		dos.writeChar(TestProtocol.START_CMD);
		data = dis.readChar();
		System.out.println("On START_CMD :" + ((data == TestProtocol.ACK)? "ACK" : "NAK"));
		dos.writeChar(TestProtocol.MOVE_CMD);
		data = dis.readChar();
		System.out.println("On MOVE_CMD :" + ((data == TestProtocol.ACK)? "ACK" : "NAK"));
		dos.writeChar('?');
		data = dis.readChar();
		System.out.println("On '?' :" + ((data == TestProtocol.ACK)? "ACK" : "NAK"));
		dos.writeChar(TestProtocol.EXIT_CMD);
		data = dis.readChar();
		System.out.println("On EXIT_CMD :" + ((data == TestProtocol.ACK)? "ACK" : "NAK"));
		
		dos.close();
		dis.close();
		nxtComm.close();
		
	}
}