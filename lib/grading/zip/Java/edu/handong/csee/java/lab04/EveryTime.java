package edu.handong.csee.java.lab04;
import edu.handong.csee.java.lab04.elements.*;

    /**
     * This is EveryTime class containing the nickname.
     */
public class EveryTime {

    private Anonymous anonymous;
    private Nick nickName;

    /**
     * This is a main method.
     * @param args to user's nick name
     */
    public static void main(String[] args){

        EveryTime runner = new EveryTime();
        runner.run(args);
  
    }
    /**
     * This is a run method.
     * @param args to get user's nick name
     */
    public void run(String[] args){

        anonymous = new Anonymous();
        anonymous.setAnonymous(args[0]);

        nickName = new Nick();

        if(anonymous.getAnonymous().equals("Yes")){
            anonymous. setAnonymous("익명");
            nickName. setNickName(anonymous.getAnonymous(), anonymous.getRandom());
        }

        if(anonymous.getAnonymous().equals("No")){
            nickName.setNickName(args[1], Integer.parseInt(args[2]));
        }

        System.out.println(nickName.getNickName());
    }
}