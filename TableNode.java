
public class TableNode {
	Order myOrder;
	String nodeID;
	int tableStatus;
	TableNode(String uniqueID) {
		nodeID = uniqueID;
		tableStatus = 0;
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
}
