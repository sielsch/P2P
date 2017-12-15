package comClientServer;

import java.io.Serializable;

public class AdressServerTCP implements Comparable<AdressServerTCP>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5741505366900812225L;
	private int port;
	private String host;

	public AdressServerTCP(int port, String host) {
		super();
		this.port = port;
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int compareTo(AdressServerTCP adr) {
		int compare = this.getHost().compareTo(adr.getHost());
		if (compare == 0) {
			if (this.getPort() < adr.getPort()) {
				compare = -1;
			} else if (this.getPort() > adr.getPort()) {
				compare = 1;
			} else {
				compare = 0;
			}
		}
		return compare;
	}

}
