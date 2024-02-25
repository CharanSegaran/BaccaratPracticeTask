package server;

import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;

public class ClientHandler implements Runnable{
    private Socket socket;
    private BacarratEngine engine;

    public ClientHandler(Socket socket, BacarratEngine engine){
        this.socket = socket;
        this.engine = engine;
    }
    

    @Override
    public void run(){
        System.out.println(Thread.currentThread().getName());
        System.out.println(Thread.currentThread().threadId());

        try{
            NetworkIOServer netIO = new NetworkIOServer(this.socket);
            System.out.println("Connected!");
            while(true){
                try{
                String[] input = netIO.read().toLowerCase().split("\\|"); //READ1
                System.out.println(Arrays.toString(input));
                String output = "";
                switch (input[0]) {
                    case "login":
                        output = BacarratEngine.login(input);
                        if(output.equals("Login Successful"))ClientDB.save(input[1], input[2]);
                        break;

                    case "bet":
                        try{
                            Integer.parseInt(input[1]);
                            if (Integer.parseInt(input[1]) <= ClientDB.getMoneyLeft()){
                                ClientDB.deductMoney(Integer.parseInt(input[1]));
                                System.out.println(String.format("User has %d left", ClientDB.getMoneyLeft()));
                                output = String.format("You have $%d left", ClientDB.getMoneyLeft());
                            }
                            else output = "insufficient amount";
                        }catch(NumberFormatException e){
                            System.out.println("I am here");
                            output = "Wrong format";
                            System.out.println(output);
                        }
                        break;

                    case "deal":
                        if (input.length == 2){
                            if(input[1].equals("b") || input[1].equals("p")){
                            output = BacarratEngine.dealPlayer() + BacarratEngine.dealBanker();
                            }
                        }
                        else{
                            output = "Wrong format";
                        }
                        break;

                    case "win":
                        output = String.format("You have $%d left", ClientDB.addMoney(Integer.parseInt(input[1]), 
                                                                                            Double.parseDouble(input[2])));
                        break;
                    
                    case "lose":
                        output = String.format("You have $%d left", ClientDB.getMoneyLeft());
                        break;
                    
                    case "draw":
                        output = String.format("You have $%d left", ClientDB.getMoneyLeft());
                        break;
                    default:
                        output = "Wrong format";
                }

                System.out.println(output);
                netIO.writeWithFlush(output);
                
                }catch(IOException e){
                    System.out.println("bye Bye");
                    break;
                }
            }
            netIO.close();
            this.socket.close();
            
        }catch(IOException e){
            e.printStackTrace();
        }
    
    }
}
