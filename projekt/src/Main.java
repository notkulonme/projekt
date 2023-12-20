import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        //-----------------előkészületek------------------
        ArrayList<String> lines = readFromFile(new File("quiz.qz"));//sorok beolvasása a fileból
        ArrayList<Quiz> data = dataProcessing(lines);//sorok objecteké alakítása
        data = randomizeList(data); //kérdések sorrendjének randomizálása
        Scanner scanner = new Scanner(System.in);

        //--------------------játék----------------------
        int pontok = 0;
        System.out.println("""

                A játék célja, hogy helyesen válaszoljon az ipari forradalommal kapcsolatos quiz kérdésekre.
                A helyes válaszokért pontot kap amelyet a játék végén megtekinthet.
                A válasz megadásához csak gépelje be az előtte lévő sorszámot.
                Ha megszeretné szakítani a játékot gépeljen be nullát.
                Jó játékot!😊
                """);
        int i = 1;//számolja hanyadik kérdésnél járunk
        for(Quiz qz : data)
        {
            System.out.print(i+". ");
            qz.game();//Quiz.java fileban olvasható a működése
            System.out.print("\tAdja meg a választ: ");
            int answer = scanner.nextInt();

            if (answer == 0)//válasz ellenörzése
                break;
            if(qz.randomList[answer-1].equals(qz.goodAns))
            {
                System.out.println("\thelyes válasz!\n");
                pontok++;
            }
            else
                System.out.printf("\thelytelen válasz!\n\t\"%s\" lett volna a helyes\n\n",qz.goodAns);
            i++;

        }
        System.out.printf("Ön %d pontott kapott",pontok);
        scanner = new Scanner(System.in);
        String end = scanner.nextLine();
    }
    public static ArrayList<String> readFromFile(File file) throws FileNotFoundException//returns the lines of the file
    {
        ArrayList<String> sorok = new ArrayList<>();
        Scanner scannner = new Scanner(file);
        while(scannner.hasNextLine())
        {
            String line = scannner.nextLine();
            if(!line.contains("#") && !line.isEmpty())
                sorok.add(line);
        }
        return sorok;
    }
    public static ArrayList<Quiz> dataProcessing(ArrayList<String> lines)
    {
        ArrayList<Quiz> list = new ArrayList<>();
        for(String line : lines)
        {
            String[] sptLine = line.split(";");
            Quiz qz = new Quiz(sptLine[0],sptLine[1],sptLine[2],sptLine[3]);
            list.add(qz);
        }
        return list;
    }
    public  static ArrayList<Quiz> randomizeList(ArrayList<Quiz> arr)
    {
        Random random = new Random(Double.doubleToLongBits(Math.random()));
        ArrayList<Quiz> finalArr = new ArrayList<>();
        while(!arr.isEmpty())
        {
            int ran = random.nextInt(arr.size());
            finalArr.add(arr.get(ran));
            arr.remove(ran);
        }
        return  finalArr;
    }
}
