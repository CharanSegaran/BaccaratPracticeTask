package server;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CardsDB {
    static File repository;
    private static List<Double> cards;

    public static List<Double> getCards() {
        return cards;
    }

    public void setCards(List<Double> cards) {
        CardsDB.cards = cards;
    }

    public static int cardsSize(){
        return cards.size();
    }

    public static File getRepository() {
        return repository;
    }

    public void setRepository(File repository) {
        CardsDB.repository = repository;
    }

    public void staticCardsDB(String _repository){
        CardsDB.repository = new File(_repository);
    }


    public static void save(List<Double> cards) throws IOException{
        //construct cardsdb file
        String cardsDBFileName = "cards.db";
        String savedbLocation = "/Users/charansegaran/BaccaratPracticeTask/" + File.separator + cardsDBFileName;

        repository = new File(savedbLocation);
        repository.createNewFile();
        OutputStream os = new FileOutputStream(savedbLocation);
        try{
            if(!repository.exists()){
                try{
                    Path p = Paths.get(repository.getPath());
                    Files.createDirectories(p);
                }catch(FileAlreadyExistsException e){
                    System.err.println("File already exists: " + e.getMessage());
                }
                repository.createNewFile();
            }
            
            OutputStreamWriter writer = new OutputStreamWriter(os);
            BufferedWriter bw = new BufferedWriter(writer);

            try{
                for(Double card:cards){
                    try{
                        bw.write(String.valueOf(card));
                        bw.newLine();
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }finally{
                try{
                    writer.flush();
                    bw.flush();
                    writer.close();
                    bw.close();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }


        }catch(IOException e){
            e.printStackTrace();
        }finally{
            os.flush();
            os.close();
        }
    }

    
}
