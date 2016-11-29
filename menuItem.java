import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class menuItem{
	
	String name;
	int id;
	boolean isRefillable;
	String SRefillable;
	boolean isSelected;
	String type;
	BufferedImage button;
	BufferedImage buttonP;
	
	BufferedImage currentButton;
	
	int x, y;
	int stringOffsetX, stringOffsetY;
	
	Rectangle rect;
	
	public menuItem(String name, int id, String type, String SRefillable) throws IOException{
		this.name = name;
		this.id = id;
		this.button = ImageIO.read(new File("itemBox.png"));
		this.buttonP = ImageIO.read(new File("itemBoxPressed.png"));
		this.currentButton = ImageIO.read(new File("itemBox.png"));
		
		if(SRefillable == "TRUE"){
			this.isRefillable = true;
		}
		else{
			this.isRefillable = false;
		}
		stringOffsetX = button.getWidth()/4;
		stringOffsetY = button.getHeight()/2;
	}
	public menuItem(menuItem m){
		this.name = m.name;
		this.id = m.id;
		this.button = m.button;
		this.buttonP = m.buttonP;
		this.currentButton = m.button;
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
	
	public String getName(Rectangle rect){
		return this.name;
	}
	
	public void reset(){
		this.currentButton = this.button;
	}
	
	public void pressed(){
		this.currentButton = this.buttonP;
	}
	
	public void setSel(){
		this.isSelected = true;
	}

}
