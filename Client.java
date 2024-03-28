import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    Socket socket;
    String username;
    BufferedWriter bufferedWriter;
    BufferedReader bufferedReader;
    public Client(Socket socket,String username){
        try{
        this.socket = socket;
        this.username = username;
        bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        bufferedWriter.write(username);
        bufferedWriter.newLine();
        bufferedWriter.flush();
        }catch(IOException e){
            closeEverything(socket,bufferedReader,bufferedWriter);
        }
    }
    // Helper Functions
    public void sendMessage(){
        while (!socket.isConnected()) {
            Scanner in = new Scanner(System.in);
            String messageToSend = in.nextLine();
            try{
                bufferedWriter.write(messageToSend);
                bufferedWriter.newLine();
                bufferedWriter.flush();
                }catch(IOException e)
                {
                    closeEverything(socket, bufferedReader, bufferedWriter);
                    in.close();
                    break;
                }
        }
    }
    // Listening continously for messages
    public void ListenForMessages(){
        Runnable MyThread = ()->{
            try{
            String message = bufferedReader.readLine();
            System.out.println(message);
            }catch(IOException e){
                closeEverything(socket, bufferedReader, bufferedWriter);
            }
        };
        Thread thread = new Thread(MyThread);
        thread.start();
    }
    public void closeEverything(Socket socket,BufferedReader bufferedReader,BufferedWriter bufferedWriter){
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
    public static void main(String[] args) {
        try{
        Socket socket = new Socket("localhost",1999);
        Scanner in = new Scanner(System.in);
        System.out.println("Enter your name : ");
        String username = in.nextLine();
        Client client = new Client(socket, username);
        client.sendMessage();
        client.ListenForMessages();
        in.close();
        }catch(IOException e){
            System.out.println("ERROR : Connection cannot be established");
        }
    }
}
