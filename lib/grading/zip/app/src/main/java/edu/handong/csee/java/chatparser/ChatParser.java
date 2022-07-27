package edu.handong.csee.java.chatparser;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.SortedSet;
import java.util.TreeSet;

import edu.handong.csee.java.chatparser.datamodel.Message;
import edu.handong.csee.java.chatparser.parser.ParserForMac;
import edu.handong.csee.java.chatparser.parser.ParserForWindows;
import edu.handong.csee.java.chatparser.parser.Parserable;
import edu.handong.csee.java.chatparser.parser.exception.AlreadyExistingOutputFileException;
import edu.handong.csee.java.chatparser.parser.exception.DataFileNotFoundException;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
/**
 * class ChatParser
 * @author seojueun
 *
 */
public class ChatParser {
	
	private int numOfMessages=0;
	private String ipath;
	private String opath;
	private String cdate;
	private String writer;
	private boolean help;
	/**
	 * enum ValueTypes
	 * @author seojueun
	 *
	 */
	public static enum ValueTypes {
		/**
		 * DATETIME
		 */
		DATETIME,
		/**
		 * WRITER
		 */
		WRITER,
		/**
		 * MESSAGE
		 */
		MESSAGE
	};
	/**
	 * main() method
	 * @param args message content
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		
		ChatParser myChatParser = new ChatParser();
		
		myChatParser.run(args);

	}
	/**
	 * run() method
	 * @param ChatMessages message content
	 * @throws IOException 
	 */
	public void run(String[] ChatMessages) throws IOException {
		
		Options options = createOptions();
		
		if(parseOptions(options, ChatMessages)){
			if (help){
				printHelp(options);
				return;
			}
			
			int messageIndex = 0;
			try {
				File fileRead = new File(ipath);
				File fileWrte = new File(opath);
				boolean isReadExists = fileRead.exists();
				boolean isWrteExists = fileWrte.exists();
				String line = "";
				
				if (isReadExists == false) {
					DataFileNotFoundException err = new DataFileNotFoundException(ipath);
					throw err;
				}
				
				
				if (isWrteExists == true) {
					String readln = "";
					int readnum = 0;
					BufferedReader readStream = new BufferedReader(new FileReader(fileRead));
					while ((readln = readStream.readLine()) != null) {
						if (Utils.isMacExportedMessage(readln) || Utils.isWindowsExportedMessage(readln)) {
							String wrt = "";
							if (Utils.isMacExportedMessage(readln)) {
								String[] spl = readln.split(",");
								wrt = spl[1];
							}
							else if (Utils.isWindowsExportedMessage(readln)) {
								String[] spl = readln.split("]");
								wrt = spl[1];
							}
							if (writer == null && cdate == null) readnum ++;
							else if (writer != null && cdate == null) {
								if (wrt.contains(writer)) readnum ++;
							}
							else if (writer == null && cdate != null) {
								if (readln.contains(cdate)) readnum ++;
							}
							else if (writer != null && cdate != null) {
								if (readln.contains(cdate) && wrt.contains(writer)) readnum ++;
							}
						}
						
					}
					
					if (writer == null && cdate == null) System.out.println("The number of all messages processing: " + readnum + "\n");
					else if (writer != null && cdate == null) System.out.println("The number of messages filtered by the writer, " + writer + ": " + readnum + "\n");
					else if (writer == null && cdate != null) System.out.println("The number of messages filtered by the date, " + cdate + ": " + readnum + "\n");
					else if (writer != null && cdate != null) System.out.println("The number of messages filtered by the date, " + cdate + ", and the writer, " + writer + ": " + readnum + "\n");
					
					throw new AlreadyExistingOutputFileException(opath);
				}
				
				
				String year = "";
				String month = "";
				String day = "";
				
				String date = "";
				String getwrter = "";
				String time = "";
				String whatmes = "";
				String result = "";
				
				Parserable parser = null;
				boolean ismac = false;
				boolean iswin = false;
				
				
				BufferedReader inputStream = new BufferedReader(new FileReader(fileRead));
				BufferedWriter outputStream = new BufferedWriter(new FileWriter(fileWrte));
				HashMap<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();
				
				if (ipath.contains(".txt")) {
					
					while ((line = inputStream.readLine()) != null) {
						boolean chck = false; // format check_windows or mac
						boolean apch = true; // filter check for messages with "\n"
						
						String currentChatMessage = line;
						
						if (Utils.isWindowsExportedMessage(currentChatMessage)) {
							parser = new ParserForWindows();
							iswin = true; // format for windows
							chck = true; // initial_false
							whatmes = "";
							result = "";
							time = "";
						}
						else if (currentChatMessage.contains("2021 ---------------") || 
								 currentChatMessage.contains("2021-1 JC") || currentChatMessage.contains("Date Saved") ||
								 currentChatMessage.contains("joined this chatroom.") || currentChatMessage.contains("left.") ||
								 currentChatMessage.contains("저장한 날짜 :") || currentChatMessage.contains("--------------- 2021년") ||
								 currentChatMessage.contains("이 들어왔습니다.") || currentChatMessage.contains("이 나갔습니다.")) {
							whatmes = "";
							if (currentChatMessage.contains("2021 ---------------")) {
								year = "2021";
								String[] mess = currentChatMessage.split(",");
								String dateM_D = mess[1].trim();
								String[] dateMD = dateM_D.split(" ");
								int dayi = Integer.parseInt(dateMD[1]);
								if (dayi >= 1 && dayi <= 9) {
									String days = Integer.toString(dayi);
									day = "0" + days;
								}
								else day = dateMD[1];
								if (dateMD[0].equals("March")) month = "03";
								else if (dateMD[0].equals("April")) month = "04";
								else if (dateMD[0].equals("May")) month = "05";
								else if (dateMD[0].equals("June")) month = "06";
							}
							else if (currentChatMessage.contains("--------------- 2021년")) {
								year = "2021";
								String[] mess = currentChatMessage.split(" ");
								month = "0" + mess[2].replace("월", "").trim();
								String dayst = mess[3].replace("일", "").trim();
								int dayi = Integer.parseInt(dayst);
								if (dayi >= 1 && dayi <= 9) {
									String days = Integer.toString(dayi);
									day = "0" + days;
								}
								else day = dayst;
							}
							
							apch = false; // filtering_initial true
						}
						
						if (chck == true) { // main message
							messageIndex ++;
							if (iswin == true) {
								Message newMessage = parser.parse(currentChatMessage);
								time = newMessage.getDatetime().trim();
								date = year  + "-" + month + "-" + day + " "; // date
								date = date + time;
								getwrter = newMessage.getWriter().trim();
								whatmes = newMessage.getMessage().trim();
							}
							
							result = "When: " + date + "\n" + "Who: " + getwrter + "\n" + "What: " + whatmes;
							String keyChck = date + getwrter;
							if (map.containsKey(keyChck)) {
								ArrayList<String> containsKey = map.get(keyChck);
								containsKey.add(result);
								map.put(keyChck, containsKey);
							}
							else {
								String key = date + getwrter;
								ArrayList<String> newKey = new ArrayList<>();
								newKey.add(result);
								map.put(key, newKey);
							}
						}
						else if (messageIndex != 0 && chck == false && apch == true) {
							
							result = result + currentChatMessage;
							
							String keyChck = date + getwrter;
							if (map.containsKey(keyChck)) {
								ArrayList<String> containsKey = map.get(keyChck);
								containsKey.add(currentChatMessage);
								map.put(keyChck, containsKey);
							}
							
						}
						
					} // while end
				}
				else if (ipath.contains(".csv")) {
					
					String Date = "";
					String User = "";
					String Message = "";
					int count = 0;
					
					Iterable<CSVRecord> records = CSVFormat.RFC4180.withHeader("Date", "User", "Message").parse(inputStream);
					for (CSVRecord record : records) {
						if (count == 0) {
							count ++;
							continue;
						}
						Date = record.get("Date");
						Date = Date.substring(0, Date.length() - 3);
					    User = record.get("User");
					    Message = record.get("Message");
					    
					    result = "When: " + Date + "\n" + "Who: " + User + "\n" + "What: " + Message;
					    
					    
					    String keyChck = Date + User;
					    if (map.containsKey(keyChck)) {
							ArrayList<String> containsKey = map.get(keyChck);
							containsKey.add(result);
							map.put(keyChck, containsKey);
						}
						else {
							String key = Date + User;
							ArrayList<String> newKey = new ArrayList<>();
							newKey.add(result);
							map.put(key, newKey);
						}
					}	
				}
				
				
				if (writer == null && cdate == null) {
					messageIndex = 0;
					SortedSet<String> keys = new TreeSet<>(map.keySet());
					for (String key : keys) { 
						ArrayList<String> value = map.get(key);
						String add = "";
						for (int i = 1; i < value.size(); i ++) {
							String addMess = value.get(i);
							if (!(addMess.contains("When:") && addMess.contains("Who:") && addMess.contains("What:"))) {
								add = add + "\n" +addMess;
							}
						}
						if (add != "") value.set(0, value.get(0) + add);
						Collections.sort(value);
						for (String mes : value) {
							if (mes.contains("When:") && mes.contains("Who:") && mes.contains("What:")) {
								if (messageIndex > 0) outputStream.append("\n\n");
								outputStream.append("Parsing Message " + (++messageIndex) + "\n");
								outputStream.append(mes);
							}
						}
					}
				}
				
				
				if (writer != null && cdate == null) {
					messageIndex = 0;
					SortedSet<String> keys = new TreeSet<>(map.keySet());
					for (String key : keys) {
						if (key.contains(writer)) {
							ArrayList<String> value = map.get(key);
							String add = "";
							for (int i = 1; i < value.size(); i ++) {
								String addMess = value.get(i);
								if (!(addMess.contains("When:") && addMess.contains("Who:") && addMess.contains("What:"))) {
									add = add + "\n" +addMess;
								}
							}
							if (add != "") value.set(0, value.get(0) + add);
							Collections.sort(value);
							for (String mes : value) {
								if (mes.contains("When:") && mes.contains("Who: " + writer) && mes.contains("What:")) {
									if (messageIndex > 0) outputStream.append("\n\n");
									outputStream.append("Parsing Message " + (++messageIndex) + "\n");
									outputStream.append(mes);
								}
							}
						}
						else continue;
					}
				}
				
				
				if (writer == null && cdate != null) {
					messageIndex = 0;
					SortedSet<String> keys = new TreeSet<>(map.keySet());
					for (String key : keys) {
						if (key.contains(cdate)) {
							ArrayList<String> value = map.get(key);
							String add = "";
							for (int i = 1; i < value.size(); i ++) {
								String addMess = value.get(i);
								if (!(addMess.contains("When:") && addMess.contains("Who:") && addMess.contains("What:"))) {
									add = add + "\n" +addMess;
								}
							}
							if (add != "") value.set(0, value.get(0) + add);
							Collections.sort(value);
							for (String mes : value) {
								if (mes.contains("When: " + cdate) && mes.contains("Who:") && mes.contains("What:")) {
									if (messageIndex > 0) outputStream.append("\n\n");
									outputStream.append("Parsing Message " + (++messageIndex) + "\n");
									outputStream.append(mes);
								}
							}
						}
						else continue;
					}
				}
				
				
				if (writer != null && cdate != null) {
					messageIndex = 0;
					SortedSet<String> keys = new TreeSet<>(map.keySet());
					for (String key : keys) {
						if (key.contains(writer) && key.contains(cdate)) {
							ArrayList<String> value = map.get(key);
							String add = "";
							for (int i = 1; i < value.size(); i ++) {
								String addMess = value.get(i);
								if (!(addMess.contains("When:") && addMess.contains("Who:") && addMess.contains("What:"))) {
									add = add + "\n" +addMess;
								}
							}
							if (add != "") value.set(0, value.get(0) + add);
							Collections.sort(value);
							for (String mes : value) {
								if (mes.contains("When: " + cdate) && mes.contains("Who:") && mes.contains("What:")) {
									if (messageIndex > 0) outputStream.append("\n\n");
									outputStream.append("Parsing Message " + (++messageIndex) + "\n");
									outputStream.append(mes);
								}
							}
						}
						else continue;
					}
				}
				

				inputStream.close();
				outputStream.close();
				
				setNumOfMessages(messageIndex);
				if (messageIndex >= 0) {
					if (writer == null && cdate == null) System.out.println("The number of all messages processing: " + getNumOfMessages() + "\n");
					else if (writer != null && cdate == null) System.out.println("The number of messages filtered by the writer, " + writer + ": " + getNumOfMessages() + "\n");
					else if (writer == null && cdate != null) System.out.println("The number of messages filtered by the date, " + cdate + ": " + getNumOfMessages() + "\n");
					else if (writer != null && cdate != null) System.out.println("The number of messages filtered by the date, " + cdate + ", and the writer, " + writer + ": " + getNumOfMessages() + "\n");
					System.out.println("All the parsed messages are saved in " + opath);
				}	
			}
			/**
			 * catch DataFileNotFoundException
			 */
			catch (DataFileNotFoundException e) {
				// TODO Auto-generated catch block
				System.out.println("DataFileNotFoundException: " + e.getMessage());
			} 
			/**
			 * catch AlreadyExistingOutputFileException
			 */
			catch (AlreadyExistingOutputFileException e) {
				// TODO Auto-generated catch block
				System.out.println("AlreadyExistingOutputFileException: " + e.getMessage());
			}
		}
		
	}
	/**
	 * getNumOfMessages() method
	 * @return numOfMessages
	 */
	public int getNumOfMessages() {
		return numOfMessages;
	}
	/**
	 * setNumOfMessage() method
	 * @param numOfMessages number of message
	 */
	public void setNumOfMessages(int numOfMessages) {
		this.numOfMessages = numOfMessages;
	}
	
