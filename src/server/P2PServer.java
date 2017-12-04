package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class P2PServer {

	public static void main(String[] args) {

		ServerSocket sockConn = null;
		Socket sockComm = null;
		int portServ = 0;

		if (args.length != 1) {
			System.out.println("Le numéro de port doit être renseigné");
		}

		try {
			portServ = Integer.parseInt(args[0]);
		} catch (NumberFormatException e) {
			System.out.println("Num�ro de port non valide !");
			System.exit(1);
		}
		if (portServ < 1024 || portServ > 65535) {
			System.out.println("Num�ro de port non autoris� ou non valide !");
			System.exit(1);
		}

		
		try {
			sockConn = new ServerSocket(portServ);
		} catch (IOException e) {
			e.printStackTrace();
		}

		
		while (true) {
			try {
				sockComm = sockConn.accept();
				
				
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
