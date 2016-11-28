import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.*;

public class orderController extends JPanel implements MouseListener, MouseMotionListener{
	public int page = 1;
	public int startIndex = 0;
	public int endIndex = 10;
	
	ArrayList<menuItem> currentList = new ArrayList<>();
	ArrayList<functionKey> functionList = new ArrayList<>();
	ArrayList<Rectangle> rectangles = new ArrayList<>();
	
	
	
	public orderController()throws NumberFormatException, IOException{
		
		
		orderMenu menuArray = new orderMenu();
		for(int i = 0; i<endIndex; i++){
			currentList.add(menuArray.menu.get(i));
		}
		
		String[] icons = {"addButton.png","deleteButton.png","revertButton.png",
				"backButton.png","forwardButton.png"};
				
		String[] iconsP = {"addButtonPressed.png","deleteButtonPressed.png",
				"revertButtonPressed.png","backButtonPressed.png","forwardButtonPressed.png"};
		
		
		for(String s: icons){
			functionList.add(new functionKey(s, ImageIO.read(new File(s))));
		}
		
		for(functionKey k: functionList){
			int i = 0;
			k.buttonPressed = ImageIO.read(new File(iconsP[i]));
			i++;
		}
		}
		
		BufferedImage box = ImageIO.read(new File("itemBox.png"));


	@Override
	public void mouseClicked(MouseEvent e) {
	int x = e.getX();
	int y = e.getY();
	System.out.println(x + " " + y);
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
		for(Rectangle rect: rectangles){
			if(rect.contains(e.getX(), e.getY())){
				System.out.println("pressed");
			}
		}
		repaint();
		
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {

		//repaint();
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {	
	}

	@Override
	public void mouseMoved(MouseEvent e) {	
	}
	
	
	@Override
	public void paintComponent(Graphics g){
		
		super.paintComponent(g);
		int i = 0;
		int j = 0;
		int count = 1;
		
		for(menuItem item: currentList){
			int x = orderViewer.width/2+i;
			int y = orderViewer.height/5+j;
			
			g.drawRect(x, y, item.button.getWidth()-85, item.button.getHeight()-30);
			g.drawImage(item.button, x, y, orderViewer.width/6, orderViewer.height/10, null);
			g.drawString(item.name, x + item.stringOffsetX, y + item.stringOffsetY);
			
			if(count%2!=0){
			i+=280;
			
			}
			else{i=0; j+=100;}
			count++;
		}
		
		count = 0;
		int offsetY= 0;
		int offsetX = 170;
		
		for(functionKey key: functionList){
			
			int scale = 3;
			int buttonWidth = key.button.getWidth()/scale;
			int buttonHeight = key.button.getHeight()/scale;
			
			
			if(count < 3){
			int xF = orderViewer.width - 150;
			int yF = orderViewer.height/4;

			Rectangle newRectangle = new Rectangle(xF, yF+offsetY, buttonWidth, buttonHeight);
			rectangles.add(newRectangle);
			
			g.drawRect(xF, yF+offsetY, buttonWidth, buttonHeight);
			g.drawImage(key.button, xF, yF+offsetY, buttonWidth, buttonHeight, null);
			offsetY+=150;
			}
			
			
			else{
				int xF = orderViewer.width/2 + offsetX;
				int yF = orderViewer.height-140;
				
				g.drawRect(xF, yF, buttonWidth, buttonHeight);
				g.drawImage(key.button, xF, yF, buttonWidth, buttonHeight, null);
				offsetX+=110;
			}
			count++;	
		}
	}
}