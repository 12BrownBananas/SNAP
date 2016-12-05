import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
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

/**
 * @author Jordan Abbatiello
 * Created: November 13th
 * Last Modified: NOvember 23rd
 * orderController.java
 * Controls the logic behind the order menu screen. Uses arrays to sort through which items are 
 * availble to be selected, which items have been selected, and which items have been added
 * to the final order. Dictates the locations of each of the buttons as well.
 * Expected Revisions: Negligible
 *
 */
public class orderController extends JPanel implements MouseListener, MouseMotionListener{

	
	//arrays that will be called to create the order menu functionality
	
	ArrayList<functionKey> functionList = new ArrayList<>();
	ArrayList<Rectangle> rectangles = new ArrayList<>();
	ArrayList<menuItem> orderListTemp = new ArrayList<>();
	ArrayList<menuItem> orderList = new ArrayList<>();
	ArrayList<menuItem> currentList = new ArrayList<>();
	

	
	orderMenu menuArray;
	tableOrder test;
	
	int yoT = 50; //offset of the tableOrder
	int xoT = 30;
	
	int xOffset = 0; //offset of the menu items
	int yOffset = 0;
	
	/**@author Jordan Abbatiello
	 * Constructor for the order controller which dictates the look of the order menu
	 * @param test temporary field for when a table node is created, a new screen is created.
	 * @throws NumberFormatException
	 * @throws IOException
	 */
	public orderController(tableOrder test)throws NumberFormatException, IOException{
		
		
		int offsetFuncY= 0; //170 for function buttons
		int offsetFuncX = 0; //170 for function buttons
		
		int xFunc = LayoutView.width - 150; //initial values of function buttons
		int yFunc = LayoutView.height/4; //initial values of function butons
		
		int xFuncPage = LayoutView.width/2 + 140; //initial values of page function buttons
		int yFuncPage = LayoutView.height - 140; //initial values of page function buttons
		
		//string arrays for the different button files
		String[] icons = {"addButton.png","deleteButton.png","revertButton.png",
				"backButton.png","forwardButton.png"};
				
		String[] iconsP = {"addButtonPressed.png","deleteButtonPressed.png",
				"revertButtonPressed.png","backButtonPressed.png","forwardButtonPressed.png"};
		
		//creates a new menu order and assigns it to the menu field
		this.menuArray = new orderMenu();
		
		//this test table is equal to the parameter
		this.test = test;
		
		//creates an array of rectangles from the current list of menu items
		for(menuItem item: menuArray.currentList){
			rectangles.add(item.rect);
		}
		
		//creates adds a new function key for each of the icons in the icon string
		for(String s: icons){
			functionList.add(new functionKey(s, ImageIO.read(new File(s))));
			}
		
		int lCount = 0;
		
		//sifts through the function list and sets the values of each of the function key fields
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
			rectangles.add(key.rect); //adds the rectangle to the rectangle array
		}
	}
	

	/* @author Jordan Abbatiello
	 * 
	 * An overide of mousePressed used to create interaction with the elements in the 
	 * order controller screen.
	 * 
	 * @param e the mouse event
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		
		int x = e.getX()-10;
		int y = e.getY()-40;
		System.out.println(x + " " + y);
		
		//a for loop that checks for interaction with the function keys
		if(x>1250 || y > 655){
			for(functionKey key: functionList){
				if(key.rect.contains(x, y)){
					if(key.currentButton == key.button){
						key.pressed(); //sets the current key to pressed
					}
					else{
						key.reset(); //sets the current key to unpressed
					}
					repaint();
				}
			}
		}
		
		//a for loop that checks for interaction with the current menu list
	if(x<1250 && x > LayoutView.width/2 && y<655){
			for(menuItem item: menuArray.currentList){
				if(item.rect.contains(x, y)){
					if(item.currentButton == item.buttonP){
						item.reset(); //sets the button to unpressed
					}
					else{
						orderListTemp.add(item); //adds the item to a temporary array
						item.pressed(); //sets the button to pressed
						}
					}
				}
			}
	
	//a for loop that checks for interaction with current table order items.
	else{
		for(menuItem orderItem: test.tableOrder){
			if(orderItem.rect.contains(x, y)){
				orderItem.pressed();
				repaint();
				}
			}
		}
	}

	/* @author Jordan Abbatiello
	 * Override of the mouse released method in MouseListener. Once the button has been released,
	 * an action occurs. This is where the logic of array manipulation occurs.
	 * 
	 * param e the mouse event
	 * 
	 */
	@Override
	public void mouseReleased(MouseEvent e) {
		int x = e.getX()-10;
		int y = e.getY()-40;
		
		//checks for the function key interaction
		if(x>1250 || y > 655){
			for(functionKey key: functionList){
				if(key.rect.contains(x, y)){
						key.reset(); //resets the button image
						
						
						//if the add button is selected
						if(key.name == "addButton.png"){
							for(menuItem orderItem: orderListTemp){ //for all current items in the temp
								menuItem addItem = new menuItem(orderItem); //create a new item
								test.tableOrder.add(addItem); //add that item to the table order of that temp node
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
							}
						orderListTemp.clear(); //clears the temporary orderList
						}
						
						
						//if the delete button is selected
						if(key.name == "deleteButton.png"){
							
							Iterator<menuItem> iter = test.tableOrder.iterator();
							while(iter.hasNext()){
								menuItem item = iter.next();
								if(item.currentButton == item.buttonP){ //removes the item
									iter.remove();
									
									//resets the offsets
									xoT = 30;
									yoT = 50;
									xOffset = 0;
									yOffset = 0;
									
									//sifts through the remaining items and resets the positions
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
						
						//if the revert button is selected
						if(key.name == "revertButton.png"){
							if(test.tableOrder.size()>0 && orderListTemp.isEmpty()){
							test.tableOrder.remove(test.tableOrder.size()-1); //simply pop the last item in the array
							yOffset -=50;
							}
							else{}
							orderListTemp.clear();
						}
						
						//advance the page
						if(key.name == "forwardButton.png"){
							if(menuArray.endIndex+10 < 106){
							menuArray.increment();
							System.out.println(menuArray.startIndex);
							}
							revalidate();
							repaint();
						}
						
						//retreat the page
						if(key.name == "backButton.png"){
							if(menuArray.startIndex-10>=0){
							menuArray.decrement();
							System.out.println(menuArray.endIndex);
							}
							revalidate();
							repaint();
						}
						
						//resets all of the buttons
						for(menuItem rItem: menuArray.currentList){
							rItem.reset();
						}
					}
					repaint();
			}
		}
		else{
			
			//changes the look of the menu items when selected
			for(menuItem item: menuArray.currentList){
				if(item.rect.contains(x, y)){
					item.currentButton = item.currentButton;
				}
				repaint();
			}
		}
	}
	
	/* @author Jordan Abbatiello
	 * This method dictates how the order menu is represented visually.
	 * This will simply be added to the frame as a component.
	 * @param g The graphics context for drawing
	 */
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
		
		//draws menu items
		for(menuItem item: menuArray.currentList){
			g.drawImage(item.currentButton, item.x, item.y, item.currentButton.getWidth(), item.currentButton.getHeight(), null);
			g.drawString(item.name, (int) item.rect.getX()+10, (int) item.rect.getCenterY()+5);
		}
		
		//draws function items
		for(functionKey key: functionList){
			g.drawImage(key.currentButton, key.x, key.y, key.currentButton.getHeight()/3, key.currentButton.getHeight()/3, null);
		}
		
		//draws table order items
		for(menuItem orderItem: test.tableOrder){
			g.setColor(Color.black);
			g.setFont(myFont2);
			g.drawImage(orderItem.currentButton, orderItem.x, orderItem.y, orderItem.rect.width, orderItem.rect.height, null);
			g.drawString(orderItem.name, orderItem.x + 10, orderItem.y + 25);
		}
	}


	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
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
}
			