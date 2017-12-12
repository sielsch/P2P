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
    ListFilesServer lfs = null;
    ObjectInputStream ois = null;
    ObjectOutputStream oos = null;

    public ThreadServer(Socket sockComm, ListFilesServer lfs) {
        this.sockComm = sockComm;
        this.lfs = lfs;
    }

    public void run() {
        String request = "";

        try {
            initFlux();

            AdressServerTCP adressTCP = (AdressServerTCP) ois.readObject();
            TreeSet<P2PFile> ts = (TreeSet<P2PFile>) ois.readObject();

            for (P2PFile p : ts) {
                lfs.put(p, adressTCP);
            }

            request = (String) ois.readObject();
            processRequest(request);

        } catch (EOFException e) {
            System.out.println("EOF fin de l'émission du client");
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
            ArrayList<P2PFile> searchTab = new ArrayList<P2PFile>();

            switch (tabRequest[0]) {
                case "search":
                    if (tabRequest.length == 2) {
                        String res = "";
                        int i = 0;
                        searchTab = lfs.getKeysByName(tabRequest[1]);
                        for (P2PFile p : searchTab) {
                            res += i + ". " + p + "\n";
                        }
                        oos.writeObject(res);
                    } else {
                        oos.writeObject("nombre de paramètre non comforme");
                    }
                    break;

                case "list":
                    if (tabRequest.length == 1) {
                        String re = "";
                        if (!searchTab.isEmpty()) {
                            int j = 0;
                            for (P2PFile p : searchTab) {
                                re += j + ". " + p + "\n";
                            }
                        } else {
                            re = "Liste de résultats de recherche courante inexistante";
                        }
                        oos.writeObject(re);
                    } else {
                        oos.writeObject("nombre de paramètre non comforme");
                    }
                    break;

                case "get":
                    if (tabRequest.length == 2) {
                        if (!searchTab.isEmpty()) {
                            int numFichier = Integer.parseInt(tabRequest[1]);
                            if (numFichier >= 0 && numFichier < searchTab.size()) {
                                P2PFile fileToDownload = searchTab.get(numFichier);
                                TreeSet<String> ts = lfs.getByKey(fileToDownload);

                                oos.writeObject(fileToDownload);

                                oos.writeObject(ts);
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
                    System.out.println("requete invalide, rentrer une nouvelle requete");
//			throw new BadRequestException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
            System.out.println("Erreur entrée non comforme");
        }
    }

}
