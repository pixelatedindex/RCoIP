package edu.sjsu.cmpe.rcoip;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

public class JoyServer extends Thread {

	public static final String SERVERIP = "127.0.0.1"; // 'Within' the emulator!
	public static final int SERVERPORT = 4444;
	private static final String TAG = JoyServer.class.getCanonicalName();
	
	private Handler serverHandler;
	private DatagramSocket servsock;
	private byte[] rxbuf = new byte[2048];
	
	@Override
	public void run() {
		makeJoy();
		Looper.prepare();
		serverHandler = new Handler();
		Log.d(TAG, "Server has prepared Looper");
		
		Looper.loop();
		Log.d(TAG, "Server has entered the infinite loop");
		
		getProfit();
	}

	public synchronized void makeJoy()
	{
		try
		{
			InetAddress serverAddr = InetAddress.getByName(SERVERIP);
			servsock = new DatagramSocket(SERVERPORT, serverAddr);
		}
		catch(Exception e)
		{
			Log.d(TAG, "Error making joy :( => " + e.toString());
		}
	}
	
	public synchronized void getProfit()
	{
		String rxdata = "";
		//DatagramPacket packet;
		serverHandler.post(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					/* Prepare a UDP-Packet that can 
					 * contain the data we want to receive */
					DatagramPacket packet = new DatagramPacket(rxbuf, rxbuf.length);
					Log.d("UDP", "S: Receiving...");

					/* Receive the UDP-Packet */
					servsock.receive(packet);
					Log.d("UDP", "S: Received: '" + new String(packet.getData()) + "'");
				}
				catch(Exception e)
				{
					Log.e("UDP", "S: Error", e);
				}
			}
		});
	}
}
