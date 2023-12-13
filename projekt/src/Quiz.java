import java.util.Arrays;
import java.util.Random;
public class Quiz {
    String qstion,answ1,answ2,goodAns;
    String[] ansrand;

    public Quiz(String qstion, String answ1, String answ2, String goodAns)
    {
        this.qstion = qstion;
        this.answ1 = answ1;
        this.answ2 = answ2;
        this.goodAns = goodAns;

    }
    public void game()
    {
        Random random = new Random();
        String rnad[] = {answ1,answ2,goodAns};
        int i1 = random.nextInt(2);
        int i2 = random.nextInt(2);
        while(i1 == i2)
        {
            i2 = random.nextInt(2);
        }
        int i3 = 3-i1-i2;
        System.out.println(i1+" "+i2+" "+i3);
        String answrand[] = {rnad[i1],rnad[i2],rnad[i3]};
        System.out.println(Arrays.toString(answrand)+"\n"+ Arrays.toString(rnad));
        this.ansrand = answrand;
        System.out.println(qstion+"\n\t1."+ansrand[0]+" 2."+ansrand[1]+" 3."+ansrand[2]);
    }

}
