package server;

import comClientServer.P2PFile;
import java.io.*;
import java.net.Socket;
import java.util.*;

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
            ois = new ObjectInputStream(new BufferedInputStream(sockComm.getInputStream()));

            oos = new ObjectOutputStream(new BufferedOutputStream(sockComm.getOutputStream()));
            oos.flush();
            System.out.println("flux créé");

            String IPClient = (String) ois.readObject();
            TreeSet<P2PFile> ts = (TreeSet<P2PFile>) ois.readObject();

            for (P2PFile p : ts) {
                lfs.put(p, IPClient);
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

    public void processRequest(String request) {
        try {
            String[] tabRequest = request.split(" ");
            ArrayList<P2PFile> searchTab = new ArrayList<P2PFile>();

            switch (tabRequest[0]) {
                case "search":
                    String res = "";
                    int i = 0;
                    searchTab = lfs.getKeysByName(tabRequest[1]);
                    for (P2PFile p : searchTab) {
                        res += i + ". " + p + "\n";
                    }
                    oos.writeObject(res);
                    break;

                case "list":
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
                    break;

                case "get":
                    if (!searchTab.isEmpty()) {
                        int numFichier = Integer.parseInt(tabRequest[1]);
                        if (numFichier >= 0 && numFichier < searchTab.size()) {
                            P2PFile key = searchTab.get(numFichier);
                            TreeSet<String> ts = lfs.getByKey(key);
                            oos.writeObject(ts);
                        }else{
                            oos.writeObject("Numéro de fichier non valide");
                        }

                    } else {
                        oos.writeObject("Liste de résultats de recherche courante inexistante");
                    }
                    break;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
            System.out.println("Erreur entrée non comforme");
        }
    }

}
