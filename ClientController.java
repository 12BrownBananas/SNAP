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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        myView.frame.addMouseListener(this);
        myView.frame.addMouseMotionListener(this);
        hasView = true;
        mainButtonBounds = new Ellipse2D.Double(myView.width/4-xOffset, myView.height/8+yOffset, snapButton.getWidth(), snapButton.getHeight());
        notifButtonBounds = new Ellipse2D.Double(myView.width-notifButton.getWidth()-xOffset, myView.height/20+yOffset, notifButton.getWidth(), notifButton.getHeight());
        orderButtonBounds = new Ellipse2D.Double(myView.width-orderButton.getWidth()-xOffset, myView.height/2, orderButton.getWidth(), orderButton.getHeight());
    }
    public void updateNode(TableNode node) {
    	if (node.nodeID == myNode.nodeID) {
    		myNode.tableStatus = node.tableStatus;
    		myNode.genNotif = node.genNotif;
    		myNode.tableOrder = node.tableOrder;
    	}
    }
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
	

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
	
		
	}

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

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	public boolean mouseOverNotifButton(int x, int y) {
		if (notifButtonBounds.contains(x, y)) {
			return true;
		}
		return false;
	}
	public boolean mouseOverMainButton(int x, int y) {
		if (mainButtonBounds.contains(x, y)) {
			return true;
		}
		return false;
	}
	public boolean mouseOverOrderButton(int x, int y) {
		if (orderButtonBounds.contains(x, y)) {
			return true;
		}
		return false;
	}
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