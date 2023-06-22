package edu.isel.csee.jchecker2_0.submit;

import java.io.File;

/**
 * Class for checking github repository
 */
public class GithubConnector 
{
	/**
	 * Method for clone the repository
	 * @param URL repository url
	 * @param workpath workspace path
	 */
	public void clone(String URL, String workpath)
	{
		ProcessBuilder builder = null;
		
		try 
		{
			builder = new ProcessBuilder("git clone " + URL);
			builder.directory(new File(workpath));
			
			Process process = builder.start();
			process.destroy();
			
			
		} catch(Exception e) { e.printStackTrace(); }
	}
}
