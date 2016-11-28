import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import java.awt.*;
 
public class ClientController extends JPanel implements ActionListener {
	protected JTextField textField;
    protected JTextArea textArea;
    boolean finished;
    String id;
    boolean idEntered;
    static JFrame frame;
    private final static String newline = "\n";
    ClientView myView;
    JFrame frame2;
    BufferedImage snapButton;
    TableNode myNode;
    NetworkClient myNetwork;
 
    public ClientController(String id, NetworkClient ntwk) throws IOException, InterruptedException {
    	myView = new ClientView(this);
    	this.id = id;
    	myNode = new TableNode(id, this);
    	myNetwork = ntwk;
        try {
			snapButton = ImageIO.read(new File("snapbutton_unsynched.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


    }
    
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawString("#"+id, 0, 16);
		g.drawImage(snapButton, myView.width/4, myView.height/8, null);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
