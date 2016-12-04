import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import javax.imageio.ImageIO;
import javax.swing.*;
/*
* @author Garrett Thompson
* Created: November 4th
* Last Edited: November 23rd
* TableNode.java
* A fundamental class for SNAP, the Table Node object is used by both the client and server components
* to keep track of the current status of tables. 
* Expected Revisions: Negligible
*/
public class TableNode implements Serializable {
	tableOrder thisOrder;
	String nodeID;
	int tableStatus;
	String[] statusNames;
	boolean isClientSide;
	int x;
	int y;
	int oldX;
	int oldY;
	int xOffset;
	int yOffset; //Offset value for drawing properly
	int selectYOffset;
	int genNotif; //However many unacknowledged general notifications we have
	int refillNotif;
	BufferedImage nodeIcon;
	BufferedImage dialogBox;
	BufferedImage trashCan;
	BufferedImage button;
	BufferedImage button2; //The "pressed" state of button
	BufferedImage orderButton;
	BufferedImage notifIcon;
	BufferedImage advanceIcon;
	int buttonX;
	int buttonY;
	int dialogBoxX;
	int dialogBoxY; //Both of these are relative to the x and y coordinate of the node
	int trashX;
	int trashY;
	boolean isSelected;
	boolean synched;
	LayoutView view;
	ClientController ctrl;
	orderController control;
	tableOrder tableOrder;
	
	Ellipse2D advanceButtonMask;
	
	/*
	* @author Garrett Thompson
	* Client-side constructor for the object. Some details are left blank due to client-side operations making them
	* irrelevant.
	* @param uniqueID  A unique node identification string (four-digit) such that the node will be uniquely present in the network.
	* @param myController  Controller object this node is associated with.
	*/
	TableNode(String uniqueID, ClientController myController) {
		ctrl = myController;
		isClientSide = true;
		tableStatus = 0;
		nodeID = uniqueID;
		statusNames = new String[6];
		statusNames[0] = "Idle";
		statusNames[1] = "Ready to Order";
		statusNames[2] = "Order Placed";
		statusNames[3] = "Order Preparing";
		statusNames[4] = "Order Delivered";
		statusNames[5] = "Ready to Pay";
	}
	/*
	*@author Garrett Thompson
	* Server-side constructor for the object. Introduces new variables to account for the extended
	* node-specific features of the server-side interface.
	* @param uniqueID  A unique four-digit identification string for the node to ensure that it is unique on the network
	* @param xPos  X coordinate for the node's location
	* @param yPos  Y coordinate for the node's location
	* @param myView  LayoutView object this node will be drawn by. I don't think this is being used in our current build but I don't want to break anything by taking it out.
	*/
	TableNode(String uniqueID, int xPos, int yPos, LayoutView myView) throws NumberFormatException, IOException {
		view = myView;
		nodeID = uniqueID;
		tableStatus = 0;
		isSelected = false;
		isClientSide = false;
		x = xPos;
		y = yPos;
		oldX = x;
		oldY = y;
		xOffset = -6;
		yOffset = -22;
		selectYOffset = 32;
		synched = false;
		genNotif = 0;
		refillNotif = 0;
		createOrder();
		this.control = new orderController(new tableOrder(nodeID));
		
		try {
			nodeIcon = ImageIO.read(new File("node0_1.png"));
			dialogBox = ImageIO.read(new File("nodeDialogBox.png"));
			trashCan = ImageIO.read(new File("trash.png"));
			button = ImageIO.read(new File("button0.png"));
			button2 = ImageIO.read(new File("button1.png"));
			orderButton = ImageIO.read(new File ("orderButton.png"));
			notifIcon = ImageIO.read(new File("notificon.png"));
			advanceIcon = ImageIO.read(new File("serversnapbutton.png"));
		}
		catch (IOException e) {
		}
		dialogBoxX = nodeIcon.getWidth();
		dialogBoxY = nodeIcon.getHeight()+yOffset*3;
		trashX = dialogBoxX+dialogBox.getWidth()/3+dialogBox.getWidth()/28;
		trashY = dialogBoxY+dialogBox.getHeight()+yOffset*5;
		buttonX = dialogBoxX+dialogBox.getWidth()/5;
		buttonY = dialogBoxY-yOffset;
		//advanceButtonMask = new Ellipse2D.Double(x+buttonX, y+buttonY+orderButton.getHeight()+16, advanceIcon.getWidth(), advanceIcon.getHeight());
	}
	/* @author Garrett Thompson
	* Generates a new empty order to be stored in the table node. 
	*/
	public void createOrder() {
		thisOrder = new tableOrder(nodeID);
	}
	/* @author Garrett Thompson
	* Removes the order currently stored in the node
	*/
	public void removeOrder() {
		thisOrder = null;
	}
	/* @author Garrett Thompson
	* Advances node status between values 0 and 5, to represent each possible status of the node.
	*/
	public void advanceStatus() {
		if (tableStatus < 5) {
			tableStatus++;
		}
		else {
			tableStatus = 0;
		}
		if (!isClientSide) {
			updateNodeImage();
			view.myController.repaint();
		}
	}
	/* @author Garrett Thompson
	* Updates image of node based on current node status.
	*/
	public void updateNodeImage() {
		File imgFile = null;
		if (tableStatus == 0) {
			imgFile = new File("node1_1.png");
		}
		if (tableStatus == 1) {
			imgFile = new File("node2_1.png");
		}
		if (tableStatus == 2) {
			imgFile = new File("node3_1.png");
		}
		if (tableStatus == 3) {
			imgFile = new File("node4_1.png");
		}
		if (tableStatus == 4) {
			imgFile = new File("node5_1.png");
		}
		if (tableStatus == 5) {
			imgFile = new File("node6_1.png");
		}
		if (tableStatus == 6) {
			imgFile = new File("node6_1.png");
		}
		try {
			nodeIcon = ImageIO.read(imgFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/* @author Garrett Thompson
	* Basically the reverse of advanceStatus(). Steps back status by one, unless it's already at zero.
	*/
	public void stepBackStatus() {
		if (tableStatus > 0) {
			tableStatus--;
		}
		updateNodeImage();
		view.myController.repaint();
	}
	/* @author Garrett Thompson
	* Sets table status back to its initial state, zero.
	*/
	public void resetStatus() {
		tableStatus = 0;
		tableOrder = null;
		updateNodeImage();
		view.myController.repaint();
	}
	/* @author Garrett Thompson
	* @returns Current table status
	*/
	public int getStatus() {
		return tableStatus;
	}
	/* @author Garrett Thompson
	* Changes x position based on paramter.
	* @param newX  the x coordinate to change this node's x position to.
	*/
	public void modifyXPos(int newX) {
		x = newX;
	}
	/* @author Garrett Thompson
	* Changes y position based on parameter.
	* @param newY  the y coordinate to change this node's x position to.
	*/
	public void modifyYPos(int newY) {
		y = newY;
	}
	/* @author Garrett Thompson
	* Increments the tally of general notifications sent to/from the table node.
	*/
	public void incrementGenNotif() {
		genNotif++;
	}
	/* @author Garrett Thompson
	* Increments the tally of refill notifications sent to/from this table node. Not currently implemented.
	*/
	public void incrementRefillNotif() {
		refillNotif++;
	}
	/* @author Garrett Thompson
	* Reverts notifications to zero, thus "acknowledging" them.
	*/
	public void acknowledgeNotif() {
		refillNotif = 0;
		genNotif = 0;
	}
}
