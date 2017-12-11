package server;

import comClientServer.P2PFile;
import java.io.*;
import java.net.Socket;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ThreadServer extends Thread {

    Socket sockComm = null;
    ListFilesServer lfs = null;

    public ThreadServer(Socket sockComm, ListFilesServer lfs) {
        this.sockComm = sockComm;
        this.lfs = lfs;
    }

    public void run() {
        ObjectInputStream ois = null;
        ObjectOutputStream oos = null;

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
                Logger.getLogger(ThreadServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }


}
