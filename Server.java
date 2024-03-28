import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
public class Server {
    private ServerSocket serverSocket;
    public Server(ServerSocket ss){
        serverSocket = ss;
    }
    public void startServer(){
        // While the server is on,listen for connections
        while(!serverSocket.isClosed()){
            try{
            Socket socket = serverSocket.accept();
            System.out.println("A new client has entered");
            // We will make a separate thread for each client
            ClientHandler clienthandler = new ClientHandler(socket);
            Thread thread = new Thread(clienthandler);
            thread.start();
        }
    catch(IOException e){
        close();
        break;
    }
    }}
    public void close()
    {
        try{
        if(serverSocket != null)
        {
            serverSocket.close();
        }}
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        try{
        ServerSocket serverSocket = new ServerSocket(1999);
        Server server = new Server(serverSocket);
        server.startServer();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}