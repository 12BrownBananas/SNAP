import java.awt.List;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTextArea;

/**
 *as in now there is communication  between client using the server as a router
 */

public class NetworkServer {
	 private static ArrayList<Integer>  TempNodes;
		
	private JFrame frame = new JFrame("SNAPServer");
   	static JTextArea area = new JTextArea(10, 30);
	private JMenuBar MenuBar = new JMenuBar();
	JMenu Menu = new JMenu ("Menu");
	JMenuItem exit = new JMenuItem("Exit");
	//I want a local variable that I can use to store nodes inside of, e.g. TableNode tempNode = new TableNode(...)
   
    private static final int PORT = 9003;

    /**
     * this has keeps track of the table ID no and make sure that 
     * there are not other tables with the same ID No
     */
   // private static ArrayList<Integer> TempId= new ArrayList<Integer>();
    private ArrayList<TableNode> IdNode = new ArrayList<TableNode>();
    private static HashSet<String> IdNodeLookup = new HashSet<String>();

    /**
     * The set of all the print writers for all the clients.  This
     * set is kept so we can easily broadcast messages.
     */
    private static HashSet<PrintWriter> writers = new HashSet<PrintWriter>();

    /**
     * CREATES the window frame and menu for the server
     */
    public NetworkServer() throws IOException {
        ServerSocket listener = new ServerSocket(PORT);
        try {
            while (true) {
                new Handler(listener.accept()).start();
            }
        } finally {
            listener.close();
        }
    }
    public String findNode(String id) {
    	for (int i = 0; i < IdNode.size(); i++) {
    		if (IdNode.get(i).nodeID == id) {
    			return IdNode.get(i).nodeID;
    		}
    	}
    	return null;
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
		private ObjectInputStream objectIn;
		private ObjectOutputStream outPut;
		private ObjectInputStream inPut;
		private ArrayList<Integer> TempId;

        public Handler(Socket socket) {
            this.socket = socket;
        }
       
        @SuppressWarnings("unchecked")
		public void run() {
            try {
           
            	ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            	TempId =  new ArrayList<Integer>();;
				try {
					TempId = (ArrayList<Integer>) ois.readObject();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                System.out.println("Message Received from client: " + TempId.toString());
            
                printArray(TempId);   
                acceptData(ois);
                //ois.close();
                //socket.close();
                //System.out.println("Waiting for client message is...");
                in = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
                


                // Request a name from this client.  Keep requesting until
                // a name is submitted that is not already used.  Note that
                // checking for the existence of a name and adding the name
                // must be done while locking the set of names.
                while (true) {
                	
                    out.println("NAME");
                    name = in.readLine();
                    if (name == null) {
                        return;
                    }
                    synchronized (IdNodeLookup) {
                        if (!IdNodeLookup.contains(name)) {
                            IdNodeLookup.add(name);
                        	//I need to be able to add a Table Node object
                        	//IdNode.add();
                            
                            break;
                        }
                    }
                }
               
                area.append("The Table IdNode " + name + " is Connected" +"\n");

                // Now that a successful name has been chosen, add the
                // socket's print writer to the set of all writers so
                // this client can receive broadcast messages.
                out.println("ACCEPTED");
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
                    	IdNodeLookup.remove(name);
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

		private void printArray(ArrayList<Integer> tempId2) {
			// TODO Auto-generated method stub
			for(Integer s:tempId2){
		          System.out.println("anything"+ s);
		        }
		}
		
		private void acceptData(ObjectInputStream inPut) {
			// TODO Auto-generated method stub
			System.out.println("acceptData called by " + Thread.currentThread().getName());
		    Integer s = 0;
			try {
				s = (Integer) inPut.readObject();
			} catch (ClassNotFoundException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    TempId.add(s);
		    System.out.println("Data coming in: " + TempId.toString());
		}
		
		
    }
   
	
