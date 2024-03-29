package javax.bluetooth;

import it.polito.BluetoothComm.CommProtocol;

import java.io.IOException;

import javax.bluetooth.RemoteDevice;
import javax.microedition.io.Connection;

import lejos.nxt.comm.Bluetooth;
import lejos.nxt.comm.BTConnection;

/**
 * Represents a remote Bluetooth device.
 * 
 * @author Lawrie Griffiths
 *
 */
public class RemoteDevice {

	private byte[] addr = new byte[7];
	
	private String friendlyName;
	
	// !! Delete next two once redundant:
	private char[] friendlyNameCAr = new char[16];
	private int friendlyNameLen = 0;
	private byte[] deviceClass = new byte[4];
	
	/**
	 * Note: The standard JSR 82 method for obtaining a RemoteDevice
	 * uses a String rather than byte[]. Protected so shouldn't matter.
	 * @param addr
	 */
	protected RemoteDevice(byte [] addr) {
		// Set Address:
		this.addr = addr;
		
		// !! Set device class: Is this going to be used?
		// (Not part of JSR 82)
		
		// Set Friendly name:
		this.getFriendlyName(true); // Refresh name
	}
	
	
	// Temporary FIX
	// TODO Dynamic byte[] generation from String
	protected RemoteDevice(String address) {
		// System.out.println("Remote BT Address: " + address);
		this.addr = CommProtocol.bBT_ADDR;

		// Do Not Set Friendly name !
		this.getFriendlyName(false); // Refresh name
	}
	
	// !! DEV NOTES: Remove this eventually.
	public RemoteDevice(char[] friendlyNameCharArray, int len, byte[] deviceAddr, byte [] devclass) {
		setFriendlyName(friendlyNameCharArray, len);
		setDeviceAddr(deviceAddr);
		setDeviceClass(devclass);
	}

	/*
	 * UNIMPLEMENTED
	 * DEV NOTES: Internally this just casts the Connection object 
	 * into a BTConnection object. However, how to pull information 
	 * from it, such as friendly name, address, etc... ??
	 * Solution: Add address to BTConnection class?
	 */
	public static RemoteDevice getRemoteDevice(Connection conn) throws IOException {
		BTConnection btc = (BTConnection)conn;
		return new RemoteDevice(btc.getAddress());
	}
	
	public void setDeviceAddr(byte[] deviceAddr) {
		for(int i=0;i<7;i++) addr[i] = deviceAddr[i];		
	}
	
	public byte[] getDeviceAddr() {
		return addr;
	}
	
	/*
	 * DELETE THIS:
	 */
	public void setFriendlyName(char[] friendlyNameCharArray, int len) {
		for(int i=0; i<len; i++) this.friendlyNameCAr[i] = friendlyNameCharArray[i];
		this.friendlyNameLen = len;

	}
	
	/**
	 * 
	 * @param alwaysAsk true causes the method to contact the remote device for the name. false and it will use the known name. 
	 * @return the friendly name
	 */
	public String getFriendlyName(boolean alwaysAsk) {
		
		if(alwaysAsk) {
			String name = Bluetooth.lookupName(addr);
			// NOTE: friendlyNameCAr array length changes to < 16:
			friendlyNameCAr = name.toCharArray();
			friendlyNameLen = name.length();
		}
		return new String(this.friendlyNameCAr, 0 ,this.friendlyNameLen);
	}
	
	/*
	 * !! DELETE THIS. UNUSED. Then move to all String usage for name.
	 * Get the FriendlyName of the BTRemoteDevice as Char-Array 
	 * @params: 
	 */
	public int getFriendlyName(char[] friendlyNameCharArray) {
		for(int i=0; i<friendlyNameLen; i++) friendlyNameCharArray[i] = this.friendlyNameCAr[i];
		return friendlyNameLen;
	}
	
	/*
	 * REMOVE EVENTUALLY
	 * DEV NOTES: This is not a standard JSR 82 method.
	 */
	public void setDeviceClass(byte[] devclass) {
		for(int i=0;i<4;i++) deviceClass[i] = devclass[i];
	}
	
	public String getBluetoothAddress() {
		return Bluetooth.addressToString(addr);
	}
	
	/**
	 * Determines if two RemoteDevices are equal. If they both have the same BT address
	 * then they are considered equal.
	 */
	public boolean equals(Object obj) {
		return obj != null && obj instanceof RemoteDevice && ((RemoteDevice) obj).getBluetoothAddress().equals(getBluetoothAddress());
	}
	
	/*
	 * REMOVE EVENTUALLY
	 * DEV NOTES: This is not a standard JSR 82 method.
	 */
	public byte[] getDeviceClass() {
		return deviceClass;
	}
}