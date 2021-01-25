package edu.isel.csee.jchecker.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class GradleStage implements IGradeStage {
	
	
	@Override
	public int compile(String dpath)
	{
		int state = -1;
		ProcessBuilder builder = null;
		Process process = null;
		
		
		try {
			builder = new ProcessBuilder(getCommand());
			builder.directory(new File(dpath));
			
			process = builder.start();
			state = process.waitFor();
			process.destroy();
		} catch(Exception e) {
			System.out.println("Compile Error: Fatal error in compile stage.");
			e.printStackTrace();
		}
		
		
		return state;
	}
	

	@Override
	public boolean build(ArrayList<String> cases, String output, String dpath)
	{
		boolean result = false;
		BufferedReader stdout = null;
		ProcessBuilder builder = null;
		Process process = null;
		
		
		try {
			builder = new ProcessBuilder(cases);
			builder.directory(new File(dpath));
			
			process = builder.start();
			
			
			stdout = new BufferedReader(new InputStreamReader(process.getInputStream(), "UTF-8"));
			
			StringBuffer sb = new StringBuffer();
			String line;
			boolean flag = false;
			
			
			while( (line = stdout.readLine()) != null)
			{
				if (line.contains("> Task :app:run")) {
					flag = true;
					continue;
				}
				
				
				if (line.contains("BUILD SUCCESSFUL in"))
					flag = true;
				
				if (flag)
					sb.append(line + "\n");
			}
			
			
			String answer = sb.toString();
			
			if (output.equals(answer.trim()))
				result = true;
			
			
			stdout.close();
			process.destroy();
			
			
		} catch(Exception e) {
			System.out.println("Runtime Error: Fatal error in execute stage.");
			e.printStackTrace();
		}
		
		
		return result;
	}
	
	
	
	public ArrayList<String> getTest(String argument)
	{
		ArrayList<String> command = new ArrayList<>();
		
		command.add("gradle");
		command.add("run");
		command.add("--warning-mode=all");
		command.add("--args=");
		command.add("argument");
		
		return command;
	}
	
	
	
	private ArrayList<String> getCommand()
	{
		ArrayList<String> command = new ArrayList<>();
		
		command.add("gradle");
		command.add("build");
		command.add("-Dfile.encoding=UTF-8");
		
		return command;
	}
}
