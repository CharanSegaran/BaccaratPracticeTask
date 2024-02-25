package client;

import java.io.Console;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


public class ClientApp {
    static Console cons = System.console();
    static int col = 0;
    static List<String> results = new ArrayList<>();
    public static void main(String[] args) throws IOException{

        //initialize connection
        System.out.println("Hello, welcome to NUS Gambling Society! Let's play baccarat!");
        String[] conninfo = args[0].split(":");
        System.out.println(conninfo[0] + conninfo[1]);
        NetworkIOClient netIO = null;
       
        String outputToServer = "";
        String input = "";

        try(Socket sock = new Socket(conninfo[0], Integer.parseInt(conninfo[1]))){

            netIO = new NetworkIOClient(sock);

            //get input to login and send to server
            String login = cons.readLine(">Please login in this format - Login <Your Name> <Initial Amount $>: ");
            netIO.writeWithFlush(login.replace(" ", "|")); //WRITE1-READ1
            
            while((input = netIO.read()).equals("Wrong format") || input.equals("Not valid num")){
                System.out.println(input);
                login = cons.readLine(">Please login in this format - Login <Your Name> <Amount of money to bet>: ");
                netIO.writeWithFlush(login.replace(" ", "|")); //WRITE2-READ1
            }
            System.out.println(input);
        
            while(true){
                

                //ask user if they wanna quit
                if((outputToServer = (cons.readLine(">Wanna quit? Type exit, else press Enter: ")
                        .toLowerCase())).equals("exit")) break;
                    
                //time to bet LEGGO. server to tell how much left
                String betHowMuch = cons.readLine(">Place your bet in this format - Bet <Amout to bet>: ");
                netIO.writeWithFlush(betHowMuch.replace(" ", "|"));
                while((input = netIO.read()).equals("Wrong format") || input.equals("insufficient amount")){
                    System.out.println(input);
                    betHowMuch = cons.readLine(">Place your bet amount in this format - Bet <Amout to bet>: ");
                    netIO.writeWithFlush(betHowMuch.replace(" ", "|"));
                }
                System.out.println(input);

                //ask user if they wanna quit
                if((outputToServer = (cons.readLine(">Wanna quit? Type exit, else press Enter: ")
                        .toLowerCase())).equals("exit")) break;

                //who does user wanna bet on
                String betOnWho = cons.readLine(">Deal on Banker or Player in this format - Deal B or Deal P: ");
                netIO.writeWithFlush(betOnWho.replace(" ", "|"));
                while((input = netIO.read()).equals("Wrong format")){
                    System.out.println(input);
                    betOnWho = cons.readLine(">Deal on Banker or Player in this format - Deal B or Deal P: ");
                    netIO.writeWithFlush(betOnWho.replace(" ", "|"));
                }
                System.out.println(String.format("You have bet on the %s", betOnWho.split(" ")[1]));

                //take input from server and show winner
                int bankerSum = 0;
                int playerSum = 0;
                String[] result = input.split(","); 
                
                for(String indiResult: result){
                    String[] indiResults = indiResult.split("\\|");
                    if(indiResults[0].equals("P")){
                        for (int i=1; i<indiResults.length; i++){
                            playerSum += Integer.parseInt(indiResults[i]);
                        }
                    }
                    else if(indiResults[0].equals("B")){
                        for (int i=1; i<indiResults.length; i++){
                            bankerSum += Integer.parseInt(indiResults[i]);
                        }
                    }
                }
                bankerSum %= 10;
                playerSum %= 10;

                if (bankerSum > playerSum && (betOnWho.split(" ")[1]).equals("B") && bankerSum == 6){
                    System.out.println("You won! and will get 1.75x your bet amount");
                    System.out.println(String.format("Banker wins with %d points", bankerSum));
                    netIO.writeWithFlush(String.format("Win|%s|%f", betHowMuch.split(" ")[1], 1.75));
                    netIO.writeWithFlush("B");
                }
                else if (bankerSum > playerSum && (betOnWho.split(" ")[1]).equals("B") && bankerSum != 6){
                    System.out.println("You won! and will get 2.0x your bet amount");
                    System.out.println(String.format("Banker wins with %d points", bankerSum));
                    netIO.writeWithFlush(String.format("Win|%s|%f", betHowMuch.split(" ")[1], 2.0));
                    netIO.writeWithFlush(("B"));
                }
                else if (playerSum > bankerSum && (betOnWho.split(" ")[1]).equals("P")){
                    System.out.println("You won! and will get 2.0x your bet amount");
                    System.out.println(String.format("Player wins with %d points", playerSum));
                    netIO.writeWithFlush(String.format("Win|%s|%f", betHowMuch.split(" ")[1], 2.0));
                    netIO.writeWithFlush(("P"));
                }
                else if (playerSum == bankerSum){
                    System.out.println(String.format("It was a draw with both sides having %d points", bankerSum));
                    netIO.writeWithFlush("draw");
                    netIO.writeWithFlush(("D"));
                }
                else if (bankerSum > playerSum && !(betOnWho.split(" ")[1]).equals("B")){
                    System.out.println("Sorry you lost");
                    System.out.println(String.format("Banker wins with %d points", bankerSum));
                    netIO.writeWithFlush("lose");
                    netIO.writeWithFlush(("B"));
                }
                else if (bankerSum < playerSum && !(betOnWho.split(" ")[1]).equals("P")){
                    System.out.println("Sorry you lost");
                    System.out.println(String.format("Player wins with %d points", playerSum));
                    netIO.writeWithFlush("lose");
                    netIO.writeWithFlush(("P"));
                }
                
                //how much user has left
                System.out.println(input = netIO.read());
                netIO.read();
                if((input.split(" ")[2]).equals("$0")) break;
            }

        }catch(IOException e){
            System.out.println("Connection ended unexpectedly");
        }
        finally{
            netIO.close();
            if(outputToServer.equals("exit")) System.out.println("bye bye!");
            else if((input.split(" ")[2]).equals("$0")) System.out.println("Sorry you ran out of money");
            
        }
    }
}
            


