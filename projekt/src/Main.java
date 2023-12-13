import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        ArrayList<String> lines = readFromFile(new File("quiz.qz"));
        ArrayList<Quiz> data = dataProcessing(lines);
        Scanner scanner = new Scanner(System.in);
        int pontok = 0;

        for(Quiz qz : data)
        {
            qz.printAll();
        }
    }
    public static ArrayList<String> readFromFile(File file) throws FileNotFoundException//returns the lines of the file
    {
        ArrayList<String> sorok = new ArrayList<>();
        Scanner scannner = new Scanner(file);
        while(scannner.hasNextLine())
        {
            String line = scannner.nextLine();
            if(line.charAt(0)!='#')
                sorok.add(line);
        }
        return sorok;
    }
    public static ArrayList<Quiz> dataProcessing(ArrayList<String> lines)
    {
        ArrayList<Quiz> list = new ArrayList<>();
        for(String line : lines)
        {
            String sptLine[] = line.split(";");
            Quiz qz = new Quiz(sptLine[0],sptLine[1],sptLine[2],sptLine[3]);
            list.add(qz);
        }
        return list;
    }
}