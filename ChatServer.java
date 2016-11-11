package multiplayer;

import java.awt.Button;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 *as in now there is communication  between client using the server as a router
 */

public class ChatServer {
		private BufferedReader in;
		private PrintWriter out;
		private JFrame frame = new JFrame("SNAPServer");
	    static JTextArea area = new JTextArea(8, 40);
		private JMenuBar MenuBar = new JMenuBar();
		JMenu Menu = new JMenu ("Menu");
		private Button ServiceButton= new Button("Touch Me for Service");
		JMenuItem exit = new JMenuItem("Exit");

   
    private static final int PORT = 9003;

    /**
     * this has keeps track of the table ID no and make sure that 
     * there are not other tables with the same ID No
     */
    private static HashSet<String> IdNode = new HashSet<String>();

    /**
     * The set of all the print writers for all the clients.  This
     * set is kept so we can easily broadcast messages.
     */
    private static HashSet<PrintWriter> writers = new HashSet<PrintWriter>();

    /**
     * CREATES the window frame and menu for the server
     */
    public ChatServer() throws IOException {
    	
    	frame.setSize(600, 600);
    	frame.setLocationRelativeTo(frame);
    	frame.setLayout(new FlowLayout()); 
        frame.getContentPane().add(new JScrollPane(area), "Center");
        frame.setJMenuBar(MenuBar);
        frame.add(Menu);
        Menu.add(exit);
    	MenuBar.add(Menu);
    	exit.setMnemonic(KeyEvent.VK_E);
    	exit.setToolTipText("Exit Application");
    	exit.addActionListener((ActionEvent event)->{
    		System.exit(0);
    	});
    	ServiceButton.setLocation(30, 45);;
        area.setEditable(false);
        ServiceButton.setSize(45, 20);
            // Add Listeners
        }
    /**
     * main application, check for connection
     * @param args arguments to pass over
     * @throws Exception
     */

    public static void main(String[] args) throws Exception {
        System.out.println("The server is running.");
        ServerSocket listener = new ServerSocket(PORT);
        ChatServer  server = new ChatServer();
      
        server.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        server.frame.setVisible(true);
        //server.run();
        try {
            while (true) {
                new Handler(listener.accept()).start();
            }
        } finally {
            listener.close();
        }
    }
    

    /**
     * handler thread class. responsible for a dealing with a single client
     * and broadcasting its messages.
     */
    public static class Handler extends Thread {
        private String name;
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;

        public Handler(Socket socket) {
            this.socket = socket;
        }
       
        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                // Request a name from this client.  Keep requesting until
                // a name is submitted that is not already used.  Note that
                // checking for the existence of a name and adding the name
                // must be done while locking the set of names.
                while (true) {
                    out.println("SUBMITNAME");
                    name = in.readLine();
                    if (name == null) {
                        return;
                    }
                    synchronized (IdNode) {
                        if (!IdNode.contains(name)) {
                            IdNode.add(name);
                            break;
                        }
                    }
                    System.out.println(IdNode);
                }
                area.append("The Table IdNode " + name + " is Connected" +"\n");

                // Now that a successful name has been chosen, add the
                // socket's print writer to the set of all writers so
                // this client can receive broadcast messages.
                out.println("NAMEACCEPTED");
                writers.add(out);

                // Accept messages from this client and broadcast them.
                // Ignore other clients that cannot be broadcasted to.
                while (true) {
                    String input = in.readLine();
                    if (input == null) {
                        return;
                    }
                    for (PrintWriter writer : writers) {
                        writer.println("MESSAGE " + name + ": " + input);
                    }
                }
            } catch (IOException e) {
                System.out.println(e);
            } finally {
                // Remove its name and closes the socket.
                if (name != null) {
                    	IdNode.remove(name);
                }
                if (out != null) {
                    writers.remove(out);
                }
                try {
                    socket.close();
                } catch (IOException e) {
                }
            }
        }
    }
}
