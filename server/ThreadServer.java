package server;

import comClientServer.AdressServerTCP;
import comClientServer.P2PFile;
import java.io.*;
import java.net.Socket;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ThreadServer extends Thread {

	Socket sockComm = null;
	ListFilesServer listFileServer = null;
	ObjectInputStream ois = null;
	ObjectOutputStream oos = null;
	HashMap<Integer, P2PFile> searchTab = null;
	AdressServerTCP adressTCPClient =null ;
	HashSet<P2PFile> hashSetFileClient;

	public ThreadServer(Socket sockComm, ListFilesServer listFileServer) {
		this.sockComm = sockComm;
		this.listFileServer = listFileServer;
		searchTab = new HashMap<Integer, P2PFile>();

	}

	public void run() {
		String request = "";

		try {
			initFlux();

			 adressTCPClient = (AdressServerTCP) ois.readObject();
			 hashSetFileClient = ((HashSet<P2PFile>) ois.readObject());

			for (P2PFile file : hashSetFileClient) {
				listFileServer.put(file, adressTCPClient);
			}

			while (true) {
				request = (String) ois.readObject();
				processRequest(request);
			}

		} catch (EOFException e) {
			System.out.println("EOF fin de l'émission du client");
			listFileServer.removeByClient(hashSetFileClient, adressTCPClient);
			
		} catch (IOException e) {
			System.out.println("Pb de communication " + e.toString());
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
			System.out.println("Erreur entrée non comforme");
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
			System.out.println("ClassNotFound");
		} finally {
			try {
				oos.close();
				ois.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	public void initFlux() {
		try {
			ois = new ObjectInputStream(new BufferedInputStream(sockComm.getInputStream()));
			oos = new ObjectOutputStream(new BufferedOutputStream(sockComm.getOutputStream()));
			oos.flush();
			System.out.println("flux créé");
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public void processRequest(String request) {
		try {
			String[] tabRequest = request.split(" ");

			switch (tabRequest[0]) {
			case "search":
				if (tabRequest.length == 2) {
					searchTab.clear();
					int i = 0;
					for (P2PFile pfile : listFileServer.getKeysByName(tabRequest[1])) {
						searchTab.put(i, pfile);
						i++;
					}

					oos.writeObject(getSearchList());
				} else {
					oos.writeObject("nombre de paramètre non comforme");
				}
				break;

			case "list":
				String re = "";
				if (tabRequest.length == 1) {
					if (!searchTab.isEmpty()) {
						re = getSearchList();
					} else {
						re = "Liste de résultats de recherche courante inexistante";
					}
				} else {
					re = "nombre de paramètre non comforme";
				}
				System.out.println(re);
				oos.writeObject(re);
				oos.flush();
				break;

			case "get":
				if (tabRequest.length == 2) {
					if (!searchTab.isEmpty()) {
						int numFichier = Integer.parseInt(tabRequest[1]);
						if (numFichier >= 0 && numFichier < searchTab.size()) {
							P2PFile fileToDownload = searchTab.get(numFichier);
							HashSet<AdressServerTCP> hashSetAdress = listFileServer.getByKey(fileToDownload);
							oos.writeObject(fileToDownload);
							oos.writeObject(hashSetAdress);
							System.out.println("\n ENVOI P2PFILE + LIST OF SEEDERS");
						} else {
							oos.writeObject("Numéro de fichier non valide");
						}

					} else {
						oos.writeObject("Liste de résultats de recherche courante inexistante");
					}
				} else {
					oos.writeObject("nombre de paramètre non comforme");
				}
				break;
			default:
				oos.writeObject("requete invalide, rentrer une nouvelle requete");
				// throw new BadRequestException();
			}
			oos.flush();
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
			System.out.println("Erreur entrée non comforme");
		}
	}

	public String getSearchList() {
		String ret = "";
		for (Map.Entry<Integer, P2PFile> entry : searchTab.entrySet()) {
			ret += entry.getKey() + ". " + entry.getValue() + "\n";

		}
		return ret;
	}

}
