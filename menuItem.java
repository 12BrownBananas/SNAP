import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * @author Jordan Abbatiello
 * Created: November 13th
 * Last Edited: November 23rd
 * menuItem.java
 * A class that is in charge of creating menu items for the server side order control.
 * Used in the controller, the order menu, as well as in the table nodes to display 
 * each table's order.
 * Expected Revisions: Implementing refillable
 *
 */
public class menuItem{
	
	String name;
	int id;
	boolean isRefillable; //future implementations
	String SRefillable;
	boolean isSelected;
	String type;
	BufferedImage button;
	BufferedImage buttonP;
	BufferedImage currentButton;
	int x, y;
	int stringOffsetX, stringOffsetY; //offsets for positioning the menu items
	Rectangle rect;
	
	/**@author Jordan Abbatiello
	 * Constructor for the menu items. 
	 * @param name provides a unique string for each menu item
	 * @param id provides a unique integer for each menu item
	 * @param type provides a type for each menu item
	 * @param SRefillable provides the string for if it is refillable
	 * @throws IOException
	 */
	public menuItem(String name, int id, String type, String SRefillable) throws IOException{
		this.name = name;
		this.id = id;
		this.button = ImageIO.read(new File("itemBox.png"));
		this.buttonP = ImageIO.read(new File("itemBoxPressed.png"));
		this.currentButton = ImageIO.read(new File("itemBox.png"));
		
		if(SRefillable == "TRUE"){
			this.isRefillable = true; //converts the string to a boolean
		}
		else{
			this.isRefillable = false;
		}
		stringOffsetX = button.getWidth()/4;
		stringOffsetY = button.getHeight()/2;
	}
	/** @author Jordan Abbatiello
	 * constructor which takes another menu items and copies it while maintaining
	 * the parameter.
	 * @param m a menu item that has been selected and added to the current order list.
	 */
	public menuItem(menuItem m){
		this.name = m.name;
		this.id = m.id;
		this.button = m.button;
		this.buttonP = m.buttonP;
		this.currentButton = m.button;
	}
	
	/**@author Jordan Abbatiello
	 * Sets x value. 
	 * @param x
	 */
	public void setX(int x){
		this.x = x;
	}
	
	/**@author Jordan Abbatiello
	 * Sets y
	 * @param y
	 */
	public void setY(int y){
		this.y = y;
	}
	
	/**@author Jordan Abbatiello
	 * Sets rectangle
	 * @param rect
	 */
	public void setRect(Rectangle rect){
		this.rect = rect;
	}
	
	/**@author Jordan Abbatiello
	 * gets unique string of a rectangle
	 * @param rect
	 * @return returns the name of the rectangle
	 */
	public String getName(Rectangle rect){
		return this.name;
	}
	
	/**@author Jordan Abbatiello
	 * resets the button to unpressed
	 */
	public void reset(){
		this.currentButton = this.button;
	}
	
	/**@author Jordan Abbatiello
	 * sets the button to pressed
	 */
	public void pressed(){
		this.currentButton = this.buttonP;
	}
	

}
