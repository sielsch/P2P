package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class P2PServer {

    public static void main(String[] args) {
        ServerSocket conn = null;
        Socket comm = null;
        ThreadServer t;
        ListFilesServer lfs = new ListFilesServer();

        if (args.length != 1) {
            System.out.println("Nombre d'arguments incorrect !");
            System.exit(1);
        }

        try {
            conn = new ServerSocket(Integer.parseInt(args[0]));
        } catch (IOException e) {
            System.out.println("pb creation serveur : " + e.toString());
            System.exit(1);
        } catch (NumberFormatException e) {
            System.out.println("Numéro de port non valide !");
            System.exit(1);
        }

        try {
            while (true) {
                comm = conn.accept();
                t = new ThreadServer(comm, lfs);
                t.start();
            }
        } catch (IOException e) {
            System.out.println("pb connexion client : " + e.toString());
        } finally {
            try {
                comm.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

}
