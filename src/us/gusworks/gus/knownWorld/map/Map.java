package us.gusworks.gus.knownWorld.map;

import java.util.Iterator;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import us.gusworks.gus.knownWorld.map.mapSection.MapSection;
import us.gusworks.gus.knownWorld.map.mapSection.NormalSectionView;

public class Map {
	
	public View[] sections;
	
	Context context;
	
	public Map(Context context){
		
		this.context = context;
		
		//a one dimensional array that will contain all of the sections
		sections = new View[mapX * mapY];
		
		for(int i = 0;i<sections.length;i++){
			
			sections[i] = new NormalSectionView(context);
			sections[i].setBackgroundColor(Color.BLACK);
			
		}
		
	}
	
	//how many blocks are along each axis. TODO possibly combine these, assuming all maps are square?
	int mapY = 7;
	int mapX = 10;
	
	
	
	public int getMapY() {
		return mapY;
	}
	public void setMapY(int mapY) {
		this.mapY = mapY;
	}
	public int getMapX() {
		return mapX;
	}
	public void setMapX(int mapX) {
		this.mapX = mapX;
	}
	
}
