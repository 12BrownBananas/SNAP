import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import java.awt.Graphics;
import java.awt.event.*;
import java.awt.image.BufferedImage;


public class LayoutController extends JPanel implements MouseListener, MouseMotionListener  {
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
	
	NetworkServer myNetwork;
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
		layout = ImageIO.read(new File("floorplan.png")); //Placeholder floorplan pulled from http://evstudio.com/giovanni-italian-restaurant-floor-plans/
		repaint();
	}
	
	public void updateNode(TableNode node) {
		//for (int i = 0; i < )
	}
	private boolean isNodeInServer(String id) {
		String node = null;
		//node = network.findNode(id);
		if (node != null) {
			return true;
		}
		return false;
	}

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
				}
			}
		}
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
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
						nodes.add(new TableNode(idString, e.getX(), e.getY(), view));
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
				if (!nodeOver(e.getX(), e.getY()).synched) {
					isHoldingNode = true;
					nodeGrabbed = nodeOver(e.getX(), e.getY());
					nodeGrabbed.x = e.getX()-nodeGrabbed.nodeIcon.getWidth()/2+nodeGrabbed.xOffset;
					nodeGrabbed.y = e.getY()-nodeGrabbed.nodeIcon.getWidth()/2+nodeGrabbed.yOffset;
					try {
						nodeGrabbed.nodeIcon = ImageIO.read(new File("node0_2.png"));
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					repaint();
				}
			}
			for (int i = 0; i < nodes.size(); i++) {
				if (nodes.get(i).isSelected) {
					if (isOverButton(e.getX(), e.getY(), nodes.get(i))) {
						isPressingButton = true;
						if (nodes.get(i).synched) {
							try {
								nodes.get(i).orderButton = ImageIO.read(new File ("orderButton2.png"));
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
						nodeGrabbed.nodeIcon = ImageIO.read(new File("node0_1.png"));
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
		for (int k = 0; k < nodes.size(); k++) {
			if (nodes.get(k).nodeID != node.nodeID) {
				if ((nodes.get(k).x > x1 && nodes.get(k).x < x2) || (nodes.get(k).x+(x2-x1) < x2 && nodes.get(k).x+(x2-x1) >= x1) || (nodes.get(k).x == x1)) {
					if ((nodes.get(k).y > y1 && nodes.get(k).y < y2) || (nodes.get(k).y+(y2-y1) < y2 && nodes.get(k).y+(y2-y1) > y1) || nodes.get(k).y == y1) {
						return true;
					}
				}
			}
		}
		return false;
	}
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
	
	private boolean isOverButton(int x, int y, TableNode node) {
		if (x > node.x+node.buttonX && x < node.x+node.buttonX+node.button.getWidth()) {
			if (y > node.y+node.buttonY+node.selectYOffset && y < node.y+node.buttonY+node.button.getHeight()+node.selectYOffset) {
				return true;
			}
 		}
		return false;
	}
	
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(layout, 0, 0, null);
		for (int i = 0; i < nodes.size(); i++) {
			g.drawImage(nodes.get(i).nodeIcon, nodes.get(i).x, nodes.get(i).y, null);
		}
		for (int i = 0; i < nodes.size(); i++) {
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
				}
			}
		}
	}

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

	@Override
	public void mouseMoved(MouseEvent arg0) {
		
	}
}
