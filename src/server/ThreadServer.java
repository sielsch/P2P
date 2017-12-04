package server;

import java.net.Socket;

public class ThreadServer extends Thread {

	Socket sockComm;

	public ThreadServer(Socket sockComm) {
		this.sockComm = sockComm;
	}

	
	public void run(){
		
	}
	
}
