import java.time.LocalDateTime;
import java.util.Random;
public class Quiz {
    String qstion,answ1,answ2,goodAns;
    String[] randomList;

    public Quiz(String qstion, String answ1, String answ2, String goodAns)
    {
        this.qstion = qstion;
        this.answ1 = answ1;
        this.answ2 = answ2;
        this.goodAns = goodAns;

    }
    public void game()
    {
        /*-------------------lista randomizálása---------------------------------*/
        Random random = new Random(LocalDateTime.now().getSecond());
        String rnad[] = {answ1,answ2,goodAns};
        int i1 = random.nextInt(3);//random indexek generálása
        int i2 = random.nextInt(3);
        while(i1 == i2)//új index generálása amíg a kettő megegyezeik
        {
            i2 = random.nextInt(3);
        }
        int i3 = 3-i1-i2;//harmadik index kiszámolása
        String answrand[] = {rnad[i1],rnad[i2],rnad[i3]};//a lista randomizálása
        this.randomList = answrand;//random lista lálthatóvá tétele
        /*----------------a ranodomizált lista kiírása---------------------------*/
        System.out.println(qstion+"\n\t\t1."+randomList[0]+" 2."+randomList[1]+" 3."+randomList[2]);//kiírás
    }
    
}
