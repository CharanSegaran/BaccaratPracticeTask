package server;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ClientDB {

    private static File repository;
    private static int moneyLeft;

    public static int getMoneyLeft() {
        return moneyLeft;
    }

    public static void setMoneyLeft(int moneyLeft) {
        ClientDB.moneyLeft = moneyLeft;
    }

    public static File getRepository() {
        return repository;
    }

    public void setRepository(File repository) {
        ClientDB.repository = repository;
    }

    public static void save(String name, String money) throws IOException{
        //player's filename
        String fileName = name + ".db";

        //construct clientDB file
        String savedbLocation = "/Users/charansegaran/BaccaratPracticeTask/" + File.separator + fileName;
        moneyLeft = Integer.parseInt(money);

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
                    try{
                        bw.write(money);
                        bw.newLine();
                    }catch(Exception e){
                        e.printStackTrace();
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

    public static void deductMoney(int betPlaced){
        moneyLeft -= betPlaced;
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(repository))){
            writer.write(String.valueOf(moneyLeft));
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public static int addMoney(int betPlaced, double times){
        moneyLeft += (int) (betPlaced * times);
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(repository))){
            writer.write(String.valueOf(moneyLeft));
        }catch(IOException e){
            e.printStackTrace();
        }

        return moneyLeft;
    }

}
