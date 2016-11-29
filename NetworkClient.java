//"When prompted  for the IP adress enter "localhost". this is for testing only
// and enter a table name or ID
import java.awt.Button;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
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
	ArrayList <Integer> TempNodes = new ArrayList<Integer>();
	private ObjectOutputStream outPut;
	private ObjectInputStream inPut;
	
    public NetworkClient() throws IOException {
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
    private void run() throws IOException, InterruptedException {

        // Make connection and initialize streams
        String serverAddress = "localhost";
        @SuppressWarnings("resource")
		Socket socket = new Socket(serverAddress, 9003);
        in = new BufferedReader(new InputStreamReader(
            socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        // Process all messages from server
        
        outPut = new ObjectOutputStream(socket.getOutputStream());
        inPut = new ObjectInputStream(socket.getInputStream());
        
        
        
        
        while (true) {
        	ObjectOutputStream outPut = new ObjectOutputStream(socket.getOutputStream());
            String line = in.readLine();
            if (line.startsWith("NAME")) {
                String id = getName();
                frame.dispose();
                ClientView view = new ClientView();
                ClientController ctrl = new ClientController(id, this, view);
                myController = ctrl;
                while(myController.synched=true){
                TempNodes.add(myController.myNode.tableStatus);
                outPut.writeObject(TempNodes);
                //System.out.print(myController.myNode.tableStatus);
                }
              
                //I want to be able to send myController.myNode to the server at any time
                ctrl.repaint();
            } else if (line.startsWith("ACCEPTED")) {
                textField.setEditable(true);
            } else if (line.startsWith("MESSAGE")) {
                messageArea.append(line.substring(8) + "\n");
            }
            
        }
        
        
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
