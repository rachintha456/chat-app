import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        try {

            Socket socket = new Socket("127.0.0.1",8000);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(),true);

            System.out.println(bufferedReader.readLine());

            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter the Name");
            String clientName = scanner.nextLine();
            printWriter.println(clientName);

            String rsData = bufferedReader.readLine();
            System.out.println(rsData);

            Thread thread = new Thread(new MessageHandler(socket));
            thread.start();

            while (true){
               String message = scanner.nextLine();
               printWriter.println(message);
            }


        }catch (IOException e){
            e.printStackTrace();
        }
    }
}

class MessageHandler implements Runnable{

    private Socket socket;

    MessageHandler(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String message;
            while ((message=bufferedReader.readLine())!=null){
                System.out.println(message);
            }
        }catch (Exception e){
           e.printStackTrace();
        }
    }
}