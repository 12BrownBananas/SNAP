import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import java.awt.Graphics;
import java.awt.event.*;
import java.awt.image.BufferedImage;

/*
* @author Garrett Thompson, Jordan Abbatiello
* Created: November 17th
* Last Edited: December 2nd
* LayoutController.java
* The controller object for the server version of SNAP. Works with the LayoutView and NetworkServer to store information
* relating to the current state of table nodes in the layout.
* Expected revisions: Ability to directly access menu database, ability to access a menu for settings adjustment
*/
public class LayoutController extends JPanel implements MouseListener, MouseMotionListener, KeyListener {
	private LayoutView view;
	boolean isHoldingNode;
	boolean isPressingButton;
	TableNode nodeGrabbed;
	ArrayList<TableNode> nodes = new ArrayList<TableNode>();
	ArrayList<TableNode> clientNodes = new ArrayList<TableNode>();
	ArrayList<String> openIds = new ArrayList<String>();
	int currentId;
	boolean isSelecting;
	boolean canMakeNode;
	boolean canStillAddToNodes;
	NetworkServer network;
	BufferedImage layout;
	ArrayList<ClientController> myClients;
	
	NetworkServer myNetwork;
	/*
	* @author Garrett Thompson
	* Constructor method for LayoutController. The second component of the
	* server-side interface in creation order. Creates its own view.
	* Initializes variables to keep track of its state. Adds listeners to
	* its view such that user input can be accepted. 
	* @throws IOException
	* @throws InterruptedException
	*/
	public LayoutController() throws IOException, InterruptedException {
		//network = ntwk;
		view = new LayoutView(this);
		isHoldingNode = false;
		currentId = 0;
		isSelecting = false;
		isPressingButton = false;
		canMakeNode = true;
		canStillAddToNodes = false;
		view.frame.addMouseListener(this);
		view.frame.addMouseMotionListener(this);
		view.frame.addKeyListener(this);
		layout = ImageIO.read(new File("floorplan.png")); //Placeholder floorplan pulled from http://evstudio.com/giovanni-italian-restaurant-floor-plans/
		repaint();
	}
	/*
	* @author Garrett Thompson
	* Takes a given TableNode object already stored in the LayoutController and 
	* updates its relevant internal variables to be equivalent to the supplied model node.
	* Used when a node object is sent through the network to be synchronized with its corresponding
	* server-side node.
	* @param node  The model node in question.
	*/
	public void updateNode(TableNode node) {
		for (int i = 0; i < nodes.size(); i++) {
			if (nodes.get(i).nodeID.equals(node.nodeID)) {
				nodes.get(i).tableStatus = node.tableStatus;
				nodes.get(i).genNotif = node.genNotif;
				nodes.get(i).tableOrder = node.tableOrder;
				nodes.get(i).updateNodeImage();
				repaint();
			}
		}
	}
	/*
	* @author Garrett Thompson
	* Checks the network to see if the supplied node ID corresponds to any existing nodes in its
	* registry of client-side table nodes. Presently doesn't do anything.
	* @param id  The nodeID of the node to check for.
	*/
	private boolean isNodeInServer(String id) {
		String node = null;
		//node = network.findNode(id);
		if (node != null) {
			return true;
		}
		return false;
	}
	/* 
	* @author Garrett Thompson
	* An override of the MouseListener's mouseClicked event. Called every time the user
	* inputs a mouse click (press, no movement, release). At the moment, used to interact
	* with two node dialog box buttons: trash and advance. Trash deletes a node, whereas
	* advance moves the node's status forward.
	* @param e  The mouse event.
	*/
	@Override
	public void mouseClicked(MouseEvent e) {
		if (isOverDialogBox(e.getX(), e.getY())) {
			for (int i = 0; i < nodes.size(); i++) {
				if (nodes.get(i).isSelected) {
					if (isOverTrash(e.getX(), e.getY(), nodes.get(i))) {
						openIds.add(nodes.get(i).nodeID);
						nodes.remove(i);
						repaint();
						canMakeNode = true;
					}
					if (isOverAdvance(e.getX(), e.getY(), nodes.get(i))) {
						nodes.get(i).advanceStatus();
						for (int j = 0; j < clientNodes.size(); j++) {
							if (clientNodes.get(j).nodeID.equals(nodes.get(i).nodeID)) {
								clientNodes.get(j).advanceStatus();
							}
						}
					}
				}
			}
		}
	}
	/*
	* @author Garrett Thompson
	* Checks to see if the mouse is hovering over the advance node button, which is located in 
	* the table node dialog box, but only if the node has been synched with the server. Useful for seeing
	* if the advance button is being selected or not.
	* @param x   X coordinate of the mouse.
	* @param y   Y coordinate of the mouse.
	* @param node  The table node whose advance button we're checking to see if we're hovering over.
	*/
	public boolean isOverAdvance(int x, int y, TableNode node) {
		if (node.isSelected) {
			int leftX = node.x+node.buttonX;
			int rightX = leftX+node.advanceIcon.getWidth();
			int topY = node.y+node.buttonY+node.orderButton.getHeight()+16;
			int botY = topY+node.advanceIcon.getHeight();
			if (x > leftX && x < rightX) {
				if (y > topY+32 && y < botY+32) {
					return true;
				}
			}			
		}
		return false;
	}
	/* 
	* Arbitrary mouseListener event stub override required by Java.
	*/
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	/* 
	* Arbitrary mouseListener event stub override required by Java.
	*/
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	/*
	* @author Garrett Thompson, Jordan Abbatiello
	* Override of the mousePressed method in MouseListener. Used primarily for preparing to select
	* GUI buttons (the actual selection doesn't occur until mouseReleased) and generating nodes on
	* the layout. 
	*
	* For further extensions of this code, I recommend placing the node generation code found here
	* in its own separate method.
	*
	* @param e  The mouse pressed event.
	*/
	@Override
	public void mousePressed(MouseEvent e) {
		
		if (!isOverDialogBox(e.getX(), e.getY())) {
			for (int i = 0; i < nodes.size(); i++) {
				if (nodes.get(i).isSelected) {
					nodes.get(i).isSelected = false;
					repaint();
				}
			}
		}
		if (SwingUtilities.isRightMouseButton(e)) {
			if (isOverNode(e.getX(), e.getY())) {
				isSelecting = true;
			}
		}
		
		if (SwingUtilities.isLeftMouseButton(e)) {
			if (!isHoldingNode && !isOverDialogBox(e.getX(), e.getY())) {
				if (!isOverNode(e.getX(), e.getY()) && canMakeNode) {
					String idString = "";
					if (openIds.size() <= 0) {
						if (currentId < 10) {
							idString = "000"+Integer.toString(currentId);
						}
						else if (currentId < 100) {
							idString = "00"+Integer.toString(currentId);
						}
						else if (currentId < 1000) {
							idString = "0"+Integer.toString(currentId);
						}
						else if (currentId < 10000) {
							idString = Integer.toString(currentId);
						}
						else {
							System.out.println("Error: ID space exhausted.");
						}
						currentId++;
					}
					else {
						idString = openIds.get(openIds.size()-1);
						openIds.remove(openIds.size()-1);
						canStillAddToNodes = true;
					}
					if (currentId <= 9999 || canStillAddToNodes) {
						try {
							nodes.add(new TableNode(idString, e.getX(), e.getY(), view));
						} catch (NumberFormatException | IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						nodes.get(nodes.size()-1).x = nodes.get(nodes.size()-1).x-nodes.get(nodes.size()-1).nodeIcon.getWidth()/2+nodes.get(nodes.size()-1).xOffset;
						nodes.get(nodes.size()-1).y = nodes.get(nodes.size()-1).y-nodes.get(nodes.size()-1).nodeIcon.getHeight()/2+nodes.get(nodes.size()-1).yOffset;
						//myNetwork.allNodes.add(nodes.get(nodes.size()-1));
						canStillAddToNodes = false;
						repaint();
					}
				}
				canMakeNode = true;
			}
			if (isOverNode(e.getX(), e.getY()) && !isOverDialogBox(e.getX(), e.getY())) {
				//The mouse is hovering over a node at this point

					isHoldingNode = true;
					nodeGrabbed = nodeOver(e.getX(), e.getY());
					nodeGrabbed.x = e.getX()-nodeGrabbed.nodeIcon.getWidth()/2+nodeGrabbed.xOffset;
					nodeGrabbed.y = e.getY()-nodeGrabbed.nodeIcon.getWidth()/2+nodeGrabbed.yOffset;
					try {
						if (nodeGrabbed.tableStatus == 0) {
							nodeGrabbed.nodeIcon = ImageIO.read(new File("node0_2.png"));
						}
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					repaint();
			}
			for (int i = 0; i < nodes.size(); i++) {
				if (nodes.get(i).isSelected) {
					if (isOverButton(e.getX(), e.getY(), nodes.get(i))) {
						isPressingButton = true;
						nodes.get(i).synched = true;
						if (nodes.get(i).synched) {
							try {
								nodes.get(i).orderButton = ImageIO.read(new File ("orderButton2.png"));
								new LayoutView(nodes.get(i).control);
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
						repaint();
					}
				}
			}
		}
	}
	/*
	* @author Garrett Thompson, Jordan Abbatiello
	* An override of the MouseListener mouseReleased event which is used primarily for selecting
	* buttons once they've been pressed, setting the position of nodes on the layout, and selecting and deselecting
	* nodes.
	* @param e  The mouse released event.
	*/
	@Override
	public void mouseReleased(MouseEvent e) {
		if (SwingUtilities.isRightMouseButton(e)) {
			boolean canSelect = true;
			for (int i = 0; i < nodes.size(); i++) {
				if (nodes.get(i).isSelected) {
					canSelect = false;
				}
			}
			if (canSelect) {
				if (isOverNode(e.getX(), e.getY())) {
					nodeOver(e.getX(), e.getY()).isSelected = true;
					nodeOver(e.getX(), e.getY()).genNotif = 0;
					for (int i = 0; i < clientNodes.size(); i++) {
						if (clientNodes.get(i).nodeID.equals(nodeOver(e.getX(), e.getY()).nodeID)) {
							clientNodes.get(i).genNotif = 0;
						}
					}
					canMakeNode = false;
					repaint();
				}
			}
			isSelecting = false;
		}
		if (SwingUtilities.isLeftMouseButton(e)) {
			if (isPressingButton) {
				for (int i = 0; i < nodes.size(); i++) {
					if (nodes.get(i).isSelected) {
						if (nodes.get(i).synched) {
							try {
								nodes.get(i).orderButton = ImageIO.read(new File ("orderButton.png"));
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					}
				}
				isPressingButton = false;
				repaint();
			}
			
			if (isHoldingNode && !isOverDialogBox(e.getX(), e.getY())){
				if (nodeGrabbed != null) {
					try {
						if (nodeGrabbed.tableStatus == 0) {
							nodeGrabbed.nodeIcon = ImageIO.read(new File("node0_1.png"));
						}
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					isHoldingNode = false;
					nodeGrabbed.x = e.getX()-nodeGrabbed.nodeIcon.getWidth()/2+nodeGrabbed.xOffset;
					nodeGrabbed.y = e.getY()-nodeGrabbed.nodeIcon.getWidth()/2+nodeGrabbed.yOffset;
					nodeGrabbed.oldX = nodeGrabbed.x;
					nodeGrabbed.oldY = nodeGrabbed.y;
					nodeGrabbed = null;
					repaint();
				}
			}
		}
	}
	/*
	* @author Garrett Thompson
	* Similar to the isOverAdvance method, this checks to see if the mouse is over the trash button
	* on the table node dialog box. Useful for checking to see if that button should be selected or not.
	* @param x  X coordinate of the mouse.
	* @param y  Y coordinate of the mouse.
	* @param node  Table Node object whose button we're checking against.
	*/
	public boolean isOverTrash(int x, int y, TableNode node) {
		if (node.synched == false) {
			if (x > node.x+node.trashX && x < node.x+node.trashX+node.trashCan.getWidth()) {
				if (y > node.y+node.trashY+node.selectYOffset && y < node.y+node.trashY+node.trashCan.getHeight()+node.selectYOffset) {
					return true;
				}
	 		}
		}
		return false;
	}
	
	/*
	* @author Garrett Thompson
	* Checks to see if one node (our "first" node) is colliding with another ("second" node), for example, by sharing the same space.
	* Formerly used to make sure that nodes would not overlap on the layout, this functionality was
	* removed later on in development after user testing revealed it was irritating and unhelpful.
	* @param x1  Leftmost coordinate of the first node.
	* @param y1 Topmost coordinate of the first node.
	* @param x2  Rightmost coordinate of the first node.
	* @param y2  Bottommost coordinate of the first node.
	* @param node  The first node object, whose bounding box is defined by the four previously-given coordinates.
	*/
	
	public boolean collisionWithNode(int x1, int y1, int x2, int y2, TableNode node) { //The node is the node that we DON'T check against
		if (x1 > x2) {
			int temp = x1;
			x1 = x2;
			x2 = temp;
		}
		if (y2 < y1) {
			int temp = y2;
			y2 = y1;
			y1 = temp;
		}
		for (int k = 0; k < nodes.size(); k++) { //Checks every node
			if (nodes.get(k).nodeID != node.nodeID) { //As long as the node we're checking against is NOT the first node.
				if ((nodes.get(k).x > x1 && nodes.get(k).x < x2) || (nodes.get(k).x+(x2-x1) < x2 && nodes.get(k).x+(x2-x1) >= x1) || (nodes.get(k).x == x1)) {
					if ((nodes.get(k).y > y1 && nodes.get(k).y < y2) || (nodes.get(k).y+(y2-y1) < y2 && nodes.get(k).y+(y2-y1) > y1) || nodes.get(k).y == y1) {
						return true; //We reach this point in code if any of the four coordinates given to this method overlap with 
							//any of the bounding boxes of other ("second") nodes.
					}
				}
			}
		}
		return false;
	}
	
	/*
	* @author Garrett Thompson
	* Checks to see if the mouse is hovering over a node, which is used to determine if a node is selectable.
	* @param x  X coordinate of the mouse.
	* @param y  Y coordinate of the mouse.
	*/
	
	private boolean isOverNode(int x, int y) {
		for (int k = 0; k < nodes.size(); k++) {
			if ((nodes.get(k).x < x && nodes.get(k).x+nodes.get(k).nodeIcon.getWidth() > x)) {
				if (nodes.get(k).y+nodes.get(k).selectYOffset < y && nodes.get(k).y+nodes.get(k).nodeIcon.getHeight()+nodes.get(k).selectYOffset > y) {
					return true;
				}
			}
		}
		return false;
	}
	/* 
	* @author Garrett Thompson
	* If we are over a node, it's useful to know which node object specifically we're hovering over with the mouse.
	* This method returns the reference to the node object which the mouse is currently over.
	* @param x  X coordinate of the mouse.
	* @param y  Y coordinate of the mouse.
	*/
	private TableNode nodeOver(int x, int y) {
		for (int k = 0; k < nodes.size(); k++) {
			if ((nodes.get(k).x < x && nodes.get(k).x+nodes.get(k).nodeIcon.getWidth() > x)) {
				if (nodes.get(k).y+nodes.get(k).selectYOffset < y && nodes.get(k).y+nodes.get(k).nodeIcon.getHeight()+nodes.get(k).selectYOffset > y) {
					return nodes.get(k);
				}
			}
		}
		return null;
	}
	/*
	* @author Garrett Thompson
	* Checks to see if the mouse is over a node dialog box. Can only happen if a node has been selected, thus
	* causing its dialog box to be visible. Useful for seeing if node buttons can be pressed.
	* @param x  X coordinate of the mouse.
	* @param y  Y coordinate of the mouse.
	*/
	private boolean isOverDialogBox(int x, int y) {
		for (int i = 0; i < nodes.size(); i++) {
			if (nodes.get(i).isSelected) {
				if (x > nodes.get(i).x+nodes.get(i).dialogBoxX && x < nodes.get(i).x+nodes.get(i).dialogBoxX+ nodes.get(i).dialogBox.getWidth()){
					if (y > nodes.get(i).y+nodes.get(i).dialogBoxY+nodes.get(i).selectYOffset && y < nodes.get(i).y+nodes.get(i).dialogBoxY+ nodes.get(i).dialogBox.getHeight()+nodes.get(i).selectYOffset){
						return true;
					}	
				}
			}
		}
		return false;
	}
	/*
	* @author Garrett Thompson
	* Checks to see if the mouse is over the main button (sync node or view order, depending on sync status)
	* of a node's dialog box. Used to see if the main button is being clicked.
	* @param x  X coordinate of the mouse
	* @param y  Y coordinate of the mouse
	* @param node  Table node we're checking against.
	*/
	private boolean isOverButton(int x, int y, TableNode node) {
		if (x > node.x+node.buttonX && x < node.x+node.buttonX+node.button.getWidth()) {
			if (y > node.y+node.buttonY+node.selectYOffset && y < node.y+node.buttonY+node.button.getHeight()+node.selectYOffset) {
				return true;
			}
 		}
		return false;
	}
	
	/* 
	* @author Garrett Thompson
	* This method dictates how the states of this object are represented graphically. This component is
	* later drawn to the screen in the LayoutView object.
	* @param g  The graphics context for drawing.
	*/
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(layout, 0, 0, null);
		for (int i = 0; i < nodes.size(); i++) {
			g.drawImage(nodes.get(i).nodeIcon, nodes.get(i).x, nodes.get(i).y, null);
			if (nodes.get(i).genNotif > 0) {
				g.drawImage(nodes.get(i).notifIcon, nodes.get(i).x, nodes.get(i).y,null);
			}
		}
		for (int i = 0; i < nodes.size(); i++) { //We're going to iterate through every node in creation order and draw them at their respective coordinates in the same way.
			if (nodes.get(i).isSelected) {
				g.drawImage(nodes.get(i).dialogBox, nodes.get(i).x+nodes.get(i).dialogBoxX, nodes.get(i).y+nodes.get(i).dialogBoxY, null);
				g.drawString("#"+nodes.get(i).nodeID, nodes.get(i).x+nodes.get(i).dialogBoxX-nodes.get(i).xOffset*10, nodes.get(i).y+nodes.get(i).dialogBoxY-nodes.get(i).yOffset-2);
				if (!nodes.get(i).synched) {
					g.drawImage(nodes.get(i).trashCan, nodes.get(i).x+nodes.get(i).trashX, nodes.get(i).y+nodes.get(i).trashY, null);
					g.drawString("Delete Node", nodes.get(i).x+nodes.get(i).trashX-10, nodes.get(i).y+nodes.get(i).trashY+nodes.get(i).trashCan.getHeight()+nodes.get(i).selectYOffset/2);
					if (!isPressingButton) {
						g.drawImage(nodes.get(i).button, nodes.get(i).x+nodes.get(i).buttonX, nodes.get(i).y+nodes.get(i).buttonY, null);
					}
					else {
						g.drawImage(nodes.get(i).button2, nodes.get(i).x+nodes.get(i).buttonX, nodes.get(i).y+nodes.get(i).buttonY, null);
					}
				}
				else {
					g.drawImage(nodes.get(i).orderButton, nodes.get(i).x+nodes.get(i).buttonX, nodes.get(i).y+nodes.get(i).buttonY, null);
					g.drawImage(nodes.get(i).advanceIcon, nodes.get(i).x+nodes.get(i).buttonX, nodes.get(i).y+nodes.get(i).buttonY+nodes.get(i).orderButton.getHeight()+16, null);
				}
			}
		}
	}
	/*
	* @author Garrett Thompson
	* This override is mainly for quality of life purposes-- if the mouse is dragged off of a button before we select that button, 
	* the button is considered deselected and will not be activated when the mouse is released.
	* @param arg0  The mouse event.
	*/
	@Override
	public void mouseDragged(MouseEvent arg0) {
		if (isHoldingNode) {
			nodeGrabbed.x = arg0.getX()-nodeGrabbed.nodeIcon.getWidth()/2+nodeGrabbed.xOffset;
			nodeGrabbed.y = arg0.getY()-nodeGrabbed.nodeIcon.getWidth()/2+nodeGrabbed.yOffset;
			repaint();	
		}
		if (isPressingButton) {
			for (int i = 0; i < nodes.size(); i++) {
				if (nodes.get(i).isSelected) {
					if (!isOverButton(arg0.getX(), arg0.getY(), nodes.get(i))) {
						if (nodes.get(i).synched) {
							try {
								nodes.get(i).orderButton = ImageIO.read(new File ("orderButton.png"));
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
						isPressingButton = false;
						repaint();
					}
				}
			}
		}
	}
	
	/* 
	* Arbitrary MouseMovementListener event stub override required by Java.
	*/
	@Override
	public void mouseMoved(MouseEvent arg0) {
		
	}

	/* 
	* Arbitrary KeyListener event stub override required by Java.
	*/
	@Override
	public void keyPressed(KeyEvent arg0) {

		
	}

	/* 
	* Arbitrary KeyListener event stub override required by Java.
	*/
	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	/*
	* @author Garrett Thompson
	* Creates a new client every time a key is pressed. Used for debugging client-side interface. Should be removed
	* for final build.
	*/

	@Override
	public void keyTyped(KeyEvent arg0) {
		NetworkClient client = null;
		try {
			client = new NetworkClient();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			client.run();
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		client.myController.myLayout = this;
		clientNodes.add(client.myController.myNode);
		client.myController.repaint();
		client.myController.checkSync();
		
	}
}
