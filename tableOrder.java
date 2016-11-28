import java.awt.Rectangle;
import java.util.ArrayList;

public class tableOrder {
	int id; 
	ArrayList<menuItem> tableOrder;
	Rectangle rect;
	int x, y;
	
	public tableOrder(int id){
		this.id = id;
		this.tableOrder = new ArrayList<>();
		
	}
	
	public ArrayList<menuItem> getOrder(int id){
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
