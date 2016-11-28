import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.IOException;

import javax.swing.*;

public class orderViewer {
	
	public JFrame frame;
	public static int width = 1440;
	public static int height = 810; 
	private static JLayeredPane lpane = new JLayeredPane();
	
	public static void main(String[] args) throws NumberFormatException, IOException{
		
		JFrame frame = new JFrame();
		frame.setPreferredSize(new Dimension(width, height));
		frame.getContentPane().setLayout(new BorderLayout());
		frame.getContentPane().add(lpane, BorderLayout.CENTER);
		orderController control = new orderController();
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.addMouseListener(control);
		frame.add(control);
		frame.pack();
		frame.setVisible(true);
		
	}

}
