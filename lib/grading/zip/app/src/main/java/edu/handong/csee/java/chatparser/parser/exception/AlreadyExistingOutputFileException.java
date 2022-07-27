package edu.handong.csee.java.chatparser.parser.exception;

/**
 * class AlreadyExistingOutputFileException
 * @author seojueun
 *
 */
public class AlreadyExistingOutputFileException extends Exception {
	/**
	 * AlreadyExistingOutputFileException()
	 */
	public AlreadyExistingOutputFileException() {
		super("AlreadyExistingOutputFileException: ");
	}
	/**
	 * AlreadyExistingOutputFileException
	 * @param message get message
	 */
	public AlreadyExistingOutputFileException (String message) {
		super(message);
	}
}
