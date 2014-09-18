package us.gusworks.gus.knownWorld.connection;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Connection {
	
	private Socket socket = null;
	private PrintWriter pw = null;
	
	public Connection(){
		
		try {
			socket = new Socket("70.15.90.240", 2324);
			pw = new PrintWriter(socket.getOutputStream());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
