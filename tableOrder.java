import java.awt.Rectangle;
import java.util.ArrayList;

/**
 * @author Jordan Abbatiello
 * Created: November 13th
 * Last Edited: November 23rd
 * A class that creates a unique table order for each node that is created. 
 * Expected Revisions: Negligible
 */
public class tableOrder {
	String id; 
	ArrayList<menuItem> tableOrder;
	Rectangle rect;
	int x, y;
	
	/**@author Jordan Abbatiello
	 * Constructor for the table order
	 * @param id the id of the table node is entered to provide no overlapping
	 */
	public tableOrder(String id){
		this.id = id;
		this.tableOrder = new ArrayList<>();
		this.x = 30;
		this.y = 30;
		
	}
	
	/**@author Jordan Abbatiello
	 * a getter for returning the tableNode's order
	 * @param id a unique string for each tableNode
	 * @return returns the order for that node
	 */
	public ArrayList<menuItem> getOrder(String id){
		if(id == this.id){
			return this.tableOrder;
		}
		
		else{
			return null;
		}
	}
	
	/**@author Jordan Abbatiello
	 * sets the rectangle for the table order
	 * @param rect
	 */
	public void setRect(Rectangle rect){
		this.rect = rect;
	}
	
	/**@author Jordan Abbatiello
	 * sets x value
	 * @param x
	 */
	public void setX(int x){
		this.x = x;
	}
	
	/**@author Jordan Abbatiello
	 * sets y value
	 * @param y
	 */
	public void setY(int y){
		this.y = y;
	}
	

}
