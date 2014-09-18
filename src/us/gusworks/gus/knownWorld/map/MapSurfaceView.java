package us.gusworks.gus.knownWorld.map;

import us.gusworks.gus.knownWorld.map.mapSection.MapSection;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.net.sip.SipRegistrationListener;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

@TargetApi(11)
public class MapSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
	
	Map map;
	MapSurfaceViewThread thread;
	int offsetLeft = 0;
	int offsetTop = 0;
	float scaleFactor = 1f;
	float oldScaleFactor = 1f;
	ScaleGestureDetector detector;
	String tag = this.getClass().getName();
	
	
	public MapSurfaceView(Context context, Map map) {
		super(context);
		
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		super.setLayoutParams(params);
		//setting the background color is apparently essential here.
		super.setBackgroundColor(Color.BLACK);

		this.map = map;
		
		detector = new ScaleGestureDetector(context, new ScaleListener());
		
		//thread = new MapSurfaceViewThread(super.getHolder(), this);
		

	}
	

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onDraw(Canvas canvas) {
		
		canvas.translate(translateX, translateY);
		canvas.scale(scaleFactor, scaleFactor,midpoint.x,midpoint.y);
		//Log.d(tag, ""+scaleFactor);

		super.onDraw(canvas);
		
		int width = canvas.getWidth()-1; int height = canvas.getHeight();
		
		float pxPerSection = (float) width / (float) map.getMapX();
		
		Paint p = new Paint();
		p.setColor(Color.GREEN);
		
		Bitmap bitmap = Bitmap.createBitmap((int)pxPerSection*map.getMapX(), (int)pxPerSection*map.getMapY(), Bitmap.Config.ARGB_8888);
		Canvas canvas2 = new Canvas(bitmap);
		
		
		for(int i = 0; i<=map.getMapX() ; i++){
			
			canvas2.drawLine(pxPerSection*(i), 0, pxPerSection*(i), pxPerSection*map.getMapY(), p);
			
		}
		
		for(int i = 0; i<=map.getMapY(); i++){
			
			canvas2.drawLine(0, pxPerSection*(i), canvas.getWidth(), pxPerSection*(i), p);
			
		}
		
		canvas2.drawCircle(midpoint.x, midpoint.y, 10, p);
		
	
		
		canvas.drawBitmap(bitmap, 0, 0, p);
		
		

		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}
	
	int mode = 0;
	final int ZOOM = 1;
	final int DRAG = 2;
	
	float startY = 0f;
	float startX = 0f;
	float newY = 0f;
	float newX = 0f;
	float translateX = 0f;
	float translateY = 0f;
	float oldTranslateX = 0f;
	float oldTranslateY = 0f;
	
	float oldDistance = 0f;
	float newDistance = 0f;
	
	Point midpoint=new Point(0, 0);
	
	int pointerId;
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		
		super.onTouchEvent(event);
		
		int eventX = (int) event.getX();
		int eventY = (int) event.getY();
		

		
		
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			
			mode = DRAG;
			
			startX = event.getX() - oldTranslateX;
			startY = event.getY() - oldTranslateY;
			return true;
        }
		
        if(event.getActionMasked() == MotionEvent.ACTION_POINTER_DOWN){
        	
        	
        	Log.d(tag, "WENIS WENIS WENIS WENIS");
        	
        	//return if the pointer is too close
        	//if(vectorDistance(event)<10f) return true;
        	
        	
        	//we're zooming
        	mode = ZOOM;
        	
        	//the original distance between pointers
        	oldDistance = vectorDistance(event);
        	
        	
        }
        
        if(event.getAction() == MotionEvent.ACTION_MOVE) {
        	
        	
        	newX = event.getX();
			newY = event.getY();
			
			//if you're dragging, you'll need to translate
			if(mode == DRAG){
				
				translateX = newX - startX;
				translateY = newY - startY;

				


				
			}
			
			if(mode == ZOOM){
				
				newDistance = vectorDistance(event);
				
				scaleFactor = oldScaleFactor*(newDistance/oldDistance);
				
				midpoint = vectorMidpoint(event);
				
				
				//oldDistance = newDistance;
				
				Log.d(tag, ""+midpoint.x+" "+midpoint.y);
				

				
			}
        	
        }
        
        if(event.getAction() == MotionEvent.ACTION_UP) {
        	
        	oldTranslateX = translateX;
			oldTranslateY = translateY;
			
			mode = 0;
        	
        }
        
        if(event.getActionMasked() == MotionEvent.ACTION_POINTER_UP) {
        	
        	oldScaleFactor = scaleFactor;
        	
        	mode = 0;
        	
        }
        
        
        invalidate();

        //Log.d(tag, ""+mode);
        
        return true;
        
		
	}
	
	public class MapSurfaceViewThread extends Thread{
		
		MapSurfaceView msv;
		SurfaceHolder sh;
		
		public MapSurfaceViewThread(SurfaceHolder holder, MapSurfaceView mapSurfaceView){
			
			msv = mapSurfaceView;
			sh = holder;
			
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			
			Canvas c = sh.lockCanvas();
			int width = c.getWidth(); int height = c.getHeight();
			
			float pxPerSection = (float) width / (float) map.getMapX();
			
			Paint p = new Paint();
			p.setColor(Color.GREEN);
			
			for(int i = 0; i<map.getMapX() - 1; i++){
				
				c.drawLine(pxPerSection*(i+1), 0, pxPerSection*(i+1), 10, p);
				
			}
			
			
		}
		
	}

	
	private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener{
		
		@Override
		public boolean onScale(ScaleGestureDetector detector) {

			//scaleFactor *= detector.getScaleFactor();
			invalidate();
			return true;
		}
		
	}
	
	static float vectorDistance(MotionEvent event){
		
		float dx = event.getX(0) - event.getX(1);
		float dy = event.getY(0) - event.getY(1);
		
		return FloatMath.sqrt(dx*dx + dy*dy);
		
	}
	
	static Point vectorMidpoint(MotionEvent event){
		
		float dx = event.getX(0) - event.getX(1);
		float dy = event.getY(0) - event.getY(1);
		
		return new Point((int)(Math.floor(dx/2)),(int) (Math.floor(dy/2)));
		
	}
	
}
