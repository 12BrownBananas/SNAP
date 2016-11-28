import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class menuItem{
	
	String name;
	int id;
	boolean isRefillable;
	String SRefillable;
	String type;
	boolean isSelected = false;
	BufferedImage button;
	int x, y;
	int stringOffsetX, stringOffsetY;
	
	public menuItem(String name, int id, String type, String SRefillable) throws IOException{
		this.name = name;
		this.id = id;
		this.button = ImageIO.read(new File("itemBox.png"));
		
		if(SRefillable == "TRUE"){
			this.isRefillable = true;
		}
		else{
			this.isRefillable = false;
		}
		stringOffsetX = button.getWidth()/4;
		stringOffsetY = button.getHeight()/2;
		
		
	}
	public menuItem(String name, boolean isSelected, BufferedImage button){
		
	}
	
	public void updateButton() throws IOException{
		if(this.button == ImageIO.read(new File("itemBox.png"))){
			this.button = ImageIO.read(new File("itemBoxPressed.png"));
		}
		if(this.button == ImageIO.read(new File("itemBoxPressed.png"))){
			this.button = ImageIO.read(new File("itemBox.png"));
		}
	}

}
