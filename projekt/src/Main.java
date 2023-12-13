import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        //-----------------előkészületek------------------
        ArrayList<String> lines = readFromFile(new File("quiz.qz"));//sorok beolvasása a fileból
        ArrayList<Quiz> data = dataProcessing(lines);//sorok objecteké alakítása
        Scanner scanner = new Scanner(System.in);

        //--------------------játék----------------------
        int pontok = 0;
        System.out.println("\nA játék célja, hogy helyesen válaszoljon az ipari forradalommal kapcsolatos quiz kérdésekre.\nA helyes válaszokért pontot kap amelyet a játék végén megtekinthet.\n");
        for(Quiz qz : data)
        {
            qz.game();//Quiz.java fileban olvasható a működése
            System.out.print("\tAdja meg a választ: ");
            int answer = scanner.nextInt()-1;
            if(qz.randomList[answer].equals(qz.goodAns))//válasz ellenörzése
            {
                System.out.println("\thelyes válasz!\n");
                pontok++;
            }
            else
                System.out.printf("\thelytelen válasz!\n\t\"%s\" lett volna a helyes válasz\n\n",qz.goodAns);

        }
        System.out.printf("Ön %d pontott kapott",pontok);
    }
    public static ArrayList<String> readFromFile(File file) throws FileNotFoundException//returns the lines of the file
    {
        ArrayList<String> sorok = new ArrayList<>();
        Scanner scannner = new Scanner(file);
        while(scannner.hasNextLine())
        {
            String line = scannner.nextLine();
            if(!line.contains("#") && !line.equals(""))
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
