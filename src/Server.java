import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server{
    private static List<Handler> clients = new ArrayList<>();

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(8000);
            System.out.println("Server has started..., on port 8000");

            while(true) {
                Socket socket = serverSocket.accept();
                System.out.println("client connected..."+socket.getInetAddress().getAddress());

                Handler handler = new Handler(socket);
                clients.add(handler);
                new Thread(handler).start();

            }
        }catch(IOException e){
            e.printStackTrace();
        }

    }


    static void broadcast(String message, Handler handler){
        for (Handler client:clients){
            if (client!=handler){
                client.sendMessage(handler.getClientName()+" : "+message);
            }
        }
    }
}

class Handler implements Runnable{

    private BufferedReader bufferedReader;
    private PrintWriter printWriter;
    private String clientName;
    private Socket socket;


    public Handler(Socket socket){
        this.socket=socket;
    }

    public String getClientName(){
        return clientName;
    }

    public void sendMessage(String message){
        printWriter.println(message);
    }

    @Override
    public void run() {
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            printWriter = new PrintWriter(socket.getOutputStream(),true);

            printWriter.println("hello!, what is your name?");
            clientName = bufferedReader.readLine();
            printWriter.println(clientName+" : Connected");

            Server.broadcast("has joined",this);

            String message;
            while ((message=bufferedReader.readLine())!=null){
                Server.broadcast(message,this);
            }

            Server.broadcast("user left...",this);
            bufferedReader.close();
            printWriter.close();
            socket.close();


        }catch (IOException e){
            e.printStackTrace();
        }

    }

}