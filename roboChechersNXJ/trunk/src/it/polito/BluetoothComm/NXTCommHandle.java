package it.polito.BluetoothComm;

import it.polito.BluetoothComm.CommProtocol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lejos.nxt.comm.Bluetooth;
import lejos.nxt.comm.NXTConnection;

public class NXTCommHandle {
	
	private static NXTConnection nxtConnection = null;
	private static DataInputStream dis = null;
	private static DataOutputStream dos = null;
	
	public static NXTConnection getInstance (){
		nxtConnection = Bluetooth.waitForConnection();
		dis = nxtConnection.openDataInputStream();
		dos = nxtConnection.openDataOutputStream();
		return nxtConnection;
	}
	
	private static char receiveCommand() throws IOException {
		char data;
		data = (char)dis.read();
		switch(data){
			case CommProtocol.START_CMD:
				sendACK();
				break;
			case CommProtocol.MOVE_CMD:
				sendACK();
				break;
			default:
				sendNAK();
				break;
		}
		return data;
	}
	
	public static boolean waitForMove () throws IOException{
		if (receiveCommand() == CommProtocol.MOVE_CMD)
			return true;
		return false;
	}
	
	public static boolean waitForStart () throws IOException{
		if (receiveCommand() == CommProtocol.START_CMD)
			return true;
		return false;
	}
	
	private static void sendACK () throws IOException{
		dos.write(CommProtocol.ACK);
		dos.flush();
	}
	
	private static void sendNAK () throws IOException{
		dos.write(CommProtocol.NAK);
		dos.flush();
	}
	
}
