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
	BufferedImage button2;
	BufferedImage orderButton;
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
	TableNode(String uniqueID, ClientController myController) {
		ctrl = myController;
		isClientSide = true;
	}
	TableNode(String uniqueID, int xPos, int yPos, LayoutView myView) {
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
		try {
			nodeIcon = ImageIO.read(new File("node0_1.png"));
			dialogBox = ImageIO.read(new File("nodeDialogBox.png"));
			trashCan = ImageIO.read(new File("trash.png"));
			button = ImageIO.read(new File("button0.png"));
			button2 = ImageIO.read(new File("button1.png"));
			orderButton = ImageIO.read(new File ("orderButton.png"));
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
		if (tableStatus < 6) {
			tableStatus++;
		}
		else {
			tableStatus = 0;
		}
		updateNodeImage();
		view.myModel.repaint();
	}
	private void updateNodeImage() {
		File imgFile = null;
		if (tableStatus == 0) {
			imgFile = new File("node0_1.png");
		}
		if (tableStatus == 1) {
			imgFile = new File("node1_1.png");
		}
		if (tableStatus == 2) {
			imgFile = new File("node2_1.png");
		}
		if (tableStatus == 3) {
			imgFile = new File("node3_1.png");
		}
		if (tableStatus == 4) {
			imgFile = new File("node4_1.png");
		}
		if (tableStatus == 5) {
			imgFile = new File("node5_1.png");
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
	public void stepBackStatus() {
		if (tableStatus > 0) {
			tableStatus--;
		}
		updateNodeImage();
		view.myModel.repaint();
	}
	public void resetStatus() {
		tableStatus = 0;
		myOrder = null;
		updateNodeImage();
		view.myModel.repaint();
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
