//"When prompted  for the IP adress enter "localhost". this is for testing only
// and enter a table name or ID
import java.awt.Button;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class NetworkClient {

    BufferedReader in;
    PrintWriter out;
    JFrame frame = new JFrame("SNAPClient");
    JTextField textField = new JTextField(40);
    JTextArea messageArea = new JTextArea(8, 40);
	Button ServiceButton= new Button("Touch Me for Service");
	ClientController myController;
	
	ArrayList<TableNode> allNodes;
	
	ObjectInputStream objectIn;
	ObjectOutputStream objectOut;
	
    public NetworkClient() throws IOException {
    }

    /**
     * Prompt for and return the address of the server.
     */
    private String getServerAddress() {
        return JOptionPane.showInputDialog(
            frame,
            "Enter IP Address of the Server:",
            "",
            JOptionPane.QUESTION_MESSAGE);
    }

    /**
     * Prompt for and return the desired screen name.
     */
    private String getName() {
        return JOptionPane.showInputDialog(
            frame,
            "Table ID Number:",
            "ID Entry",
            JOptionPane.PLAIN_MESSAGE);
    }

    /**
     * Connects to the server then enters the processing loop.
     * @throws InterruptedException 
     */
    public void run() throws IOException, InterruptedException {

        // Make connection and initialize streams
        String serverAddress = getServerAddress();
        //@SuppressWarnings("resource")
		//Socket socket = new Socket(serverAddress, 9003);
        //in = new BufferedReader(new InputStreamReader();
        //out = new PrintWriter(socket.getOutputStream(), true);
        // Process all messages from server

        String id = getName();
        frame.dispose();
        ClientView view = new ClientView();
        ClientController ctrl = new ClientController(id, view);
        myController = ctrl;
        //I want to be able to send myController.myNode to the server at any time
        ctrl.repaint();
        
        
        
    }

    /**
     * main which Runs the client 
     */
    public static void main(String[] args) throws Exception {
        NetworkClient client = new NetworkClient();
        client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        client.frame.setVisible(false);
        client.run();
    }
}