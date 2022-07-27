package edu.handong.csee.java.chatparser.parser;

import edu.handong.csee.java.chatparser.ChatParser.ValueTypes;
import edu.handong.csee.java.chatparser.datamodel.Message;
/**
 * class ParserForWindows
 * @author seojueun
 *
 */
public class ParserForWindows implements Parserable{
	Message parseMessage = new Message();
	/**
	 * parse() method
	 */
	@Override
	public Message parse(String message) {
		int num = 0;
		for (int i = 0; i < message.length(); i++) {
			if (message.charAt(i) == ']') 
				num ++;
		}
		String[] mess = message.split("]");
		String wrtr = mess[0].replace("[", "").trim();
		String mes = mess[2];
		for (int i = 1; i <= (num - 2); i ++) {
			mes = mes + "]" + mess[2 + i];
		}
		
		String times = "";
		String date = mess[1].replace("[", "").trim();
		String[] dateDN = date.split(" ");
		if (dateDN[0].contains("오전")) {
			String[] time = dateDN[1].split(":");
			int houri = Integer.parseInt(time[0]);
			if (houri >= 1 && houri <= 9) {
				String hour = Integer.toString(houri);
				times = "0" + hour + ":" + time[1];
			}
			else times = dateDN[1].trim();
		}
		else if (dateDN[0].contains("오후")) {
			String[] time = dateDN[1].split(":");
			int houri = Integer.parseInt(time[0]);
			if (houri >= 1 && houri <= 11) houri = houri + 12;
			String hour = Integer.toString(houri);
			times = hour + ":" + time[1];
		}
		else if (dateDN[1].contains("AM")) {
			String[] time = dateDN[0].split(":");
			int houri = Integer.parseInt(time[0]);
			if (houri >= 1 && houri <= 9) {
				String hour = Integer.toString(houri);
				times = "0" + hour + ":" + time[1];
			}
			else times = dateDN[0].trim();
		}
		else if (dateDN[1].contains("PM")) {
			String[] time = dateDN[0].split(":");
			int houri = Integer.parseInt(time[0]);
			if (houri >= 1 && houri <= 11) houri = houri + 12;
			String hour = Integer.toString(houri);
			times = hour + ":" + time[1];
		}
		
		parseMessage.setData(times, wrtr, mes);
		return parseMessage;
	}
	/**
	 * getValue() method
	 */
	@Override
	public String getValue(ValueTypes type) {
		if (type == ValueTypes.DATETIME) return parseMessage.getDatetime();
		else if (type == ValueTypes.WRITER) return parseMessage.getWriter();
		return parseMessage.getMessage();
	}
}
