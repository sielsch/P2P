package client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.TreeSet;

import comClientServer.P2PFile;

public class P2PClient {
	private static Socket socketComm = null;
	private static ServerSocket socketConn = null;
	private static int portServer = 0;
	private static String hostServer = "";
	// static String P2PFile_Folder = "";
	private static File P2P_directory;
	private static String localHost;
	private static ThreadClient threadClient = null;

	private static ObjectOutputStream oos = null;
	private static ObjectInputStream ois = null;
	private static BufferedOutputStream bos = null;
	private static BufferedInputStream bis = null;
	private static TreeSet<P2PFile> listFiles =null;
	
	
	public static void main(String[] args) {
		verifArgument(args);
		checkLocalHost();

		System.out.println(localHost);
		try {
			socketConn = new ServerSocket(0);
			threadClient = new ThreadClient(socketConn);
			socketComm = new Socket(hostServer, portServer);

			initFlux(socketComm);
			oos.writeObject(new String(localHost));
			oos.flush();
			listFiles=directoryToListFile();				
			oos.writeObject(listFiles);
			oos.flush();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void verifArgument(String argument[]) {

		if (argument.length != 3) {
			System.out.println(
					"L'adresse IP et le numéro de port du server doivent être renseigné, ainsi que le répertoire ou se trouvent"
							+ "les fichiers à uploader");
			System.exit(1);
		}

		try {
			portServer = Integer.parseInt(argument[0]);
		} catch (NumberFormatException e) {
			System.out.println("Num�ro de port non valide !");
			System.exit(1);
		}
		if (portServer < 1024 || portServer > 65535) {
			System.out.println("Num�ro de port non autoris� ou non valide !");
			System.exit(1);
		}

		hostServer = argument[2];

		P2P_directory = new File(argument[2]);
		if (!P2P_directory.isDirectory()) {
			System.out.println("Le répertoire n'existe pas");
			System.exit(1);
		}
	}

	public static void checkLocalHost() {
		try {
			localHost = InetAddress.getLocalHost().toString().split("/")[1];
		} catch (UnknownHostException e) {
			System.out.println("adresse ip inconnu, vous devez être connecté à un réseau");
			System.exit(1);
		}
	}

	public static TreeSet<P2PFile> directoryToListFile() {
		TreeSet t = new TreeSet<P2PFile>();
		File[] listOfFiles = P2P_directory.listFiles();

		if (listOfFiles.length == 0) {
			System.out.println("EMPTY DIRECTORY");
		}
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				P2PFile p = new P2PFile(listOfFiles[i].length(), listOfFiles[i].getName());
				t.add(p);
			}
		}

		return t;
	}

	public static void initFlux(Socket socketComm) throws IOException {
		oos = new ObjectOutputStream(socketComm.getOutputStream());
		ois = new ObjectInputStream(socketComm.getInputStream());
		bos = new BufferedOutputStream(oos);
		bis = new BufferedInputStream(ois);
	}

}
