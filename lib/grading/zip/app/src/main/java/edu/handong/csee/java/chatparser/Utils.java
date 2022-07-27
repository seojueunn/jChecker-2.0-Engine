package edu.handong.csee.java.chatparser;
/**
 * class Utils
 * @author seojueun
 *
 */
public class Utils {
	/**
	 * isMacExportedMessage() method
	 * @param message get message
	 * @return boolean type
	 */
	public static boolean isMacExportedMessage(String message) {
		String mes = message;
		boolean mac = false;
		
		if (mes.length() >= 20) {
			if (Character.compare(mes.charAt(0), '2') == 0 && Character.compare(mes.charAt(4), '-') == 0 && Character.compare(mes.charAt(7), '-') == 0 && Character.compare(mes.charAt(13), ':') == 0 && Character.compare(mes.charAt(16), ':') == 0) {
				mac = true;
			}
			else mac = false;
		}
		
		return mac;
	}
	/**
	 * isWindowsExportedMessage() method
	 * @param message get message
	 * @return boolean type
	 */
	public static boolean isWindowsExportedMessage(String message) {
		String mes = message;
		int num = 0;
		for (int i = 0; i < mes.length(); i++) {
			if (mes.charAt(0) == '[' && (mes.charAt(i) == '[' || mes.charAt(i) == ']')) 
				num ++;
		}
		if (num != 4 && num != 8 && num != 6) return false;
		
		return true;
	}

}
