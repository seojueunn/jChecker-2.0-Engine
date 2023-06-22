package edu.isel.csee.jchecker2_0.core.stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Class for compile with Build Tool
 */
public class GradleStage implements IGradeStage {
	/**
	 * Method for compile
	 * @param dpath source codes path
	 * @return state
	 */
	@Override
	public int compile(String dpath) {
		int state = -1;
		ProcessBuilder builder = null;
		Process process = null;
		BufferedReader stdout = null;

		listup(dpath);

		try {
			builder = new ProcessBuilder(getCommand());
			builder.directory(new File(dpath));

			builder.redirectErrorStream(true);

			process = builder.start();

			stdout = new BufferedReader(new InputStreamReader(process.getInputStream(), "UTF-8"));

			String line;
			boolean flag = false;

			while((line = stdout.readLine()) != null) {
				if (line.contains("No dependencies")) {
					System.out.println("No dependencies");
					flag = true;
				}

				if (line.contains("BUILD FAILED in")) {
					System.out.println("BUILD FAILED");
					flag = true;
				}
			}

			state = flag ? -1 : 0;

			process.waitFor();
			process.destroy();

		} catch (Exception e) {
			System.out.println("Compile Error: Fatal error in compile stage.");
			e.printStackTrace();
		}

		System.out.println("Compile state : " + state);
		return state;
	}

	/**
	 * Method for execution
	 * @param cases result of testing with oracle input
	 * @param output oracle output
	 * @param dpath source code path
	 * @return result
	 */
	@Override
	public boolean build(ArrayList<String> cases, String output, String dpath) {
		boolean result = false;
		BufferedReader stdout = null;
		ProcessBuilder builder = null;
		Process process = null;

		try {
			builder = new ProcessBuilder(cases);
			builder.directory(new File(dpath));
			process = builder.start();

			// Wait for the process to complete for 10 seconds
			if (!process.waitFor(20, TimeUnit.SECONDS)) {
				// Timeout - destroy the process.
				process.destroy();

				// test case violation
				return false;
			}

			stdout = new BufferedReader(new InputStreamReader(process.getInputStream(), "UTF-8"));

			StringBuffer sb = new StringBuffer();
			String line;

			while((line = stdout.readLine()) != null) {
				sb.append(line + "\n");
			}

			String answer = sb.toString();

			System.out.println("\n<Program output>");
			System.out.println(answer);

			System.out.println("<Expected output>");
			System.out.println(output);

			if (output.equals(answer.trim())) {
				result = true;
			}

			if (result) {
				System.out.println("\n--> correct\n");
			} else {
				System.out.println("\n--> incorrect\n");
			}

			process.waitFor();
			stdout.close();
			process.destroy();

		} catch (Exception e) {
			System.out.println("Runtime Error: Fatal error in execute stage.");
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * Method for testing
	 * @param argument oracle input
	 * @param isTest isTest
	 * @return command
	 */
	public ArrayList<String> getTest(String argument, boolean isTest) {
		ArrayList<String> command = new ArrayList<>();

		command.add("gradle");
		command.add("run");
		command.add("--warning-mode=all");
		command.add("-q");

		if (isTest && !argument.isEmpty()) {
			System.out.println("********************************************************************************");
			System.out.println("input : " + argument);
			command.add("--args=" + argument);
		}

		return command;
	}

	/**
	 * Method for testing
	 * @param argument main path
	 * @param cases oracle input
	 * @param isTest isTest
	 * @return result
	 */
	public ArrayList<String> getTest(String argument, String cases, boolean isTest) {
		return null;
	}

	/**
	 * Method for command setting
	 * @return command
	 */
	private ArrayList<String> getCommand() {
		ArrayList<String> command = new ArrayList<>();

		command.add("gradle");
		command.add("build");
		command.add("-Dfile.encoding=UTF-8");

		return command;
	}

	/**
	 * Method for listing all files names
	 * @param dpath main path
	 */
	private void listup(String dpath) {
		ArrayList<String> command = new ArrayList<>();

		command.add("bash");
		command.add("-c");
		command.add("find . -name '*.java' > srclist.txt");

		ProcessBuilder builder = null;
		Process process = null;

		try {
			builder = new ProcessBuilder(command);
			builder.directory(new File(dpath));
			process = builder.start();

			process.waitFor();
			process.destroy();

		} catch (Exception e) {
			System.out.println("File Error: No java files in the path: " + dpath);
			e.printStackTrace();
		}
	}
}
