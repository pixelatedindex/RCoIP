package edu.sjsu.cmpe.rcoip;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends Activity implements NetComm
{
		//TextViews for debugging purposes
	
        //private TextView txtXL, txtYL;
        //private TextView txtXR, txtYR;
	
		
        private DualJoystickView joystick;
        private WifiManager manager;
        public Handler mainHandler;
        private NetworkThread nt;
        
        public String LeftPan; 
        public String LeftTilt; 
        public String RightPan; 
        public String RightTilt;
    	long delay = 16;
        
        @Override
        protected void onCreate(Bundle savedInstanceState) 
        {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);

                //txtXL = (TextView)findViewById(R.id.TextViewX1);
                //txtXR = (TextView)findViewById(R.id.TextViewX2);
                //txtYL = (TextView)findViewById(R.id.TextViewY1);
                //txtYR = (TextView)findViewById(R.id.TextViewY2);
                joystick = (DualJoystickView)findViewById(R.id.dualjoystickView);
                joystick.setOnJostickMovedListener(_listener, listener_);
                manager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
                nt = new NetworkThread(this, manager, mainHandler);
                nt.start();
                //new Thread(new JoyServer()).start();
        }
        
        private JoystickMovedListener _listener = new JoystickMovedListener() 
        {
            @Override
            public void OnMoved(int pan, int tilt) 
            {
            		LeftPan = Integer.toHexString(Math.abs(pan+127));
            		LeftTilt = Integer.toHexString(Math.abs(tilt-127));
            		
                   // txtXL.setText("\t" + Math.abs(pan+127));
                    //txtYL.setText("\t" + Math.abs(tilt-127));
                    
                    if (LeftPan.length() == 1)
                    {
                    	LeftPan = "0" + LeftPan; 
                    }
                    
                    if (LeftTilt.length() == 1)
                    {
                    	LeftTilt = "0" + LeftTilt;	
                    }

                    //txtXL.append(" "+ LeftPan);
                    //txtYL.append(" " + LeftTilt);
                    
                    nt.send("X1" + RightPan + "X2" + RightTilt + "X3" + LeftTilt + "X4" + LeftPan);
            }

            @Override
            public void OnReleased() {  /*txtXL.setText("released"); txtYL.setText("released");*/ nt.send("X1" + RightPan + "X2" + RightTilt + "X3" + LeftTilt + "X4" + LeftPan); }
            
            public void OnReturnedToCenter() { /*txtXL.setText("stopped"); txtYL.setText("stopped");*/  nt.send("X1" + RightPan + "X2" + RightTilt + "X3" + LeftTilt + "X4" + LeftPan); };
        };
    
    	private JoystickMovedListener listener_ = new JoystickMovedListener()
    	{
    		@Override
            public void OnMoved(int pan, int tilt) 
    		{
    			RightPan = Integer.toHexString(Math.abs(pan+127));
        		RightTilt = Integer.toHexString(Math.abs(tilt-127));
        		
                /*txtXR.setText("\t" + Math.abs(pan+127));
                txtYR.setText("\t" + Math.abs(tilt-127));*/
                
                if (RightPan.length() == 1)
                {
                	RightPan = "0" + RightPan; 
                }
                
                if (RightTilt.length() == 1)
                {
                	RightTilt = "0" + RightTilt;	
                }
                /*txtXR.append(" "+ RightPan);
                txtYR.append(" " + RightTilt);*/
           
                nt.send("X1" + RightPan + "X2" + RightTilt + "X3" + LeftTilt + "X4" + LeftPan);
            }

            @Override
            public void OnReleased() {  /*txtXR.setText("released"); txtYR.setText("released"); */ nt.send("X1" + RightPan + "X2" + RightTilt + "X3" + LeftTilt + "X4" + LeftPan); }
            
            public void OnReturnedToCenter() { /* txtXR.setText("stopped"); txtYR.setText("stopped"); */ nt.send("X1" + RightPan + "X2" + RightTilt + "X3" + LeftTilt + "X4" + LeftPan); }
    	};
    	
    	public void onDestroy()
    	{
    		super.onDestroy();
    		nt.teardown();
    	}
    	
    	@Override
    	public void Communicate()
    	{
    		nt.getHandler().post(new Runnable()
    		{
    			@Override
    			public void run()
    			{ 
    				try {
    					String server = nt.getServer();
    					int port = nt.getPort();
    					// Retrieve the ServerName
    					InetAddress serverAddr = InetAddress.getByName(server);
    					
    					Log.d("UDP", "C: Connecting...");
    					/* Create new UDP-Socket */
    					DatagramSocket socket = new DatagramSocket();
    					
    					/* Prepare some data to be sent. */
    					String data = "X1" + RightPan + "X2" + RightTilt + "X3" + LeftTilt + "X4" + LeftPan;
    					byte[] buf = data.getBytes();
    					
    					/* Create UDP-packet with 
    					 * data & destination(url+port) */
    					DatagramPacket packet = new DatagramPacket(buf, buf.length,	serverAddr, port);
    					Log.d("UDP", "C: Sending: '" + new String(buf) + "'");
    					
    					/* Send out the packet */
    					socket.send(packet);
    					Log.d("UDP", "C: Sent.");
    					Log.d("UDP", "C: Done.");
    				} catch (Exception e) {
    					Log.e("UDP", "C: Error", e);
    				}
    			}
    		});
    	}
}
