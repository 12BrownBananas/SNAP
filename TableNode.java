import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

public class TableNode  {
	Order myOrder;
	String nodeID;
	int tableStatus;
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
	BufferedImage button2;
	int buttonX;
	int buttonY;
	int dialogBoxX;
	int dialogBoxY; //Both of these are relative to the x and y coordinate of the node
	int trashX;
	int trashY;
	boolean isSelected;
	LayoutView view;
	TableNode(String uniqueID, int xPos, int yPos, LayoutView myView) {
		view = myView;
		nodeID = uniqueID;
		tableStatus = 0;
		isSelected = false;
		x = xPos;
		y = yPos;
		oldX = x;
		oldY = y;
		xOffset = -6;
		yOffset = -22;
		selectYOffset = 32;
		genNotif = 0;
		refillNotif = 0;
		try {
			nodeIcon = ImageIO.read(new File("node0_1.png"));
			dialogBox = ImageIO.read(new File("nodeDialogBox.png"));
			trashCan = ImageIO.read(new File("trash.png"));
			button = ImageIO.read(new File("button0.png"));
			button2 = ImageIO.read(new File("button1.png"));
		}
		catch (IOException e) {
		}
		dialogBoxX = nodeIcon.getWidth();
		dialogBoxY = nodeIcon.getHeight()+yOffset*3;
		trashX = dialogBoxX+dialogBox.getWidth()/3+dialogBox.getWidth()/28;
		trashY = dialogBoxY+dialogBox.getHeight()+yOffset*5;
		buttonX = dialogBoxX+dialogBox.getWidth()/5;
		buttonY = dialogBoxY-yOffset;
	}
	public void createOrder() {
		myOrder = new Order();
	}
	public void removeOrder() {
		myOrder = null;
	}
	public void advanceStatus() {
		if (tableStatus < 7) {
			tableStatus++;
		}
		else {
			tableStatus = 0;
		}
	}
	public void stepBackStatus() {
		if (tableStatus > 0) {
			tableStatus--;
		}
	}
	public void resetStatus() {
		tableStatus = 0;
		myOrder = null;
	}
	public int getStatus() {
		return tableStatus;
	}
	public void modifyXPos(int newX) {
		x = newX;
	}
	public void modifyYPos(int newY) {
		y = newY;
	}
	public void incrementGenNotif() {
		genNotif++;
	}
	public void incrementRefillNotif() {
		refillNotif++;
	}
	public void acknowledgeNotif() {
		refillNotif = 0;
		genNotif = 0;
	}
}
