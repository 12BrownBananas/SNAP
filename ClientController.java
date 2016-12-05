import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import java.awt.*;
/*
* @author Garrett Thompson
* Created: November 16th
* Last Edited: November 27th
* ClientController.java
* Controller object for the client-side interface. Handles things such as accepting user input
* and sending notifications to the server.
* Expected Revisions: Adding in displaying order functionality, different types of notifications.
*/ 

public class ClientController extends JPanel implements ActionListener, MouseListener, MouseMotionListener {
	protected JTextField textField;
    protected JTextArea textArea;
    boolean hasView;
    String id;
    boolean idEntered;
    static JFrame frame;
    private final static String newline = "\n";
    ClientView myView;
    JFrame frame2;
    BufferedImage snapButton;
    BufferedImage notifButton;
    BufferedImage orderButton;
    TableNode myNode;
    NetworkClient myNetwork;
    int xOffset;
    int yOffset;
    boolean synched;
    
    boolean mainButtonPressed;
    boolean overMainButton;
    boolean notifButtonPressed;
    boolean overNotifButton;
    boolean orderButtonPressed;
    boolean overOrderButton;
    
    Ellipse2D mainButtonBounds;
    Ellipse2D notifButtonBounds;
    Ellipse2D orderButtonBounds;
    
    LayoutController myLayout;
    /* @author Garrett Thompson
    * Constructor for the ClientController. Creates a new client controller after a new ID has been allocated
    * to it by the NetworkClient. 
    * @param id  The unique ID for this client's corresponding table node.
    * @param view  The view which draws this client's GUI.
    */
    public ClientController(String id, ClientView view) throws IOException, InterruptedException {
    	mainButtonPressed = false;
    	notifButtonPressed = false;
    	overNotifButton = false;
    	orderButtonPressed = false;
    	overOrderButton = false;
    	yOffset = 32;
    	hasView = false;
    	synched=false;
    	myView = view;
    	overMainButton = false;
    	myView.frame.setContentPane(this);
    	this.id = id;
    	myNode = new TableNode(id, this);
    	//myNetwork = ntwk;
    	xOffset = 128;
        try {
		snapButton = ImageIO.read(new File("snapbutton_unsynched.png"));
		notifButton = ImageIO.read(new File("client_notifButton_unsynched.png"));
		orderButton = ImageIO.read(new File("client_orderButton_unsynched.png"));
	} catch (IOException e) {
		e.printStackTrace();
	}
        myView.frame.addMouseListener(this);
        myView.frame.addMouseMotionListener(this);
        hasView = true;
	int widthDiv = 4;
	int heightDiv = 8;
	int heightDiv2 = 20;
	int heightDiv3 = 2;
	//The above values are used for bounding circle calculation on each GUI button.
        mainButtonBounds = new Ellipse2D.Double(myView.width/widthDiv-xOffset, myView.height/heightDiv+yOffset, snapButton.getWidth(), snapButton.getHeight());
        notifButtonBounds = new Ellipse2D.Double(myView.width-notifButton.getWidth()-xOffset, myView.height/heightDiv2+yOffset, notifButton.getWidth(), notifButton.getHeight());
        orderButtonBounds = new Ellipse2D.Double(myView.width-orderButton.getWidth()-xOffset, myView.height/heightDiv3, orderButton.getWidth(), orderButton.getHeight());
    }
    /* @author Garrett Thompson
    * This method updates this client's corresponding table node based on the information supplied by some other node (passed as a parameter)
    * @param node  The node to base the update on.
    */
    public void updateNode(TableNode node) {
    	if (node.nodeID == myNode.nodeID) {
    		myNode.tableStatus = node.tableStatus;
    		myNode.genNotif = node.genNotif;
    		myNode.tableOrder = node.tableOrder;
    	}
    }
    /* @author Garrett Thompson 
    * Checks to see if this client's node has been synched with some corresponding server-side node.
    * If not, finds a node to synchronize with. No corresponding node = no state change.
    */
    public void checkSync() {
    	if (!synched) {
	    	for (int i = 0; i < myLayout.nodes.size(); i++) {
	    		System.out.println(myLayout.nodes.get(i).nodeID);
	    		System.out.println(myNode.nodeID);
		    	if (myLayout.nodes.get(i).nodeID.equals(myNode.nodeID)) {
		    		synched = true;
		    		myLayout.nodes.get(i).synched = true;
		            try {
		    			snapButton = ImageIO.read(new File("snapbutton.png"));
		    			notifButton = ImageIO.read(new File("client_notifButton.png"));
		    			orderButton = ImageIO.read(new File("client_orderButton.png"));
		    			repaint();
		    		} catch (IOException e) {
		    			// TODO Auto-generated catch block
		    			e.printStackTrace();
		    		}
		    	}
	    	}
    	}
    }
	/* @author Garrett Thompson
	* Sets up the GUI to be drawn by the ClientView
	* @param g  the graphics context to be used in drawing.
	*/
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		int status = myNode.tableStatus;
		if (status == 0) {
			g.setColor(new Color(227, 227, 227));//.setBackground(new Color(227, 227, 227));
			g.fillRect(0, 0, myView.frame.getWidth(), myView.frame.getHeight());
		}
		if (status == 1) {
			g.setColor(new Color(229, 232, 140));
			g.fillRect(0, 0, myView.frame.getWidth(), myView.frame.getHeight());
			//myView.frame.setBackground(new Color(229, 232, 140));
		}
		if (status == 2) {
			g.setColor(new Color(157, 136, 167));
			g.fillRect(0, 0, myView.frame.getWidth(), myView.frame.getHeight());
		}
		if (status == 3) {
			g.setColor(new Color(198, 150, 82));
			g.fillRect(0, 0, myView.frame.getWidth(), myView.frame.getHeight());
		}
		if (status == 4) {
			g.setColor(new Color(77, 111, 147));
			g.fillRect(0, 0, myView.frame.getWidth(), myView.frame.getHeight());
		}
		if (status == 5) {
			g.setColor(new Color(195, 255, 192));
			g.fillRect(0, 0, myView.frame.getWidth(), myView.frame.getHeight());
		}
		g.setColor(Color.black);
		if (hasView) {
			g.drawString("Node ID: #"+id, 0, 16);
			g.drawString("Current Status: "+myNode.statusNames[myNode.tableStatus], 0, 32);
			g.drawImage(snapButton, myView.width/4-xOffset, myView.height/8, null);
			g.drawImage(notifButton, myView.width-notifButton.getWidth()-xOffset, myView.height/20, null);
			g.drawImage(orderButton, myView.width-orderButton.getWidth()-xOffset, myView.height/2, null);
			
			if (overMainButton && myNode.tableStatus < 5) {
				g.drawString("Advance Status to "+myNode.statusNames[myNode.tableStatus+1]+"?", myView.width/6+myView.width/9, snapButton.getHeight()+yOffset*3+8);
			}
		}
	}
	
	/* 
	* Arbitrary method stub required by Java in order to implement ActionListener
	*/
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	/* 
	* Arbitrary method stub required by Java in order to implement MouseMotionListener
	*/
	@Override
	public void mouseDragged(MouseEvent arg0) {
	
		
	}
	/* @author Garrett Thompson
	* Currently, this method is used to update the GUI when the user mouses over the SNAP button
	* so that the state transition that occurs when clicking the SNAP button is telegraphed.
	* @param arg0  The mouse event in question.
	*/
	@Override
	public void mouseMoved(MouseEvent arg0) {
		if (mouseOverMainButton(arg0.getX(), arg0.getY())) {
			overMainButton = true;
			repaint();
		}
		else {
			overMainButton = false;
			repaint();
		}
		
		if (mouseOverNotifButton(arg0.getX(), arg0.getY())) {
			overNotifButton = true;
			repaint();
		}
		else {
			overNotifButton = false;
			repaint();
		}
		if (mouseOverOrderButton(arg0.getX(), arg0.getY())) {
			overOrderButton = true;
			repaint();
		}
		else {
			overOrderButton = false;
			repaint();
		}
	}
	/*
	* Arbitrary method stub required by Java to implement MouseListener
	*/
	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	/*
	* Arbitrary method stub required by Java to implement MouseListener
	*/
	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	/*
	* Arbitrary method stub required by Java to implement MouseListener
	*/
	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	/* @author Garrett Thompson
	* Checks to see if the mouse is hovering over the notification button, for example to check if the notification
	* button can be clicked.
	* @param x  The x coordinate of the mouse
	* @param y  The y coordinate of the mouse
	*/
	public boolean mouseOverNotifButton(int x, int y) {
		if (notifButtonBounds.contains(x, y)) {
			return true;
		}
		return false;
	}
	/* @author Garrett Thompson
	* Checks to see if the mouse is over the SNAP button, to see if it can be selected by a mouse click.
	* @param x  The x coordinate of the mouse.
	* @param y  The y coordinate of the mouse.
	*/
	public boolean mouseOverMainButton(int x, int y) {
		if (mainButtonBounds.contains(x, y)) {
			return true;
		}
		return false;
	}
	/* @author Garrett Thompson
	* Checks to see if the mouse is over the order button, such that the order button is selectable.
	* @param x  the X coordinate of the mouse.
	* @param y  Y coordinate of the mouse
	*/
	public boolean mouseOverOrderButton(int x, int y) {
		if (orderButtonBounds.contains(x, y)) {
			return true;
		}
		return false;
	}
	/* @author Garrett Thompson
	* The mouse pressed event. Used to select buttons, but will not activate their associated button events
	* which don't occur until tripping a mouse released event with a button still selected.
	* @param arg0  The mouse event itself
	*/
	@Override
	public void mousePressed(MouseEvent arg0) {
		if (synched) {
			if (!mainButtonPressed) {
				if (overMainButton) {
					mainButtonPressed = true;
					try {
						snapButton = ImageIO.read(new File("snapbutton2.png"));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					repaint();
					myView.repaint();
				}
			}
			if (!notifButtonPressed) {
				if (overNotifButton) {
					notifButtonPressed = true;
					try {
						notifButton = ImageIO.read(new File("client_notifButton2.png"));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					repaint();
					myView.repaint();
				}
			}
			if (!orderButtonPressed) {
				if (overOrderButton) {
					orderButtonPressed = true;
					try {
						orderButton = ImageIO.read(new File("client_orderButton2.png"));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					repaint();
					myView.repaint();
				}
			}
		}
		
	}
	/* @author Garrett Thompson
	* This event triggers button events when said buttons have been selected, and does nothing otherwise.
	* @param arg0  The mouse event itself.
	*/
	@Override
	public void mouseReleased(MouseEvent arg0) {
		if (synched) {
			if (mainButtonPressed) {
				mainButtonPressed = false;
				try {
					snapButton = ImageIO.read(new File("snapbutton.png"));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				myNode.advanceStatus();
				System.out.println("New node status: "+myNode.tableStatus);
				myLayout.updateNode(myNode);
				repaint();
				myView.repaint();
			}
			if (notifButtonPressed) {
				notifButtonPressed = false;
				try {
					notifButton = ImageIO.read(new File("client_notifButton.png"));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				myNode.genNotif++;
				myLayout.updateNode(myNode);
				repaint();
				myView.repaint();
			}
			if (orderButtonPressed) {
				orderButtonPressed = false;
				try {
					orderButton = ImageIO.read(new File("client_orderButton.png"));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				myLayout.updateNode(myNode);
				repaint();
				myView.repaint();
			}
		}
		
	}
}