	private boolean parseOptions(Options options, String[] args) {
		CommandLineParser parser = new DefaultParser();

		try {

			CommandLine cmd = parser.parse(options, args);

			ipath = cmd.getOptionValue("i");
			opath = cmd.getOptionValue("o");
			cdate = cmd.getOptionValue("d");
			writer = cmd.getOptionValue("w");
			help = cmd.hasOption("h");

		} catch (Exception e) {
			printHelp(options);
			return false;
		}

		return true;
	}

	// Definition Stage
	private Options createOptions() {
		Options options = new Options();

		
		// add options by using OptionBuilder
		options.addOption(Option.builder("h").longOpt("help")
				.desc("Show a Help page")
				.build());
		
		options.addOption(Option.builder("i").longOpt("input")
				.desc("Set an input data file path for chat messages")
				.hasArg()
				.argName("Input file path")
				.required()
				.build());
		
		options.addOption(Option.builder("o").longOpt("output")
				.desc("Set an output data file path for chat messages")
				.hasArg()
				.argName("Output file path")
				.required()
				.build());
		
		// add options by using OptionBuilder
		options.addOption(Option.builder("d").longOpt("date")
				.desc("Apply a filter by a specific date")
				.hasArg()
				.argName("Date filter")
				//.required()
				.build());
		
		options.addOption(Option.builder("w").longOpt("writer")
				.desc("Apply a filter by a specific writer")
				.hasArg()
				.argName("Writer filter")
				//.required()
				.build());

		return options;
	}
	
	private void printHelp(Options options) {
		// automatically generate the help statement
		HelpFormatter formatter = new HelpFormatter();
		String header = "Chatparser program";
		String footer ="\nThis chatparser is implemented in 2021-1 Java class.";
		formatter.printHelp("chatparser", header, options, footer, true);
	}

}