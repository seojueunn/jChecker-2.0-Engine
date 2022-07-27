package edu.handong.csee.java.chatparser.datamodel;
/**
 * class Message
 * @author seojueun
 *
 */
public class Message {
	private String datetime;
	private String writer;
	private String message;
	/**
	 * setData() method
	 * @param date get datetime
	 * @param wrtr get writer
	 * @param mes get message
	 */
    public void setData(String date, String wrtr, String mes) {
    	datetime = date;
    	writer = wrtr;
        message = mes;
    }
    /**
     * getDatetime() method
     * @return datetime value of datetime field
     */
    public String getDatetime(){
        return datetime;
    }
    /**
     * getWriter() method
     * @return writer value of writer field
     */
    public String getWriter(){
        return writer;
    }
    /**
     * getMessage() method
     * @return message value of message field
     */
    public String getMessage(){
        return message;
    }
    /**
     * print() method
     */
	public void print() {
		System.out.println("When: " + datetime);
		System.out.println("Who: " + writer);
		System.out.println("What: " + message);
	}
}
