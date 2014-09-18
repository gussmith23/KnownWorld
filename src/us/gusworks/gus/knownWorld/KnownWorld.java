package us.gusworks.gus.knownWorld;

import us.gusworks.gus.knownWorld.connection.Connection;
import us.gusworks.gus.knownWorld.map.Map;
import us.gusworks.gus.knownWorld.map.MapSurfaceView;
import us.gusworks.gus.knownWorld.map.MapView;
import android.os.Bundle;
import android.app.Activity;
import android.graphics.Color;
import android.text.Layout;
import android.view.Menu;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class KnownWorld extends Activity {

	Map map;
	int squareSize = 3;
	Connection connection = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_view);
        
        connection = new Connection();
        
        map = new Map(this);
        
        //this is the outer box of the map.
        LinearLayout mapViewLinearLayout = (LinearLayout) findViewById(R.id.map_view_linear_layout);
        mapViewLinearLayout.setWeightSum((float)squareSize);
        
        
        //commenting this out as i'm trying surfaceview instead of gridview
        /*mapViewLinearLayout.addView(new MapView(this, map));*/
        
        mapViewLinearLayout.addView(new MapView(this,map));
        
        
        /*
        View[][] views = new View[squareSize][squareSize];
        //params for the innermost blocks
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT, 1.0f); 	
        //the outer for loop creates linearlayouts downward.
        for(int i = 0; i < squareSize; i++){
        	
        	       	
        	mapViewLinearLayout.addView(new LinearLayout(this),LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
        	LinearLayout ll = (LinearLayout) mapViewLinearLayout.getChildAt(i);
        	ll.setWeightSum((float)squareSize);
        	ll.setLayoutParams(params);
        	GridView gv; 
        	
        	
        	
        	for(int j = 0; j < squareSize; j++){
        		
        		ll.addView(new TextView(this),LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
        		
        		TextView subll = (TextView) ll.getChildAt(j);
        		
        		subll.setText(""+ j);	
        		subll.setLayoutParams(params);
        		
        	}
        			
        	
        }
       */
        
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_map_view, menu);
        return true;
    }
}
