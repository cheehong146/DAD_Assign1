package ver3;
import java.io.Serializable;
import java.util.function.Consumer;

/**
 * Created by User on 19/10/2017.
 */
public class Server extends NetworkConnection {

    private int port;

    public Server(int port, Consumer<Serializable> onReceiveCallback){
        super(onReceiveCallback);
        this.port = port;
    }

    @Override
    protected boolean isServer() {
        return true;
    }

    @Override
    protected String getIPAddress() {
        return null;
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
    }
}
