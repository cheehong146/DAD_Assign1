package ver2;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.function.Consumer;

/**
 * Created by User on 19/10/2017.
 */
public abstract class NetworkConnection extends Thread{
    private Consumer<Serializable> onReceiveCallback;
    private ConnectionThread connectionThread = new ConnectionThread();

    public NetworkConnection(Consumer<Serializable> onReceiveCallback){
        this.onReceiveCallback = onReceiveCallback;
        connectionThread.setDaemon(true);//set to daemon thread, if all user thread dies, it dies
    }

    public void startConnection()throws Exception{
        connectionThread.start();
    }

    public void endConnection() throws Exception{
        connectionThread.socket.close();
    }

    public void send(Serializable data) throws Exception{
        connectionThread.out.writeObject(data);
    }
    protected abstract void setPort(int port);
    protected abstract void setIPAddress(String IPAddress);
    protected abstract boolean isServer();
    protected abstract String getIPAddress();
    protected abstract int getPort();

    private class ConnectionThread extends Thread{
        Socket socket;
        ObjectOutputStream out;

        @Override
        public void run() {
            try(ServerSocket server = isServer() ? new ServerSocket(getPort()) : null;
                Socket socket = isServer() ? server.accept() : new Socket(getIPAddress(), getPort());//if isServer() True, accept from serverSocket, else create new socket
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {


                this.socket = socket;
                this.out = out;
                socket.setTcpNoDelay(true);

                out.writeObject((isServer()? "Server" : "Client") + " connection formed\n");

                while(true){
                    Serializable data = (Serializable) in.readObject();
                    onReceiveCallback.accept(data);
                }
            }catch(Exception e){
                onReceiveCallback.accept("Connection closed\n");
                System.out.println((isServer()? "Server" : "Client ") + "Program closed due to socket closed");
            }


        }
    }
}
