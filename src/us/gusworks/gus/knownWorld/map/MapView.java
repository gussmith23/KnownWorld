package us.gusworks.gus.knownWorld.map;

import us.gusworks.gus.knownWorld.KnownWorld;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.text.style.SuperscriptSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ZoomButton;

public class MapView extends SurfaceView implements SurfaceHolder.Callback{

	private Map map;
	
	private String tag = this.getClass().getName();
	
	private int mode = 0;
	private final int DRAG = 1;
	private final int ZOOM = 2;
	
	private Point translate = new Point(0, 0);
	private Point oldTranslate = new Point(0, 0);
	private Point oldDrag = new Point(0, 0);
	private Point drag = new Point(0, 0);
	
	private float totalScaleFactor = 1;
	private float scaleFactor = 1;
	private float oldScaleFactor = 1;
	private float oldDistance;
	private float distance;
	private Point midpoint = new Point(0, 0);
	private Point originalTranslate = new Point(0,0);
	
	private float[] values = new float[9];
	private float scale;
	
	Matrix m = new Matrix();


	public MapView(Context context, Map map) {
		super(context);
		
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		super.setLayoutParams(params);
		
		//setting the background color is apparently essential here.
		super.setBackgroundColor(Color.BLACK);

		this.map = map;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
				
		
		//canvas.scale(scaleFactor, scaleFactor,midpoint.x-translate.x,midpoint.y-translate.y);
		//canvas.translate(translate.x/scaleFactor, translate.y/scaleFactor);

		super.onDraw(canvas);
		
		//m.reset();
		//m.preScale(scaleFactor, scaleFactor, midpoint.x-translate.x, midpoint.y-translate.y);
		//m.preTranslate(translate.x, translate.y);
		
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
		
		canvas2.drawCircle(midpoint.x, midpoint.y, 5, p);
		canvas2.drawCircle(10,10, 5, p);
		
				
		canvas.drawBitmap(bitmap, m, p);
		
	
		
		
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		super.onTouchEvent(event);
				
		switch(event.getAction() & MotionEvent.ACTION_MASK){
		
		case MotionEvent.ACTION_MOVE:
			
			switch(mode){
			
			case DRAG:
				
				
				m.getValues(values);
				scale = values[Matrix.MSCALE_X];
				
				drag.x = (int) event.getX();
				drag.y = (int) event.getY();
				
				translate.x = drag.x - oldDrag.x; 
				translate.y = drag.y - oldDrag.y;
				
				m.preTranslate(translate.x/scale, translate.y/scale);
				
				oldDrag.x = drag.x;
				oldDrag.y = drag.y;
				
			break;
			
			case ZOOM:
				
				m.getValues(values);
				scale = values[Matrix.MSCALE_X];
				
				distance = distance(event);
				
				scaleFactor = (distance/oldDistance);
				
				m.preScale(scaleFactor, scaleFactor ,midpoint.x - originalTranslate.x,midpoint.y - originalTranslate.y);
								
				oldDistance=distance;
				
				float x = midpoint.x - values[Matrix.MTRANS_X]*scale;
				float y = midpoint.y - values[Matrix.MTRANS_Y]*scale;
				Log.d(tag, ""+ values[Matrix.MTRANS_X] + " " + values[Matrix.MTRANS_Y]);
				
				
				
				
			break;
			
			}
			
			
		break;
			
		case MotionEvent.ACTION_DOWN:
						
			mode = DRAG;
			
			//the origin of the drag, compensating for past translations
			oldDrag.x = (int) (event.getX() - oldTranslate.x);
			oldDrag.y = (int) (event.getY() - oldTranslate.y);
		
		break;
		
		case MotionEvent.ACTION_POINTER_DOWN:
			
			m.getValues(values);
			
			//if there's more than two pointers down
			if(event.getPointerCount()>2) return true;
			
			mode = ZOOM;
			
			midpoint = midpoint(event);
			
			oldDistance = distance(event);
			
			originalTranslate.x = (int) values[Matrix.MTRANS_X];
			originalTranslate.y = (int) values[Matrix.MTRANS_Y];
			
			
		break;
			
		case MotionEvent.ACTION_UP:
			
			oldTranslate.x = translate.x;
			oldTranslate.y = translate.y;
			
			mode = 0;
		
		break;
		
		case MotionEvent.ACTION_POINTER_UP:
			
			oldScaleFactor = scaleFactor;
			
			oldDistance = distance;
			
			mode = 0;
			
			originalTranslate = new Point(0,0);
			
		break;
		
		}
		
		//DON'T FORGET TO INVALIDATE!
		invalidate();
		
		return true;
		
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}
	
	private float distance(MotionEvent event){
		
		float dx = event.getX(0)-event.getX(1);
		float dy = event.getY(0)-event.getY(1);
		
		return (float) Math.sqrt(dx*dx+dy*dy);
		
	}
	
	private Point midpoint(MotionEvent event){
		
		float x = (event.getX(0)+event.getX(1))/2;
		float y = (event.getY(0)+event.getY(1))/2;
		
		return new Point((int)x, (int)y);
		
	}

}
