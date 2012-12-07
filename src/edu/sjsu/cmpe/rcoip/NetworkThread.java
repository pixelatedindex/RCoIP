package edu.sjsu.cmpe.rcoip;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

public class NetworkThread extends Thread 
{
	private static final String TAG = NetworkThread.class.getCanonicalName();
	private NetComm communicator;
	private WifiManager manager;
	private Handler parentHandler;
	private Handler threadHandler;
	private DatagramSocket socket;
	long delay = 8;
	private int TIMEOUT = 500;
	private int PORT_NO = 2000;
	private String IP_ADDR = "169.254.1.1";
	
	private boolean socketed = false;
	
	String data = "00000000";
	
	public NetworkThread(NetComm nc, WifiManager wm, Handler h)
	{
		this.communicator = nc;
		this.manager = wm;
		this.parentHandler = h;
	}
	
	public NetworkThread(WifiManager wm, Handler h)
	{
		this.manager = wm;
		this.parentHandler = h;
	}
	
	public boolean isSocketed()
	{
		return socketed;
	}
	
	public String getServer()
	{
		return IP_ADDR;
	}
	
	public int getPort()
	{
		return PORT_NO;
	}
	
	public Handler getHandler()
	{
		return threadHandler;
	}
	
	@Override
	public void run()
	{
		try
		{
			setup();
			Looper.prepare();
			Log.d(TAG, "Entering the loop");
			
			threadHandler = new Handler();
			
			Looper.loop();
			Log.d(TAG, "Leaving with grace");
		}
		catch (Exception e)
		{
			Log.d(TAG, "Crashed the party: " + e.toString());
		}
	}
	
	public synchronized void setup() throws SocketException
	{
		Log.i(TAG, "Setting up, socketing");
		socket = new DatagramSocket(PORT_NO);
		socket.setBroadcast(true);
	    socket.setSoTimeout(TIMEOUT);
	}
	
	public synchronized void teardown()
	{
		threadHandler.post(new Runnable()
		{
			public void run()
			{
				Looper.myLooper().quit();
			}
		});
	}
	
	public synchronized void send(String values)
	{
		data = values;
		threadHandler.postDelayed(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					/* Prepare some data to be sent. */
					byte[] buf = data.getBytes();
					
					/* Create UDP-packet with 
					 * data & destination(url+port) */
					DatagramPacket packet = new DatagramPacket(buf, buf.length,	InetAddress.getByName(IP_ADDR), PORT_NO);
					Log.d("UDP", "C: Sending: '" + new String(buf) + "'");
					
					/* Send out the packet */
					socket.send(packet);
					Log.d("UDP", "C: Sent.");
				}
				catch (Exception e)
				{
					Log.d(TAG, "Sending threw an exception: " + e.toString());
				}
				done();
			}
		}, delay);
	}
	
	private void done()
	{
		if(communicator != null)
		{
			communicator.Communicate();
		}
	}
}
