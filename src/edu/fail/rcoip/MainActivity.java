package edu.fail.rcoip;

import com.android.debug.hv.ViewServer;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.view.Display;
import android.view.Menu;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.view.MotionEvent;

public class MainActivity extends Activity 
{
	//UI Layout Elements
	public TextView flight_info;
	public SurfaceView dpad_l, dpad_r;
	
	//Screen Properties
	public int screen_x, screen_y;
	public double density;
	
	//Properties of UI Layout Elements
	public int tv_width, tv_height;
	public int sw_width, sw_height;
	public int dpad_l_xo, dpad_l_yo;
	public int dpad_r_xo, dpad_r_yo;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewServer.get(this).addWindow(this);
        
        //Set screen_x and screen_y
        CalculateProperties();
        
        //ControlPad dpad = new ControlPad(this, screen_x, screen_y);
        
        //Set UI Component vales
        SetLayout();
    	
        //Set values for the defined SurfaceViews
        SetSurfaceViews();
        
        //Start new activity?
        //Pass the surface view and text view?
    }
    
    // Get screen dimensions using Android's DisplayManager
    // Function modifies screen_x, screen_y, and density
    public void CalculateProperties()
    {
    	Display dp = getWindowManager().getDefaultDisplay();
    	Point s_res = new Point();
    	dp.getSize(s_res);
    	screen_x = s_res.x;
    	screen_y = s_res.y;
    	density = getResources().getDisplayMetrics().density;
    }
    
    public void SetLayout()
    {
    	flight_info = (TextView) findViewById(R.id.flight_info);
    	tv_width = (int)(screen_x*0.8);
    	tv_height = (int)(screen_y*0.18);
    	sw_width = (int) (screen_x*0.3);
    	sw_height = (int) (screen_y*0.53);
    	dpad_l_xo = (int) (screen_x*0.1);
    	dpad_l_yo = (int) (screen_y*.25);
    	dpad_r_xo = (int) (screen_x*0.6);
    	dpad_r_yo = (int) (screen_y*0.25);
    	flight_info.setHeight(tv_height);
    	flight_info.setWidth(tv_width);	
    }
    
    public void SetSurfaceViews()
    {
    	RelativeLayout relay = (RelativeLayout) findViewById(R.id.relay);
    	ControlPad dpad_l = new ControlPad(this, screen_x, screen_y);
    	ControlPad dpad_r = new ControlPad(this, screen_x, screen_y);
    	
    	RelativeLayout.LayoutParams params_l = new RelativeLayout.LayoutParams(sw_width, sw_height);
    	params_l.leftMargin = dpad_l_xo;
    	params_l.topMargin = dpad_l_yo;
    	relay.addView(dpad_l, params_l);
    	
    	RelativeLayout.LayoutParams params_r = new RelativeLayout.LayoutParams(sw_width, sw_height);
    	params_r.leftMargin = dpad_r_xo;
    	params_r.topMargin = dpad_r_yo;
    	relay.addView(dpad_r, params_r);
    }
    
    public void onDestroy()
    {
    	super.onDestroy();
    	ViewServer.get(this).removeWindow(this);
    }

    public void onStart(Bundle savedInstanceState)
    {	
    	
    }
    
    public void onResume()
    {
    	super.onResume();
    	ViewServer.get(this).setFocusedWindow(this);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    public boolean onTouchEvent(MotionEvent event)
    {	
    	int eventType = event.getAction();
    	switch(eventType)
    	{
    		case MotionEvent.ACTION_DOWN:
    		{
    			float x = event.getRawX();
    			float y = event.getRawY();
    			flight_info.setText("Flight Information");
    			flight_info.append("\nSupposed screen height: " + Integer.toString(tv_height));
    	    	flight_info.append("\nSupposed screen width: " + Integer.toString(tv_width));
    			flight_info.append("\nX-axis: " + Float.toString(x));
    			flight_info.append("\nY-axis: " + Float.toString(y));
    			break;
    		}
    		
    		case MotionEvent.ACTION_MOVE:
    		{
    			float x = event.getRawX();
    			float y = event.getRawY();
    			flight_info.setText("Flight Information");
    			flight_info.append("\nSupposed screen height: " + Integer.toString(tv_height));
    	    	flight_info.append("\nSupposed screen width: " + Integer.toString(tv_width));
    			flight_info.append("\nX-axis: " + Float.toString(x));
    			flight_info.append("\nY-axis: " + Float.toString(y));
    			break;
    		}
    			
    		case MotionEvent.ACTION_UP:
    		{
    			flight_info.setText("Flight Information");
    			flight_info.append("\nFinger lifted.");
    			flight_info.append("\nSupposed screen height: " + Integer.toString(tv_height));
    	    	flight_info.append("\nSupposed screen width: " + Integer.toString(tv_width));
    			break;
    		}
    			
    	}
    	return false;
    }
    
}
