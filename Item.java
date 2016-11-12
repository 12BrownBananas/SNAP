
public class Item {
	String nameID;
	boolean refillable;
	String comment;
	Item(String myComment, boolean isRefillable, String id) {
		comment = myComment;
		refillable = isRefillable;
		nameID = id;
	}
	public void setID(String newID) {
		nameID = newID;
	}
	public String getID() {
		return nameID;
	}
	public void setRefillable(boolean isRefillable) {
		refillable = isRefillable;
	}
	public boolean isRefillable() {
		return refillable;
	}
	public void setComment(String newComment) {
		comment = newComment;
	}
	public String getComment() {
		return comment;
	}
	
}
