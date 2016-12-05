import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
/*
* @author Garrett Thompson
* Created: November 16th
* Last Edited: November 24th
* ClientView.java
* View object for client interface. Creates a viewing window (JPanel) to draw client-side GUI on.
* Expected Revisions: Support for adjustible resolution
*/ 
public class ClientView extends JPanel implements ImageObserver {
	int height;
	int width;
	boolean running;
	JFrame frame = new JFrame();
	private JLayeredPane lpane = new JLayeredPane();
	Color alpha;
	ClientController myController;
	/* @author Garrett Thompson
	* Constructor method. Initializes the size of the viewing frame and creates the frame.
	*/
	public ClientView() throws IOException, InterruptedException {
		alpha = new Color(1f, 0f, 0f, 0f);
		height = 720;
		width = 960; //4:3 aspect ratio, 
		running = true;
		frame.setPreferredSize(new Dimension(width, height));
		frame.getContentPane().setLayout(new BorderLayout());
		frame.getContentPane().add(lpane, BorderLayout.CENTER);
		//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
	/* @author Garrett Thompson, even though I didn't really do anything.
	* This method makes this class paint itself.
	*/
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
	}
}
