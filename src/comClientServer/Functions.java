package comClientServer;

import java.io.File;
import java.util.TreeSet;

public class Functions {

    public TreeSet<P2PFile> directoryToListFile(String directoryName) {
        TreeSet t = new TreeSet<P2PFile>();
                
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
