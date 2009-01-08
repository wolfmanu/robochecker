package it.polito.testremotecontrol;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import lejos.nxt.comm.*;


public class TestNxt {

	public static void main(String [] args) throws Exception {
		    
		System.out.println("Waiting for connection.");
		NXTConnection nxtConnection = Bluetooth.waitForConnection();
		System.out.println("Connected.");
		Thread.sleep(2000);
		
		DataInputStream dis = nxtConnection.openDataInputStream();
		DataOutputStream dos = nxtConnection.openDataOutputStream();
		char data;
		
		do{
			data = dis.readChar();
			switch(data) {
				case TestProtocol.EXIT_CMD:
					System.out.println("EXIT...");
					dos.writeChar(TestProtocol.ACK);
					break;
				case TestProtocol.START_CMD:
					System.out.println("START!");
					dos.writeChar(TestProtocol.ACK);
					break;
				case TestProtocol.MOVE_CMD:
					System.out.println("MOVE!");
					dos.writeChar(TestProtocol.ACK);
					break;
				default:
					System.out.println("Not supported!");
					dos.writeChar(TestProtocol.NAK);
					break;
			}
			dos.flush();
		}while(data != TestProtocol.EXIT_CMD);

		dis.close();
		dos.close();
		nxtConnection.close();
		System.out.println("Disconnected.");
			
	  }

}
