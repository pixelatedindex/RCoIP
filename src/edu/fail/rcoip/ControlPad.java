package edu.fail.rcoip;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class ControlPad extends SurfaceView implements SurfaceHolder.Callback 
{
	//The ControlPad to draw
	private Bitmap bmp;
	
	// X and Y dimensions of the screen
	int x_resolution, y_resolution;
	
	//Position of bitmap
	float bmp_x, bmp_y;
	
	public ControlPad(Context context, int xdimen, int ydimen) 
	{
		super(context);
		x_resolution = xdimen;
		y_resolution = ydimen;
		bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ball_green);
		//Set UI Component vales
        //SetLayout();
        
        //Set values for the defined SurfaceViews
        //SetSurfaceViews();
		
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) 
	{
		// TODO Auto-generated method stub
		
	}

	public void surfaceCreated(SurfaceHolder holder) 
	{
		// TODO Auto-generated method stub
		
	}

	public void surfaceDestroyed(SurfaceHolder holder) 
	{
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		bmp_x = event.getX();
		bmp_y = event.getY();
		UpdateControls();
		return true;
	}
	
	private void UpdateControls()
	{
		Canvas canvas = null;
        try {
            canvas = getHolder().lockCanvas(null);
            synchronized (getHolder()) {
                this.onDraw(canvas);
            }
        }
        finally {
            if (canvas != null) {
                getHolder().unlockCanvasAndPost(canvas);
            }
        }
	}
	
	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		canvas.drawColor(Color.DKGRAY);
		canvas.drawBitmap(bmp, bmp_x, bmp_y, null);
	}
}
