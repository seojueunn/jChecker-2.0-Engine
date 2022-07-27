package edu.handong.csee.java.chatparser.parser.exception;

/**
 * class DataFileNotFoundException
 * @author seojueun
 *
 */
public class DataFileNotFoundException extends Exception {
	/**
	 * DataFileNotFoundException()
	 */
	public DataFileNotFoundException() {
		super("DataFileNotFoundException: ");
	}
	/**
	 * DataFileNotFoundException
	 * @param message get message
	 */
	public DataFileNotFoundException(String message) {
		super(message);
	}
}
