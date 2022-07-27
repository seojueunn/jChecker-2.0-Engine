package edu.handong.csee.java.chatparser.parser;

import edu.handong.csee.java.chatparser.ChatParser;
import edu.handong.csee.java.chatparser.datamodel.Message;
/**
 * interface Parserable
 * @author seojueun
 *
 */
public interface Parserable {
	/**
	 * parse() method
	 * @param message message content
	 * @return Message
	 */
	public Message parse(String message);
	/**
	 * getValue() method
	 * @param type enum type
	 * @return value
	 */
	public String getValue(ChatParser.ValueTypes type);
}
