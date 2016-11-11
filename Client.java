package multiplayer;

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
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Client {

    BufferedReader in;
    PrintWriter out;
    JFrame frame = new JFrame("SNAPClient");
    JTextField textField = new JTextField(40);
    JTextArea messageArea = new JTextArea(8, 40);
	Button ServiceButton= new Button("Touch Me for Service");
	
    public Client() throws IOException {
    	//frame.setTitle("Server Notification and Assistance Program");
    	frame.setSize(600, 600);
    	frame.setLocationRelativeTo(frame);
    	frame.setLayout(new FlowLayout());
    	frame.add(textField);
        frame.add(ServiceButton);
        textField.setEditable(false);
        messageArea.setEditable(false);
       frame.setBounds(100, 100, 529, 300);
       
        Container c =frame.getContentPane();
        c.add(textField, "center");
        c.add(new JScrollPane(messageArea), "Center");
        c.add(ServiceButton);
        c.setBounds(40,80,80,85);
        
       // frame.pack();
       /**
        * opens a new window with options such as drinks
        * food and appetizer or to enter a quick message 
        */
        ServiceButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				Frame frame = new JFrame ("MyPanel");
	            ((JFrame) frame).setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
	            ((JFrame) frame).getContentPane().add (new services());
	            frame.pack();
	            frame.setVisible (true);
			}
        	
        });
        /**
             * this textBox is just for the purpose of getting data moving
             * this will be deleted in the process
               */
        textField.addActionListener(new ActionListener() {
            
            public void actionPerformed(ActionEvent e) {
                out.println(textField.getText());
                textField.setText("");
            }
        });
    }

    /**
     * Prompt for and return the address of the server.
     */
    private String getServerAddress() {
        return JOptionPane.showInputDialog(
            frame,
            "Enter IP Address of the Server:",
            "Welcome to the Chatter",
            JOptionPane.QUESTION_MESSAGE);
    }

    /**
     * Prompt for and return the desired screen name.
     */
    private String getName() {
        return JOptionPane.showInputDialog(
            frame,
            "Choose a screen name:",
            "Screen name selection",
            JOptionPane.PLAIN_MESSAGE);
    }

    /**
     * Connects to the server then enters the processing loop.
     */
    private void run() throws IOException {

        // Make connection and initialize streams
        String serverAddress = getServerAddress();
        @SuppressWarnings("resource")
		Socket socket = new Socket(serverAddress, 9003);
        in = new BufferedReader(new InputStreamReader(
            socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

        // Process all messages from server
        while (true) {
            String line = in.readLine();
            if (line.startsWith("SUBMITNAME")) {
                out.println(getName());
            } else if (line.startsWith("NAMEACCEPTED")) {
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
        Client client = new Client();
        client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        client.frame.setVisible(true);
        client.run();
    }
    class services extends JPanel{
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private JButton Drink;
        private JButton Appetizer;
        private JButton Food;
        private JTextField messageBox;
        public services() {
            //construct components
            Drink = new JButton ("drinks");
            Appetizer = new JButton ("Appetizers");
            Food = new JButton ("Food");
            messageBox = new JTextField (5);

            //adjust size and set layout
            setPreferredSize (new Dimension (395, 156));
            setLayout (null);

            //set component bounds (only needed by Absolute Positioning)
            Drink.setBounds (20, 45, 100, 25);
            Appetizer.setBounds (135, 60, 100, 25);
            Food.setBounds (260, 35, 100, 25);
            messageBox.setBounds (105, 115, 100, 25);

            //add components
            add (Drink);
            add (Appetizer);
            add (Food);
            add (messageBox);     
            Drink.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					JRadioButton pepsi = new JRadioButton("Pepsi");
					JRadioButton margarita = new JRadioButton("Margarita");
					JRadioButton sweetTea = new JRadioButton("Sweet Tea");
					JRadioButton Rum = new JRadioButton("Rum and Coke");
					Frame drinksFrame= new JFrame("Drinks Menu");
					drinksFrame.setLayout(new FlowLayout());
					drinksFrame.setSize(400, 400);
					
					drinksFrame.add(pepsi);
					drinksFrame.add(Rum);
					drinksFrame.add(sweetTea);
					drinksFrame.add(margarita);
					
					drinksFrame.setVisible(true);
					if (pepsi.isSelected()){
					
						ChatServer.area.append("A " + pepsi.getText() + "are required at Table");
						
					}
				
					System.out.println("A " + pepsi.getText() + " drink is required at Table");				
				}
            	
            });
        }
    }
}