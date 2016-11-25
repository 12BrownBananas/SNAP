import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class orderController extends JPanel implements MouseListener, MouseMotionListener{

	public orderController()throws FileNotFoundException{
		
		orderMenu menuArray = new orderMenu();
		int count = 0;
		for(menuItem item : menuArray.menu){
			
			
			
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void paintComponent(Graphics g){
		
		BufferedImage img = null; 
		try {
			img = ImageIO.read(new File("ItemBox.png"));
		} catch (IOException e) {
		}

		super.paintComponent(g);
		for(int i = 0; i<9; i++){
			g.drawImage(img, 100, 100, null);
			//g.drawString(menuItem.name, 80, 80);
			
		}
	}
}