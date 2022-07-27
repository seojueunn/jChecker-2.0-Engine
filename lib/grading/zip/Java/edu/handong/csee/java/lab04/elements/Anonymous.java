package edu.handong.csee.java.lab04.elements;

public class Anonymous {
 
    public String anonymous;
    int i = (int)(Math.random() * 100);

    public int getRandom(){
        return i;
    }
 
    public void setAnonymous(String an) {
        anonymous = an;
    }
  
    public String getAnonymous(){
        return anonymous;
    }
  
 }