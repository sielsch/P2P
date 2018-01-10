package client;

import java.net.*;

import comClientServer.P2PFile;
import comClientServer.P2PParametre;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class ThreadReceiver extends Thread {

    private P2PFile fileToSend = null;
    private int port;
    private DatagramPacket packetSended;

    private int buffSize;

    private long tailleDernierMorceau;

    private int NbPaquet = 100;

    private byte[][] tabData = new byte[NbPaquet][];

    public ThreadReceiver(P2PFile fileToSend, int port,
            long tailleDernier) {
        this.fileToSend = fileToSend;
        this.port = port;
        this.tailleDernierMorceau = tailleDernier;

    }

    public void run() {

        int portServ = 0, portClient;
        InetAddress adIpClient;

        DatagramSocket sockComm = null;
        DatagramPacket pkRequete;

        byte[] bufRequete;
        final int TAILLE = P2PParametre.TAILLE_PAQUET;
        bufRequete = new byte[TAILLE];

        try {

            sockComm = new DatagramSocket(portServ);

            while (true) {
                pkRequete = new DatagramPacket(bufRequete, bufRequete.length);
                sockComm.receive(pkRequete);
                ByteBuffer wrapped = ByteBuffer.wrap(Arrays.copyOfRange(bufRequete, 0, Long.BYTES - 1));
                long numPacket = wrapped.getLong();

                byte[] DataOfPacket = Arrays.copyOfRange(bufRequete, Long.BYTES, bufRequete.length - 1);

                for (int i = 0; i < DataOfPacket.length; ++i) {
                    tabData[(int) numPacket][i] = DataOfPacket[i];
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            if (sockComm != null) {
                sockComm.close();
            }

        }
    }

}
