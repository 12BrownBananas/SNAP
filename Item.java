
/*
* @author Garrett Thompson
* Created: November 4th
* Last Edited: November 13th
* Item.java
* Data storage class that was initially intended to be used to generate orders.
* Expected Revisions: Integration with order generation.
*/ 
public class Item {
	String nameID;
	boolean refillable;
	String comment;
	/* @author Garrett Thompson
	* Constructor method. Creates a new item based on the given parameters.
	* @param myComment  Text comment to describe the item.
	* @param isRefillable  Check to see if this item can have refills requested for it. Currently unused.
	* @param id  This item's string identifier (name).
	*/
	Item(String myComment, boolean isRefillable, String id) {
		comment = myComment;
		refillable = isRefillable;
		nameID = id;
	}
	/* @author Garrett Thompson
	* Sets this item's ID to a new supplied ID.
	* @param ID  the ID to set this item's ID to.
	*/
	public void setID(String newID) {
		nameID = newID;
	}
	/* @author Garrett Thompson
	* @returns this item's ID string.
	*/
	public String getID() {
		return nameID;
	}
	/* @author Garrett Thompson
	* Changes the refillable status of this item.
	* @param isRefillable  The refillable status to set this item to.
	*/
	public void setRefillable(boolean isRefillable) {
		refillable = isRefillable;
	}
	/* @author Garrett Thompson
	* @returns whether this item is refillable or not 
	*/
	public boolean isRefillable() {
		return refillable;
	}
	/* @author Garrett Thompson
	* sets this item's comment to some new comment string.
	* @param newComment  the new comment string.
	*/
	public void setComment(String newComment) {
		comment = newComment;
	}
	/* @author Garrett Thompson
	* @returns this item's current comment string.
	*/
	public String getComment() {
		return comment;
	}
	
}
