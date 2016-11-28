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

public class ClientView extends JPanel implements ImageObserver {
	int height;
	int width;
	boolean running;
	JFrame frame = new JFrame();
	private JLayeredPane lpane = new JLayeredPane();
	Color alpha;
	ClientController myController;
	public ClientView() throws IOException, InterruptedException {
		alpha = new Color(1f, 0f, 0f, 0f);
		height = 720;
		width = 960;
		running = true;
		frame.setPreferredSize(new Dimension(width, height));
		frame.getContentPane().setLayout(new BorderLayout());
		frame.getContentPane().add(lpane, BorderLayout.CENTER);
		//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
	}
}
