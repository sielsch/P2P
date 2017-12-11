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
    
    private TreeMap<String, TreeSet<P2PFile>> annuaire;
    
    public synchronized boolean containsKey(String key){
        return annuaire.containsKey(key);        
    }
    
    public synchronized TreeSet<P2PFile> getByKey(String key){
        if (containsKey(key))
            return annuaire.get(key);
        else
            return null;
    }
    
    public synchronized void put(String key, TreeSet<P2PFile> ts){
        annuaire.put(key, ts);
    }   
     
}
