import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.swing.*;

public class orderController extends JPanel implements MouseListener, MouseMotionListener{

	private LayoutView view;
	ArrayList<functionKey> functionList = new ArrayList<>();
	ArrayList<Rectangle> rectangles = new ArrayList<>();
	ArrayList<menuItem> orderListTemp = new ArrayList<>();
	ArrayList<menuItem> orderList = new ArrayList<>();
	ArrayList<menuItem> currentList = new ArrayList<>();
	

	
	orderMenu menuArray;
	tableOrder test;
	
	int yoT = 50;
	int xoT = 30;
	
	int xOffset = 0;
	int yOffset = 0;
	
	public orderController(tableOrder test)throws NumberFormatException, IOException{
		
		
		int offsetFuncY= 0; //170
		int offsetFuncX = 0; //170
		
		int xFunc = LayoutView.width - 150;
		int yFunc = LayoutView.height/4;
		
		int xFuncPage = LayoutView.width/2 + 140;
		int yFuncPage = LayoutView.height - 140;
		
		String[] icons = {"addButton.png","deleteButton.png","revertButton.png",
				"backButton.png","forwardButton.png"};
				
		String[] iconsP = {"addButtonPressed.png","deleteButtonPressed.png",
				"revertButtonPressed.png","backButtonPressed.png","forwardButtonPressed.png"};
		
		this.menuArray = new orderMenu();
		this.test = test;
		
		for(menuItem item: menuArray.currentList){
			rectangles.add(item.rect);
		}
		
		for(String s: icons){
			functionList.add(new functionKey(s, ImageIO.read(new File(s))));
			}
		
		int lCount = 0;
		for(functionKey key: functionList){
			
			key.buttonP = ImageIO.read(new File(iconsP[lCount]));
			if(lCount<3){
			key.setX(xFunc);
			key.setY(yFunc + offsetFuncY);
			offsetFuncY+=170;
			lCount++;
			}
			else{
				key.setX(xFuncPage + offsetFuncX);
				key.setY(yFuncPage);
				
				offsetFuncX+=180;
				lCount++;
			}
			key.setRect(new Rectangle(key.x, key.y, key.currentButton.getWidth()/3, key.currentButton.getHeight()/3));
			rectangles.add(key.rect);
		}
	}

	
	@Override
	public void mouseClicked(MouseEvent e) {
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {
	}
	
	@Override
	public void mouseExited(MouseEvent e) {	
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
		int x = e.getX()-10;
		int y = e.getY()-40;
		System.out.println(x + " " + y);
		
		
		if(x>1250 || y > 655){
			for(functionKey key: functionList){
				if(key.rect.contains(x, y)){
					if(key.currentButton == key.button){
						key.pressed();
					}
					else{
						key.reset();
					}

					System.out.println(key.name);
					repaint();
				}
			}
		}
	if(x<1250 && x > LayoutView.width/2 && y<655){
			for(menuItem item: menuArray.currentList){
				if(item.rect.contains(x, y)){
					if(item.currentButton == item.buttonP){
						item.reset();
					}
					else{
						orderListTemp.add(item);
						item.pressed();
					}
					for(menuItem order: test.tableOrder){
						System.out.println(order.name);
					}
					}
			}
		}
	else{
		for(menuItem orderItem: test.tableOrder){
			if(orderItem.rect.contains(x, y)){
				System.out.println(orderItem.name);
				orderItem.pressed();
				repaint();
			}
			
		}
	}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		int x = e.getX()-10;
		int y = e.getY()-40;
		
		
		if(x>1250 || y > 655){
			for(functionKey key: functionList){
				if(key.rect.contains(x, y)){
						key.reset();
						
						if(key.name == "addButton.png"){
							for(menuItem orderItem: orderListTemp){
								menuItem addItem = new menuItem(orderItem);
								test.tableOrder.add(addItem);
								addItem.setX(xoT + xOffset);
								addItem.setY(yoT + yOffset);
								addItem.setRect(new Rectangle(addItem.x, addItem.y, 300, 40));
								if(yOffset<LayoutView.height-150){
									yOffset +=50;
								}
								else{
									yOffset = 0;
									xOffset = 330;
								}
								System.out.println(test.tableOrder.size());
							}
						orderListTemp.clear();
						}
						
						if(key.name == "deleteButton.png"){
							
							Iterator<menuItem> iter = test.tableOrder.iterator();
							while(iter.hasNext()){
								menuItem item = iter.next();
								if(item.currentButton == item.buttonP){
									iter.remove();
									xoT = 30;
									yoT = 50;
									xOffset = 0;
									yOffset = 0;
								for(menuItem itemDelete: test.tableOrder){
										
									itemDelete.setX(xoT + xOffset);
									itemDelete.setY(yoT + yOffset);
									itemDelete.setRect(new Rectangle(itemDelete.x, itemDelete.y, 300, 40));
									if(yOffset<LayoutView.height-150){
										yOffset +=50;
									}
									else{
										yOffset = 0;
										xOffset = 330;
									}
								}
								}
							}
						}
						repaint();
						if(key.name == "revertButton.png"){
							if(test.tableOrder.size()>0 && orderListTemp.isEmpty()){
							test.tableOrder.remove(test.tableOrder.size()-1);
							yOffset -=50;
							}
							else{}
							orderListTemp.clear();
						}
						
						if(key.name == "forwardButton.png"){
							if(menuArray.endIndex+10 < 106){
							menuArray.increment();
							System.out.println(menuArray.startIndex);
							}
							revalidate();
							repaint();
						}
						
						if(key.name == "backButton.png"){
							if(menuArray.startIndex-10>=0){
							menuArray.decrement();
							System.out.println(menuArray.endIndex);
							}
							revalidate();
							repaint();
						}
						for(menuItem rItem: menuArray.currentList){
							rItem.reset();
						}
					}
					repaint();
			}
		}
		else{
			for(menuItem item: menuArray.currentList){
				if(item.rect.contains(x, y)){
					item.currentButton = item.currentButton;
				}
				repaint();
			}
		}
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
		Font myFont = new Font("Tw Cen MT",Font.CENTER_BASELINE, 15);
		Font myFont2 = new Font("Tw Cen MT", Font.PLAIN, 20);
		g.setFont(myFont);
		
		g.drawRect(5, 5, LayoutView.width/2 - 50, LayoutView.height -65);
		g.setColor(Color.pink);
		g.fillRect(5, 5, LayoutView.width/2-50, LayoutView.height-65);
		g.setColor(Color.black);
		
		
		for(menuItem item: menuArray.currentList){
			g.drawImage(item.currentButton, item.x, item.y, item.currentButton.getWidth(), item.currentButton.getHeight(), null);
			g.drawString(item.name, (int) item.rect.getX()+10, (int) item.rect.getCenterY()+5);
			//g.drawRect(item.rect.x, item.rect.y, (int) item.rect.getWidth(), (int) item.rect.getHeight());
		}
		
		for(functionKey key: functionList){
			g.drawImage(key.currentButton, key.x, key.y, key.currentButton.getHeight()/3, key.currentButton.getHeight()/3, null);
		}
		

		
		for(menuItem orderItem: test.tableOrder){
			g.setColor(Color.black);
			g.setFont(myFont2);
			g.drawImage(orderItem.currentButton, orderItem.x, orderItem.y, orderItem.rect.width, orderItem.rect.height, null);
			g.drawString(orderItem.name, orderItem.x + 10, orderItem.y + 25);
		}
	}
}
			