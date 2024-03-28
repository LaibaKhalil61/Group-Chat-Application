import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable{
    // List to contain ClientHandlers(sockets) of all other clients
    // so that I can loop through each of them and broadcast msgs to them 
    public static ArrayList<ClientHandler>ClientHandlers = new ArrayList<>();
    // Info to be stored about a client/connection
    BufferedReader bufferedReader ;
    BufferedWriter bufferedWriter ;
    Socket socket;
    // Will be used to tell other clients that this user has entered or left the chat
    String username;
    public ClientHandler(Socket socket){
        try{
        this.socket = socket;
        ClientHandlers.add(this);
        bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        username = bufferedReader.readLine();
        broadcastMessage("Server : "+username+" has entered the chat");
        }catch(IOException e){
            closeEverything(socket,bufferedReader,bufferedWriter);
        }
    }
    // Helper Functions
    public void ListenForMessages(){
        while(!socket.isConnected()){
            try{
            String messageToSend = bufferedReader.readLine();
            broadcastMessage(username+" "+messageToSend);
            }
            catch(IOException e){
                closeEverything(socket, bufferedReader, bufferedWriter);
                break;
            }
        }
    }
    public void broadcastMessage(String MessageToSend){
        for (ClientHandler Client : ClientHandlers) {
            try{
            if (!Client.username.equals(username)) {
                bufferedWriter.write(MessageToSend);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        }catch(IOException e)
        {
            closeEverything(socket, bufferedReader, bufferedWriter);
            break;
        }
        }
    }
    public void closeEverything(Socket socket,BufferedReader bufferedReader,BufferedWriter bufferedWriter){
        ClientHandlers.remove(this);
        broadcastMessage("Server : "+username+" has left the chat");
        try{
        if(socket != null)
        {
            socket.close();
        }
        if(bufferedReader != null)
        {
            bufferedReader.close();
        }
        if(bufferedWriter != null)
        {
            bufferedWriter.close();
        }
    }catch(IOException e){
        e.printStackTrace();
    }
    }
    @Override
    public void run() {
        ListenForMessages();
    }
}
