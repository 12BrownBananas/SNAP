import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class functionKey {
	
	String name;
	BufferedImage button;
	
	int x, y;
	boolean isSelected;
	
	Rectangle rect;
	BufferedImage currentButton;
	BufferedImage buttonP;
	
	
	public functionKey(String name, BufferedImage button){
		this.name = name;
		this.button = button;
		this.currentButton = button;
	}
	public void setX(int x){
		this.x = x;
	}
	
	public void setY(int y){
		this.y = y;
	}
	
	public void setRect(Rectangle rect){
		this.rect = rect;
		}
	
	public void reset(){
		this.currentButton = this.button;
	}
	
	public void pressed(){
		this.currentButton = this.buttonP;
	}
}
