import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.*;

public class TableNode extends JPanel  {
	Order myOrder;
	String nodeID;
	int tableStatus;
	int x;
	int y;
	int genNotif; //However many unacknowledged general notifications we have
	int refillNotif;
	ImageIcon nodeIcon;
	LayoutView view;
	TableNode(String uniqueID, int xPos, int yPos, LayoutView myView) {
		nodeID = uniqueID;
		tableStatus = 0;
		x = xPos;
		y = yPos;
		genNotif = 0;
		refillNotif = 0;
		nodeIcon = new ImageIcon("node0.png");
		view = myView;
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		nodeIcon.paintIcon(view, g, (int)x, (int)y);
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
