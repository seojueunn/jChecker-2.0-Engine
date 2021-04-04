package edu.isel.csee.jchecker.submit;

import java.io.File;

public class GithubConnector {

	
	public void clone(String URL, String workpath)
	{
		ProcessBuilder builder = null;
		
		
		try {
			builder = new ProcessBuilder("git clone " + URL);
			builder.directory(new File(workpath));
			
			Process process = builder.start();
			process.destroy();
			
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
