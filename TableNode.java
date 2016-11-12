
public class TableNode {
	Order myOrder;
	String nodeID;
	int tableStatus;
	int x;
	int y;
	int genNotif; //However many unacknowledged general notifications we have
	int refillNotif;
	TableNode(String uniqueID, int xPos, int yPos) {
		nodeID = uniqueID;
		tableStatus = 0;
		x = xPos;
		y = yPos;
		genNotif = 0;
		refillNotif = 0;
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
