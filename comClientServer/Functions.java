package comClientServer;

import java.io.File;
import java.util.HashSet;

public class Functions {

    public HashSet<P2PFile> directoryToListFile(String directoryName) {
        HashSet<P2PFile> t = new HashSet<>();
                
        File folder = new File(directoryName);
        File[] listOfFiles = folder.listFiles();

        if(listOfFiles.length == 0){
            System.out.println("EMPTY DIRECTORY");
        }
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                P2PFile p = new P2PFile(listOfFiles[i].length(),listOfFiles[i].getName());
                t.add(p);
            }
        }
        
        return t;
    }
}
