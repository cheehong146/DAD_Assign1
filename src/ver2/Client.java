package ver2;

import java.io.Serializable;
import java.util.function.Consumer;

/**
 * Created by User on 19/10/2017.
 */
public class Client extends NetworkConnection {

    private String IPAddress;
    private int port;

    public Client(String IPAddress, int port, Consumer<Serializable> onReceiveCallback) {
        super(onReceiveCallback);
        this.IPAddress = IPAddress;
        this.port = port;
    }

    @Override
    protected boolean isServer() {
        return false;
    }

    @Override
    protected String getIPAddress() {
        return IPAddress;
    }

    @Override
    protected int getPort() {
        return port;
    }

    @Override
    protected void setPort(int port) {
        this.port = port;
    }

    @Override
    protected void setIPAddress(String IPAddress) {
        this.IPAddress = IPAddress;
    }
}
