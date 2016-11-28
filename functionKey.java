import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class functionKey {
	
	String name;
	BufferedImage button;
	BufferedImage buttonPressed;
	
	int x, y;
	boolean isSelected;
	
	
	public functionKey(String name, BufferedImage button){
		this.name = name;
		this.button = button;
	}
	
	public void updateImage(BufferedImage b) throws IOException{
		button = b;
	}

}
