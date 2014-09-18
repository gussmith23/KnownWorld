/**
 * 
 */
package us.gusworks.gus.knownWorld.map.mapSection;

/**
 * @author Gus
 *
 * @description One square section of a map. Can be one of any number of types.
 */
public abstract interface MapSection {
	
	//the width and height, in density-independent pixels (dp)???(probably actually in pixels
	//these should be the same, actually
	public static final int WIDTH = 10;
	public static final int HEIGHT = 10;
	
}
