package it.polito.BluetoothComm;

public class CommProtocol {
	public static final String BT_NAME = "NXT";
	public static final String BT_ADDR = "00:16:53:05:6e:33";
	public static final byte[] bBT_ADDR = {(byte)0x00, (byte)0x16, (byte)0x53, (byte)0x05, (byte)0x6e, (byte)0x33};
	
	public static final char EXIT_CMD = 'E';
	public static final char START_CMD = 'S';
	public static final char MOVE_CMD = 'M';
	
	public static final char ACK = 6;
	public static final char NAK = 21;
}
