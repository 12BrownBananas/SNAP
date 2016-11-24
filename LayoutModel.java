import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import java.awt.Graphics;
import java.awt.event.*;


public class LayoutModel extends JPanel implements MouseListener, MouseMotionListener  {
	private LayoutView view;
	boolean isHoldingNode;
	boolean isPressingButton;
	TableNode nodeGrabbed;
	ArrayList<TableNode> nodes = new ArrayList<TableNode>();
	int currentId;
	boolean isSelecting;
	boolean canMakeNode;
	public LayoutModel() throws IOException, InterruptedException {
		view = new LayoutView(this);
		isHoldingNode = false;
		currentId = 0;
		isSelecting = false;
		isPressingButton = false;
		canMakeNode = true;
		view.frame.addMouseListener(this);
		view.frame.addMouseMotionListener(this);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (isOverDialogBox(e.getX(), e.getY())) {
			for (int i = 0; i < nodes.size(); i++) {
				if (nodes.get(i).isSelected) {
					if (isOverTrash(e.getX(), e.getY(), nodes.get(i))) {
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
			if (isOverNode(e.getX(), e.getY()) && !isOverDialogBox(e.getX(), e.getY())) {
				//The mouse is hovering over a node at this point
				isHoldingNode = true;
				nodeGrabbed = nodeOver(e.getX(), e.getY());
			}
			for (int i = 0; i < nodes.size(); i++) {
				if (nodes.get(i).isSelected) {
					if (isOverButton(e.getX(), e.getY(), nodes.get(i))) {
						isPressingButton = true;
						repaint();
					}
				}
			}
		}
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		if (SwingUtilities.isRightMouseButton(e)) {
			if (isOverNode(e.getX(), e.getY())) {
				nodeOver(e.getX(), e.getY()).isSelected = true;
				canMakeNode = false;
				repaint();
			}
			isSelecting = false;
		}
		if (SwingUtilities.isLeftMouseButton(e)) {
			if (isPressingButton) {
				isPressingButton = false;
				repaint();
			}
			if (!isHoldingNode && !isOverDialogBox(e.getX(), e.getY())) {
				if (!isOverNode(e.getX(), e.getY()) && canMakeNode) {
					String idString = "";
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
					if (currentId <= 9999) {
						currentId++;
						nodes.add(new TableNode(idString, e.getX(), e.getY(), view));
						nodes.get(nodes.size()-1).x = nodes.get(nodes.size()-1).x-nodes.get(nodes.size()-1).nodeIcon.getWidth()/2+nodes.get(nodes.size()-1).xOffset;
						nodes.get(nodes.size()-1).y = nodes.get(nodes.size()-1).y-nodes.get(nodes.size()-1).nodeIcon.getHeight()/2+nodes.get(nodes.size()-1).yOffset;
						repaint();
					}
				}
				canMakeNode = true;
			}
			else if (!isOverDialogBox(e.getX(), e.getY())){
				if (nodeGrabbed != null) {
					if (!collisionWithNode(nodeGrabbed.x, nodeGrabbed.y, nodeGrabbed.x+nodeGrabbed.nodeIcon.getWidth(), nodeGrabbed.y+nodeGrabbed.nodeIcon.getHeight(), nodeGrabbed)) {
						isHoldingNode = false;
						nodeGrabbed.x = e.getX()-nodeGrabbed.nodeIcon.getWidth()/2+nodeGrabbed.xOffset;
						nodeGrabbed.y = e.getY()-nodeGrabbed.nodeIcon.getWidth()/2+nodeGrabbed.yOffset;
						nodeGrabbed.oldX = nodeGrabbed.x;
						nodeGrabbed.oldY = nodeGrabbed.y;
						nodeGrabbed = null;
						repaint();
					}
					else {
						nodeGrabbed.x = nodeGrabbed.oldX;
						nodeGrabbed.y = nodeGrabbed.oldY;
						isHoldingNode = false;
						repaint();
						nodeGrabbed = null;
					}
				}
			}
		}
	}
	
	public boolean isOverTrash(int x, int y, TableNode node) {
		if (x > node.x+node.trashX && x < node.x+node.trashX+node.trashCan.getWidth()) {
			if (y > node.y+node.trashY+node.selectYOffset && y < node.y+node.trashY+node.trashCan.getHeight()+node.selectYOffset) {
				return true;
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
		for (int i = 0; i < nodes.size(); i++) {
			g.drawImage(nodes.get(i).nodeIcon, nodes.get(i).x, nodes.get(i).y, null);
		}
		for (int i = 0; i < nodes.size(); i++) {
			if (nodes.get(i).isSelected) {
				g.drawImage(nodes.get(i).dialogBox, nodes.get(i).x+nodes.get(i).dialogBoxX, nodes.get(i).y+nodes.get(i).dialogBoxY, null);
				g.drawImage(nodes.get(i).trashCan, nodes.get(i).x+nodes.get(i).trashX, nodes.get(i).y+nodes.get(i).trashY, null);
				g.drawString("Delete Node", nodes.get(i).x+nodes.get(i).trashX-10, nodes.get(i).y+nodes.get(i).trashY+nodes.get(i).trashCan.getHeight()+nodes.get(i).selectYOffset/2);
				if (!isPressingButton) {
					g.drawImage(nodes.get(i).button, nodes.get(i).x+nodes.get(i).buttonX, nodes.get(i).y+nodes.get(i).buttonY, null);
				}
				else {
					g.drawImage(nodes.get(i).button2, nodes.get(i).x+nodes.get(i).buttonX, nodes.get(i).y+nodes.get(i).buttonY, null);
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
