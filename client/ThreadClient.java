package client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import comClientServer.P2PFile;

public class ThreadClient extends Thread {

	private ServerSocket sockConn=null;
	private Socket sockComm=null;
	private ObjectInputStream ois=null;
	private ObjectOutputStream oos=null;
	
	
	
	
	public ThreadClient(ServerSocket sockConn) {
		this.sockConn = sockConn;
	}

	public void run() {
		
		try {
			
			sockComm=sockConn.accept();
			ois = new ObjectInputStream(new BufferedInputStream(sockComm.getInputStream() ));
			oos = new ObjectOutputStream(new BufferedOutputStream(sockComm.getOutputStream()));
			oos.flush();
			String HostReceiver=ois.readUTF();
			int portReceiver=ois.readInt();
			try {
				P2PFile fileToSend=(P2PFile) ois.readObject();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			long premierMorceau = ois.readLong();
			long dernierMorceau = ois.readLong();
			
			System.out.println(HostReceiver);
			System.out.println(portReceiver);
			System.out.println(premierMorceau);
			System.out.println(dernierMorceau);
			
//			ThreadSender senderUDP = new ThreadSender();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
