import java.awt.Rectangle;
import java.util.ArrayList;

public class tableOrder {
	String id; 
	ArrayList<menuItem> tableOrder;
	Rectangle rect;
	int x, y;
	
	public tableOrder(String id){
		this.id = id;
		this.tableOrder = new ArrayList<>();
		this.x = 30;
		this.y = 30;
		
	}
	
	public ArrayList<menuItem> getOrder(String id){
		if(id == this.id){
			return this.tableOrder;
		}
		
		else{
			return null;
		}
	}
	
	public void setRect(Rectangle rect){
		this.rect = rect;
	}
	
	public void setX(int x){
		this.x = x;
	}
	
	public void setY(int y){
		this.y = y;
	}
	

}
