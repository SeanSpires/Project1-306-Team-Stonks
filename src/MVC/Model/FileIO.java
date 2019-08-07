package MVC.Model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileIO {

    public List<String> inputStringList = new ArrayList<>();

    public void writeFile(String fileName){
        File outputFile = new File(fileName + ".dot");

        String currentFileName = fileName+".dot";

        if(outputFile.exists()){
            outputFile.delete();
        }
        try {
            FileWriter fw = new FileWriter(currentFileName,true);

            for(String s : inputStringList) {
                fw.write(s + "\n");
            }
            fw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readFile(String fileName){
        File currentFile = new File(fileName);

        try (BufferedReader br = new BufferedReader((new FileReader(currentFile)))){
            String line;
            while ((line = br.readLine()) != null){
                inputStringList.add(line);
            }

        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
