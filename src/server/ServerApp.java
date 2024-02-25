package server;

import java.io.EOFException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
public class ServerApp {
    public static void main(String[] args) throws IOException{
        
        System.out.println("Hello, You are in NUS Gambling Society's server. Someone wants to play Baccarat!");
        ExecutorService threadPool = Executors.newFixedThreadPool(2);
        int port = Integer.parseInt(args[0]);
        System.out.printf("The server has started on port %s: \n", port );
        try (ServerSocket server = new ServerSocket(port)){
            int numOfDecks = Integer.parseInt(args[1]);
            BacarratEngine engine = new BacarratEngine(numOfDecks);

            while(true){
                System.out.println("Waiting for client connection...");
                Socket socket = server.accept();
                System.out.println("Connected!");
                ClientHandler clientHandler = new ClientHandler(socket, engine);
                threadPool.submit(clientHandler);
                System.out.println("Submitted to threadpool");
            }
        }catch(EOFException e){
            e.printStackTrace();
        }
    }
}
