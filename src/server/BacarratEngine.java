package server;

import java.io.BufferedWriter;
import java.io.Console;
import java.io.EOFException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;


public class BacarratEngine {
    //Global Variables 
    private static List<Double> shuffledCards = new ArrayList<>(6);
    private int totalNumberofDecks = 0;

    public BacarratEngine(int numOfDecks){
        this.totalNumberofDecks = numOfDecks;

        shuffledCards = BacarratEngine.shuffle(numOfDecks);
        try{
            CardsDB.save(shuffledCards);
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }

    private static int drawBanker(){
        File cardsDB =  CardsDB.getRepository();
        int bankerValue = 0;
        
        Random random = new Random();
        int randomIndex = random.nextInt(shuffledCards.size());
        bankerValue = (int) shuffledCards.get(randomIndex).doubleValue();

        shuffledCards.remove(randomIndex);
        
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(cardsDB))){
            for(double card:shuffledCards){
                writer.write(String.valueOf(card));
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        return bankerValue;
        }

    public static String dealBanker(){
        String result = "";
        result += ",B";
        int bankerValue = 0;
        for(int i=0; i<=1; i++){
            int val = drawBanker();
            if (val>10){
                val = 10;
            }
            result += String.format("|%d", val);
            bankerValue += val;
        }
        if(bankerValue%10 <= 5){
            int val = drawBanker();
            if (val>10){
                val = 10;
            }
            result += String.format("|%d", val);
            bankerValue += val;
        }
        return result;
    }
    

    private static int drawPlayer(){
        File cardsDB =  CardsDB.getRepository();
        int playerValue = 0;
        
        Random random = new Random();
        int randomIndex = random.nextInt(shuffledCards.size());
        playerValue = (int) shuffledCards.get(randomIndex).doubleValue();

        shuffledCards.remove(randomIndex);

        try(BufferedWriter writer = new BufferedWriter(new FileWriter(cardsDB))){
            for(double card:shuffledCards){
                writer.write(String.valueOf(card));
            }
        }catch(IOException e){
            e.printStackTrace();
        }

        
        return playerValue;
        }

    public static String dealPlayer(){
        String result = "";
        result += "P";
        int playerValue = 0;
        for(int i=0; i<=1; i++){
            int val = drawPlayer();
            if (val>10){
                val = 10;
            }
            result += String.format("|%d", val);
            playerValue += val;
        }
        if(playerValue%10 <= 5){
            int val = drawPlayer();
            if (val>10){
                val = 10;
            }
            result += String.format("|%d", val);
            playerValue += val;
        }
        return result;
    }

    public static String login(String[] input){
        String output = "";
        try{
            Integer.parseInt(input[2]);
            output = "Login Successful";
        }catch(IllegalArgumentException e){
             output = "Not valid num";
        }catch(ArrayIndexOutOfBoundsException e){
            output = "Wrong format";
        }
        return output;
    }

    public static List<Double> shuffle(int numOfDecks){
        for (int i = 0 ; i<numOfDecks; i++){
            for (int j = 1; j<=13; j++){
                for (int c = 1; c <= 4; c++){
                    shuffledCards.add(j + (double)c/10);
                }
            }
        }
        Collections.shuffle(shuffledCards);
        return shuffledCards;
    }
}



