import java.awt.*;
import java.awt.image.*;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.*;

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
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
	}
}