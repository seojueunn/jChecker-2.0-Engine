package edu.isel.csee.jchecker2_0.core.stage;

import java.util.ArrayList;

/**
 * Interface for compile and execution
 */
public interface IGradeStage {

	/**
	 * Method for compile
	 * @param dpath source codes path
	 * @return state
	 */
	public int compile(String dpath);

	/**
	 * Method for execution
	 * @param cases result of testing with oracle input
	 * @param output oracle output
	 * @param dpath source code path
	 * @return result
	 */
	public boolean build(ArrayList<String> cases, String output, String dpath);

	/**
	 * Method for testing
	 * @param argument oracle input
	 * @param isTest isTest
	 * @return result
	 */
	public ArrayList<String> getTest(String argument, boolean isTest);

	/**
	 * Method for testing
	 * @param argument main path
	 * @param cases oracle input
	 * @param isTest isTest
	 * @return result
	 */
	public ArrayList<String> getTest(String argument, String cases, boolean isTest);
}
