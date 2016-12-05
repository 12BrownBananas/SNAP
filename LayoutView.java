import java.awt.*;
import java.awt.image.*;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.*;

/*
* @author Garrett Thompson, Jordan Abbatiello
* Created: November 17th
* Last Edited: November 28th
* LayoutView.java
* View for the server-side interface. Paints GUI to a window (JFrame) and sets component focus.
* Expected revisions: Support for multiple resolutions.
*/
public class LayoutView extends JPanel implements ImageObserver {
	static int height;
	static int width;
	boolean running;
	ArrayList<TableNode> tables = new ArrayList<TableNode>();
	JFrame frame = new JFrame();
	private JLayeredPane lpane = new JLayeredPane();
	Color alpha;
	LayoutController myController;
	orderController orderController;
	/* @author Garrett Thompson
	* Constructor for the layout view. Generates a new window to paint the GUI of the provided controller object.
	* @param controller  The controller in question.
	*/
	public LayoutView(LayoutController controller) throws IOException, InterruptedException {
		alpha = new Color(1f, 0f, 0f, 0f);
		height = 810;
		width = 1440;
		running = true;
		myController = controller;
		frame.setPreferredSize(new Dimension(width, height));
		frame.getContentPane().setLayout(new BorderLayout());
		frame.getContentPane().add(lpane, BorderLayout.CENTER);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(myController);
		frame.pack();
		frame.setVisible(true);
	}
	/* @author Jordan Abbatiello
	* A second constructor that is used for creating windows that display the order submenu rather than the layout.
	* @param controller  an OrderController object to draw the GUI of.
	*/
	public LayoutView(orderController controller){
		orderController = controller;
		frame.addMouseListener(orderController);
		frame.setPreferredSize(new Dimension(width, height));
		frame.getContentPane().setLayout(new BorderLayout());
		frame.getContentPane().add(lpane, BorderLayout.CENTER);
		frame.setContentPane(orderController);
		frame.pack();
		frame.setVisible(true);
	}
	/* @author Garrett Thompson
	* Makes this class draw itself.
	*/
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
	}
}
