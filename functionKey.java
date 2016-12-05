import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @author Jordan Abbatiello
 * Created: November 14th
 * Last Edited: November 23rd
 * functionKey.java
 * The class that dictates the functionality of the keys on the order menu screen.
 * Allows for the server to add, delete, and undo menu changes. 
 * Expected Revisions: Negligible
 */
public class functionKey {
	
	String name;
	BufferedImage button;
	int x, y;
	boolean isSelected;
	Rectangle rect;
	BufferedImage currentButton;
	BufferedImage buttonP;
	
	
	/**@author Jordan Abbatiello
	 * Constructor for the functions keys. This is called on the server side order menu
	 * to allow for the server to interact with the keys.
	 * @param name A unique identifier for each of the buttons created
	 * @param button initialized the look of the button when the function screen is created
	 */
	public functionKey(String name, BufferedImage button){
		this.name = name;
		this.button = button;
		this.currentButton = button;
	}
	/**
	 * @author Jordan Abbatiello
	 * Sets the x value of the function key.
	 * @param x the x coordinate of the function key.
	 */
	public void setX(int x){
		this.x = x;
	}
	
	/**@author Jordan Abbatiello
	 * Sets the y value of the function key.
	 * @param y the y coordinate of the function key
	 */
	public void setY(int y){
		this.y = y;
	}
	
	/**@author Jordan Abbatiello
	 * Sets the rectangle field of the function key.
	 * @param rect the rectangle bounding the function key
	 */
	public void setRect(Rectangle rect){
		this.rect = rect;
		}
	
	/** @author Jordan Abbatiello
	 *  Resets the button look for the function key to unpressed.
	 */
	public void reset(){
		this.currentButton = this.button;
	}
	
	/** @author Jordan Abbatiello
	 * 	Advances the look of the button for the function key to pressed.
	 */
	public void pressed(){
		this.currentButton = this.buttonP;
	}
}
