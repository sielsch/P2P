/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import comClientServer.AdressServerTCP;
import comClientServer.P2PFile;
import java.util.*;

/**
 *
 * @author Enzo
 */
public class ListFilesServer {

    private TreeMap<P2PFile, TreeSet<AdressServerTCP>> annuaire;

    public synchronized boolean containsKey(P2PFile key) {
        return annuaire.containsKey(key);
    }

    public synchronized TreeSet<AdressServerTCP> getByKey(P2PFile key) {
        if (annuaire.containsKey(key)) {
            return annuaire.get(key);
        } else {
            return null;
        }
    }

    public synchronized void put(P2PFile key, AdressServerTCP addr) {
        TreeSet<AdressServerTCP> ts;
        if (!annuaire.containsKey(key)) {
            ts = new TreeSet<>();
            ts.add(addr);
        } else {
            ts = annuaire.get(key);
            ts.add(addr);
        }
        annuaire.put(key, ts);
    }
    
    public synchronized Set<P2PFile> getAllKeys(){
        return annuaire.keySet();
    }
    
    public synchronized ArrayList<P2PFile> getKeysByName(String name){
        Set<P2PFile> all;
        ArrayList<P2PFile> selection = new ArrayList<P2PFile>();
        all = annuaire.keySet();
        for(P2PFile p : all){
            if(p.getName().contains(name))
                selection.add(p);
        }
        return selection;
    }
    

}
