import java.io.IOException;
import java.net.ServerSocket;

import javax.swing.JFrame;

/*
* @author Garrett Thompson
* Created: November 26th
* Last Edited: November 27th
* SnapMain.java
* Runs the program. Would initialize only the server-side initially but due to our current 
* lack of a network component, is currently used to create both the client and server.
* Expected Revisions: Negligible
*/ 
public class SnapMain {
	private static final int PORT = 9003;
	/* @author Garrett Thompson
	*  Main method. Begins running the program.
	*/
	public static void main(String[] args) throws IOException, InterruptedException {
		
		LayoutController controller = new LayoutController();
		
		//NetworkServer server = new NetworkServer();
		//controller.myNetwork = server;
	}
}
