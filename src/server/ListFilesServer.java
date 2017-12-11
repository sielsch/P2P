/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package P2P.src.server;

import comClientServer.P2PFile;
import java.util.*;

/**
 *
 * @author Enzo
 */
public class ListFilesServer {

    private TreeMap<P2PFile, TreeSet<String>> annuaire;

    public synchronized boolean containsKey(P2PFile key) {
        return annuaire.containsKey(key);
    }

    public synchronized TreeSet<String> getByKey(P2PFile key) {
        if (annuaire.containsKey(key)) {
            return annuaire.get(key);
        } else {
            return null;
        }
    }

    public synchronized void put(P2PFile key, String s) {
        TreeSet<String> ts;
        if (!annuaire.containsKey(key)) {
            ts = new TreeSet<String>();
            ts.add(s);
        } else {
            ts = annuaire.get(key);
            ts.add(s);
        }
        annuaire.put(key, ts);

    }

}
