package edu.handong.csee.java.lab04.elements;

public class Nick {
    
    String nickname;
    int i = 0;
    String count;
 
    public void setNickName(String name, int num) {
        nickname = name;
        i = num;
        count = Integer.toString(i);
    }
  
    public String getNickName(){
        return nickname.concat(count);
    }
  
 }