package client;

import java.io.*;
import java.net.*;
import java.util.TreeSet;

import comClientServer.AdressServerTCP;
import comClientServer.P2PFile;
import comClientServer.P2PParametre;
import exception.QuitException;
import java.util.HashSet;

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
	private static HashSet<P2PFile> listFiles = null;

	private static InputStreamReader isr;
	private static BufferedReader clavier;

	private static String requete = "";

	public static void main(String[] args) {
		verifArgument(args);
		checkLocalHost();

		System.out.println(localHost);
		try {
			socketConn = new ServerSocket(0);
			threadClient = new ThreadClient(socketConn);
			socketComm = new Socket(hostServer, portServer);

			initFlux(socketComm);

			oos.writeObject(new AdressServerTCP(socketConn.getLocalPort(), localHost));
			oos.flush();
			listFiles = directoryToListFile();
			oos.writeObject(listFiles);
			oos.flush();

			try {

				while (true) {
					System.out.println("rentrer une action : search <pattern>, get <num>, list, local list, quit");
					requete = clavier.readLine();
					analyseRequete(requete);
				}

			} catch (QuitException e) {
				// TODO: handle exception

			}
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

		hostServer = argument[1];

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

	public static HashSet<P2PFile> directoryToListFile() {
		HashSet<P2PFile> h = new HashSet<>();
		File[] listOfFiles = P2P_directory.listFiles();

		if (listOfFiles.length == 0) {
			System.out.println("EMPTY DIRECTORY");
		}
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				P2PFile p = new P2PFile(listOfFiles[i].length(), listOfFiles[i].getName());
				h.add(p);
			}
		}

		return h;
	}

	public static void initFlux(Socket socketComm) throws IOException {
		oos = new ObjectOutputStream(new BufferedOutputStream(socketComm.getOutputStream()));
		oos.flush();
		ois = new ObjectInputStream(new BufferedInputStream(socketComm.getInputStream()));
		isr = new InputStreamReader(System.in);
		clavier = new BufferedReader(isr);
	}

	public static void analyseRequete(String requete) throws IOException, QuitException {
		try {
			String[] stringTable = requete.split(" ");

			switch (stringTable[0]) {
			case "search":
				if (stringTable.length == 2) {
					oos.writeObject(requete);
					oos.flush();
					System.out.println((String) ois.readObject());
				} else {
					System.out.println("requete invalide, rentrer une nouvelle requete");
				}
				break;
			case "get":
				if (stringTable.length == 2) {
					// throw bad request exception
					oos.writeObject(requete);
					oos.flush();
				} else {
					System.out.println("requete invalide, rentrer une nouvelle requete");
				}

				break;
			case "list":
				if (stringTable.length == 1) {
					oos.writeObject(requete);
					oos.flush();
					System.out.println((String)ois.readObject());
				} else {
					System.out.println("requete invalide, rentrer une nouvelle requete");
				}
				break;
			case "local":
				if (stringTable.length == 2) {
					if(stringTable[1].equals("list")){
						directoryToListFile();
						for (P2PFile f : listFiles) {
							System.out.println(f);
						}
						
					}				
				} else {
					System.out.println("requete invalide, rentrer une nouvelle requete");
				}
				break;
			case "quit":
				throw new QuitException();
			default:
				System.out.println("requete invalide, rentrer une nouvelle requete");
				// throw new BadRequestException();

			}
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		}

	}

	public static void telechargement(TreeSet<AdressServerTCP> listClient, P2PFile file) {

		long nbPaquet = file.getTaille() / P2PParametre.TAILLE_PAQUET;

		if (file.getTaille() % P2PParametre.TAILLE_PAQUET != 0) {
			nbPaquet++;
		}

		long nbPaquetClient = nbPaquet / listClient.size();

		long premierMorceau = 0;
		long dernierMorceau = nbPaquetClient;

		for (AdressServerTCP adressServerTCP : listClient) {
			try {
				ObjectOutputStream oos_client = null;
				DatagramSocket sockUdpReceive = new DatagramSocket();
				
				
				Socket socketComm = new Socket(adressServerTCP.getHost(), adressServerTCP.getPort());
				oos_client = new ObjectOutputStream(new BufferedOutputStream(socketComm.getOutputStream()));
				oos_client.flush();
				oos_client.writeUTF(localHost);
				oos_client.writeInt(sockUdpReceive.getLocalPort());
				oos_client.writeObject(file);
				oos_client.writeLong(premierMorceau);
				oos_client.writeLong(dernierMorceau);
				oos_client.flush();
				premierMorceau = premierMorceau + nbPaquetClient;
				dernierMorceau = dernierMorceau + nbPaquetClient;

			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
