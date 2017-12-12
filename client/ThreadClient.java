package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ThreadClient extends Thread {

	private ServerSocket sockConn;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	
	
	
	
	public ThreadClient(ServerSocket sockConn) {
		this.sockConn = sockConn;
	}

	public void run() {
		
		try {
			
			sockConn.accept();
			
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
