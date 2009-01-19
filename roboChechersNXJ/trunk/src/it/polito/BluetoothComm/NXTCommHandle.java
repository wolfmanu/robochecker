package it.polito.BluetoothComm;

import it.polito.BluetoothComm.CommProtocol;
import it.polito.util.HumanInput;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lejos.nxt.Button;
import lejos.nxt.comm.Bluetooth;
import lejos.nxt.comm.NXTConnection;

public class NXTCommHandle implements HumanInput{
	
	private static NXTCommHandle nxtCommHandle = null;
	private NXTConnection nxtConnection = null;
	private DataInputStream dis = null;
	private DataOutputStream dos = null;
	
	public static NXTCommHandle getInstance (){
		if(nxtCommHandle == null)
			nxtCommHandle = new NXTCommHandle();
		return nxtCommHandle;
	}
	
	private NXTCommHandle(){
		
	}
	
	private char receiveCommand() throws IOException {
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
	
	public boolean waitForMove (boolean locking) {
		if(locking){
			try {
				while( receiveCommand() != CommProtocol.MOVE_CMD );
			} catch (IOException e) {
				while( !Button.ENTER.isPressed() );
			}
			return true;
		} else {
			try {
				return (receiveCommand() == CommProtocol.MOVE_CMD);
			} catch (IOException e) {
				return Button.ENTER.isPressed();
			}
		}
	}
	
	public boolean waitForStart (boolean locking) {
		if(locking){
			try {
				while( receiveCommand() != CommProtocol.START_CMD );
			} catch (IOException e) {
				while( !Button.ENTER.isPressed() );
			}
			return true;
		} else {
			try {
				return (receiveCommand() == CommProtocol.START_CMD);
			} catch (IOException e) {
				return Button.ENTER.isPressed();
			}
		}
	}
	
	private void sendACK () throws IOException{
		dos.write(CommProtocol.ACK);
		dos.flush();
	}
	
	private void sendNAK () throws IOException{
		dos.write(CommProtocol.NAK);
		dos.flush();
	}

	public void connect() {
		nxtConnection = Bluetooth.waitForConnection();
		dis = nxtConnection.openDataInputStream();
		dos = nxtConnection.openDataOutputStream();
	}
	
	public void disconnect() throws IOException {
		dis.close();
		dos.close();
		nxtConnection.close();
	}

	
	public void destroy() {
		try {
			disconnect();
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}
	
	}

	
	public void init() {
		connect();
		
	}
	
}
