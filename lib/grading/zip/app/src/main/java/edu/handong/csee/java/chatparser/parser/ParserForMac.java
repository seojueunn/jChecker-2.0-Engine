package edu.handong.csee.java.chatparser.parser;

import edu.handong.csee.java.chatparser.ChatParser.ValueTypes;
import edu.handong.csee.java.chatparser.datamodel.Message;
/**
 * class ParserForMac
 * @author seojueun
 *
 */
public class ParserForMac implements Parserable{
	Message parseMessage = new Message();
	/**
	 * parse() method
	 */
	@Override
	public Message parse(String message) {
		int num = 0;
		int numAdd = 0;
		for (int i = 0; i < message.length(); i++) {
			if (message.charAt(i) == ',') {
				if (i < (message.length() - 1) && message.charAt(i + 1) == ',') numAdd ++;
				else if (i == message.length() - 1) numAdd ++;
				else num ++;
			}
		}
		String[] mess = message.split(",");
		String date = mess[0].replace("\"", "");
		date = date.substring(0, date.length() - 3);
		String wrtr = mess[1].replace("\"", "");
		String mes = mess[2];
		for (int i = 1; i <= (num - 2); i ++) {
			mes = mes + "," + mess[2 + i];
		}
		for (int i = 0; i < numAdd; i ++) mes = mes + ",";
		mes = mes.replace("\"", "");
		
		parseMessage.setData(date, wrtr, mes);
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
