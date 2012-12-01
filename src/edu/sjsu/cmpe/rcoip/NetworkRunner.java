package edu.sjsu.cmpe.rcoip;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import android.util.Log;

public class NetworkRunner implements Runnable {

	public static final String SERVERIP = "192.168.1.96";
	public static final int SERVERPORT = 2001;
	
	String data = "0xDEADBEEF";
	@Override
	public void run() 
	{
		try {
			// Retrieve the ServerName
			InetAddress serverAddr = InetAddress.getByName(SERVERIP);
			
			Log.d("UDP", "C: Connecting...");
			/* Create new UDP-Socket */
			DatagramSocket socket = new DatagramSocket();
			
			/* Prepare some data to be sent. */
			byte[] buf = data.getBytes();
			
			/* Create UDP-packet with 
			 * data & destination(url+port) */
			DatagramPacket packet = new DatagramPacket(buf, buf.length,	serverAddr, SERVERPORT);
			Log.d("UDP", "C: Sending: '" + new String(buf) + "'");
			
			/* Send out the packet */
			socket.send(packet);
			Log.d("UDP", "C: Sent.");
			Log.d("UDP", "C: Done.");
		} catch (Exception e) {
			Log.e("UDP", "C: Error", e);
		}
	}
}
