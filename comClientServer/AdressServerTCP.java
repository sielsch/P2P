package comClientServer;

public class AdressServerTCP implements Comparable<AdressServerTCP> {

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


}
