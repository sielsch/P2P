package comClientServer;

public class P2PFile {

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

	public boolean equals(P2PFile file) {
		return file.taille == taille && file.name == name;
	}
	
	
}
