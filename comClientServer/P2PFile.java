package comClientServer;

import java.io.Serializable;

public class P2PFile implements Comparable<P2PFile> , Serializable {

	
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 5134500275816488234L;
	private long taille;
    private String name;

    public P2PFile(long taille, String name) {
        super();
        this.taille = taille;
        this.name = name;
    }

    public long getTaille() {
        return taille;
    }

    public void setTaille(long taille) {
        this.taille = taille;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + (int) (taille ^ (taille >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        P2PFile other = (P2PFile) obj;
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        if (taille != other.taille) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(P2PFile p) {
        int compare = this.getName().compareTo(p.getName());
        if (compare == 0) {
            if (this.getTaille() < p.getTaille()) {
                compare = -1;
            } else if (this.getTaille() > p.getTaille()) {
                compare = 1;
            } else {
                compare = 0;
            }
        }
        return compare;
    }
    
    @Override
    public String toString() {
        return "" + this.name + ", [" + this.taille + " octets]";
    }

}
